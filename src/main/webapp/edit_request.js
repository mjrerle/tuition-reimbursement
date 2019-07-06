//run on page load

function init() {
  let uId = readCookie('loggedInUid');

  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUser.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      const user = JSON.parse(this.responseText);
      getUserRoles(user);
    }
  }
  xhttp.send();
}

function setRoles(roles) {
  let i = 0;
  let powered = false;
  let benco = false;
  roles.forEach(role => {
    setCookie(`userRole_${i++}`, role.name);
    if (role.can_approve_ri_basic || role.can_approve_ri_intermediate) {
      powered = true;
    } else if (role.can_approve_ri_super) {
      benco = true;
    }
  });
  return {
    benco,
    powered
  };
}

function getUserRoles(user) {
  let uId = user.u_id;

  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/role/getRolesForUser.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let roles = JSON.parse(this.responseText);
      //used to figure out approval permissions
      let {
        benco,
        powered
      } = setRoles(roles);
      populateUserBox();

      if (benco) {
        getAllUsers(user);
      } else if (powered) {
        getUnderlings(user);
      } else {
        getUserRequests(user, uId, true);
      }
    }
  }
  xhttp.send();
}


function getAllUsers(user) {
  let xhttp = new XMLHttpRequest();
  const uId = user.u_id;

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUsers.do');

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let users = JSON.parse(this.responseText);
      getAllRequestsForUsers(user, users);
    }
  }
  xhttp.send();
}

function getUnderlings(user) {
  const uId = user.u_id;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUnderlings.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let otherUsers = JSON.parse(this.responseText);
      getAllRequestsForUsers(user, otherUsers);
    }
  }
  xhttp.send();
}

function getAllRequestsForUsers(user, otherUsers) {
  const uId = user.u_id;
  getUserRequests(user, true);

  otherUsers.forEach(otherUser => {
    if (otherUser.u_id !== uId) {
      getUserRequests(otherUser);

    }
  });
}

function reloadAllReimbursements() {
  document.getElementById('reimbursements').innerHTML = ``;
  init();
}

