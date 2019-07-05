//run on page load
function init() {
  let uId = readCookie('loggedInUid');

  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/role/getRolesForUser.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let roles = JSON.parse(this.responseText);
      let powered = false;
      let benco = false;
      let i = 0;
      //used to figure out approval permissions
      roles.forEach(role => {
        setCookie(`userRole_${i++}`, role.name);
        if (role.can_approve_ri_basic || role.can_approve_ri_intermediate) {
          powered = true;
        } else if (role.can_approve_ri_super) {
          benco = true;
        }
      });
      populateUserBox();

      if (benco) {
        getAllUsers();
      } else if (powered) {
        getUnderlings(uId);
      } else {
        getUserRequests(uId, true);
      }
    }
  }
  xhttp.send();
}

function getAllUsers() {
  let xhttp = new XMLHttpRequest();
  const uId = readCookie('loggedInUid');

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUsers.do');

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let users = JSON.parse(this.responseText);
      getAllRequestsForUsers(uId, users);
    }
  }
  xhttp.send();
}

function getUnderlings(uId) {
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUnderlings.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let users = JSON.parse(this.responseText);
      getAllRequestsForUsers(uId, users);
    }
  }
  xhttp.send();
}

function getAllRequestsForUsers(uId, users) {
  getUserRequests(uId, true);
  users.forEach(user => {
    if (user.u_id !== uId) {
      getUserRequests(user.u_id);
    }
  });
}

function reloadAllReimbursements() {
  document.getElementById('reimbursements').innerHTML = ``;
  init();
}

function getUserRequests(uId, single = false) {
  let params = `?u_id=${uId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/reimbursement/getReimbursementsByUser.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let reimbursements = JSON.parse(this.responseText);

      getUserSpecificData(reimbursements, uId, single);
    } else if (this.readyState === XMLHttpRequest.DONE && this.status === 404) {
      if (!document.getElementById(`err_no_re`) && document.getElementById('reimbursements').childElementCount < 1) {
        document.getElementById(`reimbursements`).parentElement.insertAdjacentHTML('afterend', `<span id="err_no_re" class="text-danger">No reimbursements found!</span>`);
      }
    }
  }
  xhttp.send();
}


function getUserSpecificData(reimbursements, uId, single = false) {
  let loggedInUid = readCookie('loggedInUid');

  if ((uId === loggedInUid) && !single) {
    return;
  }

  let params = `?u_id=${uId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUser.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      const user = JSON.parse(this.responseText);
      let stats = '';
      if (!single) {
        let html = populateStatsContainer(reimbursements);

        stats = `
          <li class="list-group-item" id="user_${user.u_id}">
            <h3 class="text-dark">Amount available for ${prettyPipe(user.username)} in tuition reimbursement.</h3>
            ${html}
          </li>
        `;
      }


      getReimbursementData(reimbursements, stats);
    }
  }
  xhttp.send();

}



function timeLeftOnReimbursement(submissionDate, eventStartDate) {
  submissionDate = new Date(submissionDate).getTime();
  eventStartDate = new Date(eventStartDate).getTime();
  let now = Date.now();
  let twoWeeksFromNow = now + (14 * 24 * 60 * 60 * 1000);

  //add week from submission date
  let oneWeekFromSubDate = submissionDate + (7 * 24 * 60 * 60 * 1000);
  let timeUntilAutoApproval = (oneWeekFromSubDate - now) / 1000 / 60 / 60;
  let message = 'until this request is auto approved.';
  let sclass = 'text-success';
  //if less than 14 days, mark as urgent
  if ((twoWeeksFromNow - eventStartDate) >= 0) {
    message += `<h4 class="text-danger">===URGENT REQUIRES ACTION!===</h4>`;
  }
  if ((timeUntilAutoApproval / 24) <= 14) {
    sclass = 'text-warning';
  }
  if ((timeUntilAutoApproval / 24) <= 7) {
    sclass = 'text-danger';
  }

  //if more than 48 hours, display days
  if (timeUntilAutoApproval >= 48) {
    return `
      <span class="${sclass}">${(timeUntilAutoApproval / 24).toFixed(2)} days left</span> ${message}
    `;
  } else if (timeUntilAutoApproval >= 0 && timeUntilAutoApproval < 48) {
    return `
      <span class="text-danger">${timeUntilAutoApproval.toFixed(2)} hours left</span> ${message}
    `;
  } else {
    return `This reimbursement has been auto approved.`;
  }
}

function singleCommentHtml(comment, rId, uId) {
  let html = ``;
  if (uId === comment.u_id) {
    html += `
    <li class="list-group-item" id="comment_${comment.c_id}">
      <div class="container">
        <h4>${comment.title} - ${comment.status}</h4>
        <p>${comment.body}</p>
        <button type="button" onclick="replyToComment(this)" data-cid=${comment.c_id} value="${rId}" class="btn btn-dark btn-sm">Reply</button>
        <button type="button" onclick="deleteComment(this)" data-cid=${comment.c_id} value="${rId}" class="btn btn-danger btn-sm">Delete</button>
      </div>
    </li>
  `;
  } else {
    html += `
    <li class="list-group-item" id="comment_${comment.c_id}">
      <div class="container">
        <h4>${comment.title} - ${comment.status}</h4>
        <p>${comment.body}</p>
        <button type="button" onclick="replyToComment(this)" data-cid=${comment.c_id} value="${rId}" class="btn btn-dark btn-sm">Reply</button>
      </div>
    </li>
  `;
  }
  return html;
}

function allCommentsHtml(comments, rId) {
  let uId = readCookie('loggedInUid');
  let html = ``;
  comments.forEach(comment => {
    html += singleCommentHtml(comment, rId, uId);
  });
  return html;

}