function getUserRequests(user, single = false) {
  const uId = user.u_id;
  let params = `?u_id=${uId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/reimbursement/getReimbursementsByUser.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let reimbursements = JSON.parse(this.responseText);
      getUserSpecificData(reimbursements, user, single);
    } else if (this.readyState === XMLHttpRequest.DONE && this.status === 404) {
      if (!document.getElementById(`err_no_re`) && document.getElementById('reimbursements').childElementCount < 1) {
        document.getElementById(`reimbursements`).parentElement.insertAdjacentHTML('afterend', `<span id="err_no_re" class="text-danger">No reimbursements found!</span>`);
      }
    }
  }
  xhttp.send();
}


function getUserSpecificData(reimbursements, user, single = false) {
  const uId = user.u_id;
  let loggedInUid = readCookie('loggedInUid');

  //return because this function will print the stats container
  if ((uId === loggedInUid) && !single) {
    return;
  }
  let stats = '';
  if (!single) {
    let html = populateStatsContainer(reimbursements);

    stats = `
      ${html}
    `;
  }

  getReimbursementData(user, reimbursements, stats);

  // let params = `?u_id=${uId}`;
  // let xhttp = new XMLHttpRequest();

  // xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUser.do' + params);

  // // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  // xhttp.onreadystatechange = function () {
  //   if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
  //     const user = JSON.parse(this.responseText);
  //     let stats = '';

  // }
  // }
  // xhttp.send();

}



function timeLeftOnReimbursement(reimbursement) {
  let submissionDate = reimbursement.submission_date;
  let eventStartDate = reimbursement.event_start_date;
  submissionDate = new Date(submissionDate).getTime();
  eventStartDate = new Date(eventStartDate).getTime();
  let now = Date.now();
  let twoWeeksFromNow = now + (14 * 24 * 60 * 60 * 1000);

  //add week from submission date
  let oneWeekFromSubDate = submissionDate + (7 * 24 * 60 * 60 * 1000);
  let timeUntilAutoApproval = (oneWeekFromSubDate - now) / 1000 / 60 / 60;
  let message = 'until this request is auto-approved.';
  let sclass = 'text-success';
  //if less than 14 days, mark as urgent
  if ((twoWeeksFromNow - eventStartDate) >= 0) {
    message += `<h4 class="bg-danger text-white text-center">===URGENT REQUIRES ACTION!===</h4>`;
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
    autoApprove();
    return `This reimbursement has been auto approved.`;
  }
}

function singleCommentHtml(comment, rId, uId) {
  let html = ``;
  if (uId === comment.u_id) {
    html += `
    <li class="list-group-item" id="comment_${comment.c_id}_for_${rId}">
      <div class="container">
        <h4>${comment.title} - ${prettyPipe(comment.status)}</h4>
        <p>${comment.body}</p>
        <button type="button" onclick="replyToComment(this)" value="${rId}" class="btn btn-dark btn-sm">Reply</button>
        <button type="button" onclick="deleteComment(this)" data-cid="${comment.c_id}" value="${comment.c_id}" class="btn btn-danger btn-sm">Delete</button>
      </div>
    </li>
  `;
  } else {
    html += `
    <li class="list-group-item" id="comment_${comment.c_id}_for_${rId}">
      <div class="container">
        <h4>${comment.title} - ${prettyPipe(comment.status)}</h4>
        <p>${comment.body}</p>
        <button type="button" onclick="replyToComment(this)" data-cid="${comment.c_id}" value="${rId}" class="btn btn-dark btn-sm">Reply</button>
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


function reimbursementHtml(reimbursement, comments, stats, user) {
  let roles = getRoles();
  let baseHtml = ``;
  const uId = readCookie('loggedInUid');

  baseHtml = baseReimbursementHtml(reimbursement, false, stats, user);
  roles.forEach(role => {
    if (reimbursement.stage === 0 && (role === `dictator` || (role === `supervisor` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true, stats, user);
    } else if (reimbursement.stage === 1 && (role === `dictator` || (role === `department_head` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true, stats, user);
    } else if (reimbursement.stage === 2 && (role === `dictator` || (role === `benefits_coordinator` && uId !== reimbursement.u_id))) {

      baseHtml = baseReimbursementHtml(reimbursement, true, stats, user);
    } else if (reimbursement.stage === 3 && (role === `benefits_coordinator` && uId !== reimbursement.u_id)) {

      baseHtml = baseReimbursementHtml(reimbursement, true, stats, user);
    }
  });



  if (comments) {
    return baseHtml + allCommentsHtml(comments, reimbursement.r_id) + '</ul></li>';
  } else {
    return baseHtml + '</ul></li>';
  }
}

function autoApprove(reimbursement) {
  sendApproval(
    prepareApproval(
      reimbursement.rId,
      reimbursement.stage,
      reimbursement.amount_requested,
      reimbursement.status
    )
  );
}

function prepareApproval(rId, stage, amountRequested, status) {
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
  return {
    'r_id': rId,
    'stage': stage,
    'amount_request': amountRequested,
    'status': status
  }
}

function sendApproval(reimbursement) {
  let paramsList = `r_id=${reimbursement.r_id}&stage=${reimbursement.stage}&status=${reimbursement.status}`;
  if (stage >= 4) {
    paramsList += `&amount_granted=${reimbursement.amount_requested}&amount_requested=0`;
  }
  let xhttp = new XMLHttpRequest();

  xhttp.open('POST', 'http://localhost:8080/project1/api/reimbursement/updateReimbursement.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      document.getElementById(`reimbursement_${reimbursement.r_id}_approve_btn`).disabled = true;
      event.setAttribute('data-stage', reimbursement.stage + 1);
      let reListItem = document.getElementById(`reimbursement_${reimbursement.r_id}`);
      if (!document.getElementById(`re_${reimbursement.r_id}_app_success`)) {
        reListItem.insertAdjacentHTML('afterend', successMessage(`Successfully approved reimbursement ${reimbursement.r_id}`, `re_${reimbursement.r_id}_app_success`));
      }
    }
  }
  xhttp.send(paramsList);
}

function approveRequest(event) {
  let rId = event.value;
  let stage = Number(event.getAttribute('data-stage')) + 1;
  let amountRequested = event.getAttribute('data-requested');
  let status = `pending_supervisor_approval`;

  sendApproval(
    prepareApproval(
      rId,
      stage,
      amountRequested,
      status
    )
  );
}

function baseReimbursementHtml(reimbursement, approve = false, stats, user) {
  if (reimbursement.stage === 4) {
    return `
      <li class="list-group-item" id="reimbursement_${reimbursement.r_id}">
        <div class="input-group">
          <button class="btn btn-danger btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_delete_btn" value="${reimbursement.r_id}" onclick="deleteRequest(this)">Delete</button>
          <button type="button" onclick="replyToComment(this)" value="${reimbursement.r_id}" class="btn btn-dark btn-sm">Reply</button>
          <button class="btn btn-light btn-sm" type="button" onclick="showAttachments(this)" data-uid="${reimbursement.u_id}" value="${reimbursement.r_id}">Show attachments</button>
        </div>
        ${basicReimbursementInformation(reimbursement, true, stats, user)}
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
            <button class="btn btn-dark btn-sm" type="button" onclick="replyToComment(this)" value="${reimbursement.r_id}" >Reply</button>
            <button class="btn btn-light btn-sm" type="button" onclick="showAttachments(this)" data-uid="${reimbursement.u_id}" value="${reimbursement.r_id}">Show attachments</button>
          </div>
          <div class="input-group-append">
            <div class="custom-file">
              <label class="custom-file-label" id="attachment_label" for="attachments_for_${reimbursement.r_id}">(+) Attachments</label>
              <input onchange="handleAddAttachment(this)" type="file" class="custom-file-input" id="attachments_for_${reimbursement.r_id}">
            </div>
          </div>
        </div>
      ${basicReimbursementInformation(reimbursement, false, stats, user)}
    `;
  }
  return `
  <li class="list-group-item" id="reimbursement_${reimbursement.r_id}">
    <div class="input-group" id="actions_for_${reimbursement.r_id}">
      <div class="input-group-prepend">
        <button class="btn btn-primary btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_edit_btn" value='${JSON.stringify(reimbursement)}' onclick="openEditForm(this)">Edit</button>
        <button class="btn btn-danger btn-sm" type="button" id="reimbursement_${reimbursement.r_id}_delete_btn" value="${reimbursement.r_id}" onclick="deleteRequest(this)">Delete</button>
        <button class="btn btn-dark btn-sm" type="button" onclick="replyToComment(this)" value="${reimbursement.r_id}" >Reply</button>
        <button class="btn btn-light btn-sm" type="button" onclick="showAttachments(this)" data-uid="${reimbursement.u_id}" value="${reimbursement.r_id}">Show attachments</button>
      </div>
      <div class="input-group-append">
        <div class="custom-file">
          <label class="custom-file-label" id="attachment_label_for_${reimbursement.r_id}" for="attachments_for_${reimbursement.r_id}">(+) Attachments</label>
          <input onchange="handleAddAttachment(this, prepareAttachment)" data-rid="${reimbursement.r_id}" type="file" class="custom-file-input" id="attachments_for_${reimbursement.r_id}">
        </div>
      </div>
    </div>
   ${basicReimbursementInformation(reimbursement, false, stats, user)}
`;
}

function basicReimbursementInformation(reimbursement, final = false, stats, user) {
  let dateHtml = '';
  let requestingHtml = '';
  if (!final) {
    dateHtml = ` and has ${timeLeftOnReimbursement(reimbursement)}`;
    requestingHtml = `<p class="text-dark">Requesting <span class="text-success">$${reimbursement.amount_requested}</span>.</p>`;
  }
  return `
    <div class="">
    <p class="text-info">${user.username} submitted this request on ${new Date(reimbursement.submission_date).toDateString()}</p>
    <p class="text-dark">Event (ID: ${reimbursement.r_id}) ${reimbursement.event_type} starts on <span class="font-weight-bold"><u>${new Date(reimbursement.event_start_date).toDateString()}</u></span>${dateHtml}</p>
    </div>
    <div class="row">
      <div class="col-md-5">
        <p class="text-white bg-info text-center">This request is at the ${prettyPipe(reimbursement.status)} stage.</p>        
        <p class="text-dark">
          <h4 class="text-dark">Description - ${reimbursement.event_description}</h4>
          <blockquote class="blockquote">${reimbursement.justification_comment}
          <footer class="blockquote-footer"><cite title="Note from the employee">Note from the employee</cite></footer>
          </blockquote>
          ${requestingHtml}
        </p>
        </div>
      <div class="col-md-4">
        <h5 class="font-weight-bold text-center">Available funds for ${prettyPipe(user.username)}</h5>
        ${stats}
      </div>
      <div class="col-md-3">
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
    ele = document.getElementById(`attachments_for_${rId}_by_${uId}`);
    ele.innerHTML = ``;
    if (attachments && attachments.length > 0) {
      attachments.forEach(attachment => {
        ele.insertAdjacentHTML('beforeend', attachmentHtml(attachment));
      });
    } else {
      ele.insertAdjacentHTML('beforeend', `
        <h4 class="text-danger">No attachments found!</h4>
      `);
    }
  })
}

function getReimbursementData(user, reimbursements, stats) {
  let container = document.getElementById(`reimbursements`);


  for (let i = 0; i < reimbursements.length; i++) {
    const reimbursement = reimbursements[i];

    getComments(user, reimbursement, container, stats);
  }
  //close the ul tag that opens in getUserSpecificData
}

function sendReply(event) {
  const rId = event.value;
  const title = document.getElementById(`comment_for_${rId}_title`).value;
  const body = document.getElementById(`comment_for_${rId}_body`).value;
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

function getCommentForm(rId) {
  return `
    <li class="list-group-control" id="comment_for_${rId}_form">
      <div class= "form-group">
        <label for="comment_for_${rId}_title">Title</label>  
        <input class="form-control" type="text" name="comment_for_${rId}_title" id="comment_for_${rId}_title" required>
        <label for="comment_for_${rId}_body">Body</label>  
        <textarea class="form-control" type="text" name="comment_for_${rId}_body" id="comment_for_${rId}_body" required></textarea>
        <button onclick="sendReply(this)" value="${rId}" type="button" class="btn btn-primary btn-sm">Submit</button>
      </div>
    </div>
  `;
}

function replyToComment(event) {
  const rId = event.value;
  let parent = document.getElementById(`comments_for_reimbursement_${rId}`);
  let commentForm = document.getElementById(`comment_for_${rId}_form`);
  if (commentForm) {
    parent.removeChild(commentForm);
  } else {
    parent.insertAdjacentHTML('beforeend', getCommentForm(event.value));
  }

}



function getComments(user, reimbursement, container, stats) {
  let rId = reimbursement.r_id;
  let params = `?r_id=${rId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/comment/getCommentsForReimbursement.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let comments = JSON.parse(this.responseText);

      appendComments(rId, reimbursement, container, stats, comments, user);
    } else if (this.readyState === XMLHttpRequest.DONE && this.status === 404) {
      appendComments(rId, reimbursement, container, stats, null, user)
    }
  }
  xhttp.send();
}

function appendComments(rId, reimbursement, container, stats, comments, user) {
  let commentsContainer = document.getElementById(`comments_for_reimbursement_${rId}`);
  if (commentsContainer) {
    commentsContainer.innerHTML = ``;
  }
  if (!document.getElementById(`user_${reimbursement.u_id}`)) {
    container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, comments, stats, user));
  } else {
    container.insertAdjacentHTML('afterbegin', reimbursementHtml(reimbursement, comments, stats, user));
  }
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