function reimbursementHtml(reimbursement, comments) {
  let roles = getRoles();
  let baseHtml = ``;
  const uId = readCookie('loggedInUid');

  baseHtml = baseReimbursementHtml(reimbursement);
  roles.forEach(role => {
    if (reimbursement.stage === 0 && (role === `dictator` || (role === `supervisor` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true);
    } else if (reimbursement.stage === 1 && (role === `dictator` || (role === `department_head` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true);
    } else if (reimbursement.stage === 2 && (role === `dictator` || (role === `benco` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true);
    } else if (reimbursement.stage === 3 && (role === `benco` && uId !== reimbursement.u_id)) {

      baseHtml = baseReimbursementHtml(reimbursement, true);
    }
  });



  if (comments) {
    return baseHtml + allCommentsHtml(comments, reimbursement.r_id) + '</ul></li>';
  } else {
    return baseHtml + '</ul></li>';
  }
}

function approveRequest(event) {
  let rId = event.value;
  let stage = Number(event.getAttribute('data-stage')) + 1;
  let amountRequested = event.getAttribute('data-requested');
  let status = `pending_supervisor_approval`;
  /**
    reimbursement.setStatus("approval_by_supervisor");
    reimbursement.setStatus("approval_by_department_head");
    reimbursement.setStatus("approval_by_first_benco");
    reimbursement.setStatus("appoval_by_final_benco");
   */
  switch (stage) {
    case 1:
      status = `approval_by_supervisor`;
      break;
    case 2:
      status = `approval_by_department_head`;
      break;
    case 3:
      status = `approval_by_first_benco`;
      break;
    case 4:
      status = `approval_by_final_benco`;
  }
  let paramsList = `r_id=${rId}&stage=${stage}&status=${status}`;
  if (stage >= 4) {
    paramsList += `&amount_granted=${amountRequested}&amount_requested=0`;
  }
  let xhttp = new XMLHttpRequest();

  xhttp.open('POST', 'http://localhost:8080/project1/api/reimbursement/updateReimbursement.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      document.getElementById(`reimbursement_${rId}_approve_btn`).disabled = true;
      event.setAttribute('data-stage', stage + 1);
      let reListItem = document.getElementById(`reimbursement_${rId}`);
      if (!document.getElementById(`re_${rId}_app_success`)) {
        reListItem.insertAdjacentHTML('afterend', successMessage(`Successfully approved reimbursement ${rId}`, `re_${rId}_app_success`));
      }
    }
  }
  xhttp.send(paramsList);
}

function baseReimbursementHtml(reimbursement, approve = false) {
  if (reimbursement.stage === 4) {
    return `
      <li class="list-group-item" id="reimbursement_${reimbursement.r_id}">
        <div class="input-group">
          <button class="btn btn-danger btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_delete_btn" value="${reimbursement.r_id}" onclick="deleteRequest(this)">Delete</button>
          <button type="button" onclick="replyToComment(this)" data-cid=${reimbursement.r_id} value="${reimbursement.r_id}" class="btn btn-dark btn-sm">Reply</button>
        </div>
        ${basicReimbursementInformation(reimbursement)}
    `;
  }
  if (approve) {
    return `
      <li class="list-group-item" id="reimbursement_${reimbursement.r_id}">
        <div class="input-group" id="actions_for_${reimbursement.r_id}">
          <div class="input-group-prepend">
            <button class="btn btn-primary btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_edit_btn" value='${JSON.stringify(reimbursement)}' onclick="openEditForm(this)">Edit</button>
            <button class="btn btn-success btn-sm" type="button" data-stage="${reimbursement.stage}" data-requested="${reimbursement.amount_requested}" id="reimbursement_${reimbursement.r_id}_approve_btn" value="${reimbursement.r_id}" onclick="approveRequest(this)">Approve</button>
            <button class="btn btn-danger btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_delete_btn" value="${reimbursement.r_id}" onclick="deleteRequest(this)">Delete</button>
            <button class="btn btn-dark btn-sm" type="button" onclick="replyToComment(this)" data-cid=${reimbursement.r_id} value="${reimbursement.r_id}" >Reply</button>
          </div>
          <div class="input-group-append">
            <div class="custom-file">
              <label class="custom-file-label" id="attachment_label" for="attachments_for_${reimbursement.r_id}">(+) Attachments</label>
              <input onchange="handleAddAttachment(this)" type="file" class="custom-file-input" id="attachments_for_${reimbursement.r_id}">
            </div>
          </div>
        </div>
      ${basicReimbursementInformation(reimbursement)}
    `;
  }
  return `
  <li class="list-group-item" id="reimbursement_${reimbursement.r_id}">
    <div class="input-group" id="actions_for_${reimbursement.r_id}">
      <div class="input-group-prepend">
        <button class="btn btn-primary btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_edit_btn" value='${JSON.stringify(reimbursement)}' onclick="openEditForm(this)">Edit</button>
        <button class="btn btn-danger btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_delete_btn" value="${reimbursement.r_id}" onclick="deleteRequest(this)">Delete</button>
        <button class="btn btn-dark btn-sm" type="button" onclick="replyToComment(this)" data-cid=${reimbursement.r_id} value="${reimbursement.r_id}" >Reply</button>
      </div>
      <div class="input-group-append">
        <div class="custom-file">
          <label class="custom-file-label" id="attachment_label_for_${reimbursement.r_id}" for="attachments_for_${reimbursement.r_id}">(+) Attachments</label>
          <input onchange="handleAddAttachment(this, prepareAttachment)" data-rid="${reimbursement.r_id}" type="file" class="custom-file-input" id="attachments_for_${reimbursement.r_id}">
        </div>
        <button class="btn btn-light btn-sm" type="button" onclick="showAttachments(this)" data-uid="${reimbursement.u_id}" value="${reimbursement.r_id}">Show attachments</button>
      </div>
    </div>
   ${basicReimbursementInformation(reimbursement)}
`;
}

function basicReimbursementInformation(reimbursement) {
  return `
    <div class="container">
      <p class="text-dark">Event (ID: ${reimbursement.r_id}) ${reimbursement.event_type} starts on <span class="font-weight-bold"><u>${new Date(reimbursement.event_start_date).toDateString()}</u></span> and has ${timeLeftOnReimbursement(reimbursement.submission_date, reimbursement.event_start_date)}</p>
      <span class="text-info">Submitted on ${new Date(reimbursement.submission_date).toDateString()}</span>
    </div>
    <div class="row">
      <div class="col-md-6">
        <p class="text-white bg-info text-center">This request is at the ${prettyPipe(reimbursement.status)} stage.</p>        
        <p class="text-dark">
          <h4 class="text-dark">Description - ${reimbursement.event_description}</h4>
          <blockquote class="blockquote">${reimbursement.justification_comment}
          <footer class="blockquote-footer"><cite title="Note from the employee">Note from the employee</cite></footer>
          </blockquote>
          <p class="text-dark">Requesting <span class="text-success">$${reimbursement.amount_requested}</span>.</p>
        </p>
        </div>
      <div class="col-md-6 container">
        <div id="attachments_for_${reimbursement.r_id}_by_${reimbursement.u_id}" class="list-group"></div>
      </div>
    </div>
    <ul class="list-group" id="comments_for_reimbursement_${reimbursement.r_id}">
    `;
}

function showAttachments(event) {
  const rId = event.value;
  const uId = +event.getAttribute('data-uid');
  getAttachmentsByUserForReimbursement(uId, rId, (attachments) => {
    attachments.forEach(attachment => {
      ele = document.getElementById(`attachments_for_${rId}_by_${uId}`);
      ele.insertAdjacentHTML('beforeend', attachmentHtml(attachment));
    });
  })
}

function getReimbursementData(reimbursements, stats) {
  let container = document.getElementById('reimbursements');

  for (let i = 0; i < reimbursements.length; i++) {
    const reimbursement = reimbursements[i];
    getComments(reimbursement, container, stats);
  }
}

function sendReply(event) {
  const rId = event.value;
  const title = document.getElementById(`reply_to_${event.getAttribute('data-cid')}_title`).value;
  const body = document.getElementById(`reply_to_${event.getAttribute('data-cid')}_body`).value;
  const username = readCookie(`loggedInUsername`);
  const uId = readCookie(`loggedInUid`);
  const comment = {
    'r_id': rId,
    'title': title,
    'body': body,
    'status': username,
    'u_id': uId
  }
  let xhttp = new XMLHttpRequest();
  const paramsList = `r_id=${comment.r_id}&body=${comment.body}&title=${comment.title}&status=${comment.status}&u_id=${comment.u_id}`;

  xhttp.open('POST', 'http://localhost:8080/project1/api/comment/createComment.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      let commentsContainer = document.getElementById(`comments_for_reimbursement_${rId}`);
      commentsContainer.insertAdjacentHTML('beforeend', singleCommentHtml(comment, comment.r_id, comment.u_id));
    }
  }
  xhttp.send(paramsList);
}

function getCommentForm(cId, rId) {
  return `
    <div class="form-control" id="comment_${cId}_form">
      <label for="reply_to_${cId}_title">Title</label>  
      <input class="form-control" type="text" name="reply_to_${cId}_title" id="reply_to_${cId}_title" required>
      <label for="reply_to_${cId}_body">Body</label>  
      <textarea class="form-control" type="text" name="reply_to_${cId}_body" id="reply_to_${cId}_body" required></textarea>
      <button onclick="sendReply(this)" data-cid="${cId}" value="${rId}" type="button" class="btn btn-primary btn-sm">Submit</button>
    </div>
  `;
}

function replyToComment(event) {
  let parent = document.getElementById(`comment_${event.getAttribute('data-cid')}`);
  let commentForm = document.getElementById(`comment_${event.getAttribute('data-cid')}_form`);
  if (commentForm) {
    parent.removeChild(commentForm);
  } else {
    parent.insertAdjacentHTML('beforeend', getCommentForm(event.getAttribute('data-cid'), event.value));
  }
}



function getComments(reimbursement, container, stats) {
  let rId = reimbursement.r_id;
  let params = `?r_id=${rId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/comment/getCommentsForReimbursement.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let comments = JSON.parse(this.responseText);
      let commentsContainer = document.getElementById(`comments_for_reimbursement_${rId}`);
      if (commentsContainer) {
        commentsContainer.innerHTML = ``;
      }
      if (!document.getElementById(`user_${reimbursement.u_id}`)) {
        container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, comments) + stats);
      } else {
        container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, comments));
      }

    } else if (this.readyState === XMLHttpRequest.DONE && this.status === 404) {
      if (!document.getElementById(`user_${reimbursement.u_id}`)) {
        container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, null) + stats);
      } else {
        container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, null));
      }
    }
  }
  xhttp.send();
}

function openEditForm(event) {
  const reimbursement = JSON.parse(event.value);
  const rId = reimbursement.r_id;
  let container = document.getElementById('reimbursement_' + rId);
  let existingEditForm = document.getElementById(`re_${rId}_edit_form`);
  //toggles showing the form
  if (existingEditForm) {
    document.getElementById(`reimbursement_${rId}_edit_btn`).innerHTML = `Edit`;
    existingEditForm.parentNode.removeChild(existingEditForm);
  } else {
    document.getElementById(`reimbursement_${rId}_edit_btn`).innerHTML = `Close`;
    container.insertAdjacentHTML('afterend', requestForm(reimbursement));
  }
}

function makeRequestObject(rId) {

  let {
    eventStartDate,
    eventEndDate,
    eventDailyStartTime,
    eventAddress,
    eventDescription,
    amountRequested,
    eventPassingGrade,
    justificationComment
  } = getDetails(rId);

  let uId = readCookie('loggedInUid');
  let selectedEventTypeOption = findOption(`re_${rId}_event_type`);
  let eventType = null;
  let percentCoverage = null;
  if (selectedEventTypeOption && selectedEventTypeOption.length > 0) {
    eventType = selectedEventTypeOption[0];
    percentCoverage = selectedEventTypeOption[1];
  }
  if (eventStartDate) {
    eventStartDate = new Date(eventStartDate.value).getTime();
  }
  if (eventEndDate) {
    eventEndDate = new Date(eventEndDate.value).getTime();
  }
  if (eventDailyStartTime) {
    eventDailyStartTime = eventDailyStartTime.value;
  }
  if (eventAddress) {
    eventAddress = eventAddress.value;
  }
  if (eventDescription) {
    eventDescription = eventDescription.value;
  }
  if (amountRequested) {
    amountRequested = amountRequested.value;
  }
  let eventGradingFormat = document.getElementById(`re_${rId}_event_grading_format_2`);

  if (eventGradingFormat.value) {
    eventGradingFormat = eventGradingFormat.value;
  } else {
    eventGradingFormat = findOption(`re_${rId}_event_grading_format`) ? findOption(`re_${rId}_event_grading_format`)[0] : 'Custom';
  }

  if (justificationComment) {
    justificationComment = justificationComment.value;
  }
  let reimbursement = {
    'r_id': rId,
    'event_type': eventType,
    'percent_coverage': percentCoverage,
    'event_start_date': eventStartDate,
    'event_end_date': eventEndDate,
    'event_daily_start_time': eventDailyStartTime,
    'event_address': eventAddress,
    'event_description': eventDescription,
    'amount_requested': amountRequested,
    'event_grading_format': eventGradingFormat,
    'event_passing_grade': eventPassingGrade,
    'justification_comment': justificationComment,
    'u_id': uId
  };

  return reimbursement;
}

function getParamsList(reimbursement) {
  let paramsList = `u_id=${reimbursement.u_id}&r_id=${reimbursement.r_id}`;
  console.log(reimbursement);

  if (reimbursement.event_type) {
    paramsList += `&event_type=${reimbursement.event_type}`;
  }

  if (reimbursement.percent_coverage) {
    paramsList += `&percent_coverage=${reimbursement.percent_coverage}`;
  }

  if (reimbursement.event_start_date) {
    paramsList += `&event_start_date=${reimbursement.event_start_date}`;
  }

  if (reimbursement.event_end_date) {
    paramsList += `&event_end_date=${reimbursement.event_end_date}`;
  }

  if (reimbursement.event_daily_start_time) {
    paramsList += `&event_daily_start_time=${reimbursement.event_daily_start_time}`;
  }

  if (reimbursement.event_address) {
    paramsList += `&event_address=${reimbursement.event_address}`;
  }

  if (reimbursement.event_description) {
    paramsList += `&event_description=${reimbursement.event_description}`;
  }

  if (reimbursement.amount_requested) {
    paramsList += `&amount_requested=${reimbursement.amount_requested}`;
  }

  if (reimbursement.event_grading_format) {
    paramsList += `&event_grading_format=${reimbursement.event_grading_format}`;
  }

  if (reimbursement.event_passing_grade) {
    paramsList += `&event_passing_grade=${reimbursement.event_passing_grade}`;
  }

  if (reimbursement.justification_comment) {
    paramsList += `&justification_comment=${reimbursement.justification_comment}`;
  }
  return paramsList;
}

function handleConfirm(event) {
  let reimbursement = makeRequestObject(event.value);

  const paramsList = getParamsList(reimbursement);
  let xhttp = new XMLHttpRequest();

  xhttp.open('POST', 'http://localhost:8080/project1/api/reimbursement/updateReimbursement.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      console.log(this.responseText);

      let reEditForm = document.getElementById(`re_${reimbursement.r_id}_edit_form`);
      if (!document.getElementById(`re_${reimbursement.r_id}_success`)) {
        reEditForm.insertAdjacentHTML('afterend', successMessage(`Successfully updated reimbursement ${reimbursement.r_id}`, `re_${reimbursement.r_id}_edit_success`));
      }

    }
  }
  xhttp.send(paramsList);
}

function successMessage(message, id) {
  if (id) {
    return `
      <span id=${id} class="text-white bg-success">${message}</span>
    `;
  }
  return `
    <span class="text-white bg-success">${message}</span>
  `;
}

function deleteRequest(event) {
  const rId = event.value;
  let xhttp = new XMLHttpRequest();
  const paramsList = `r_id=${rId}`;

  xhttp.open('POST', 'http://localhost:8080/project1/api/reimbursement/deleteReimbursement.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      console.log(this.responseText);
      let reimbursement = document.getElementById(`reimbursement_${rId}`);
      reimbursement.innerHTML = '';
    }
  }
  xhttp.send(paramsList);
}