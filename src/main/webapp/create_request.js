
//run on page load
function init() {
  let uId = readCookie('loggedInUid');

  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/role/getRolesForUser.do?u_id=' + uId);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let roles = JSON.parse(this.responseText);
      let i = 0;
      //used to figure out approval permissions
      roles.forEach(role => {
        setCookie(`userRole_${i++}`, role.name);
      });
      insertForm('create-request-page');
    }
  }
  xhttp.send();
}


function handleSumbit() {
  let username = readCookie('loggedInUsername');
  let params = `?username=${username}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/user/getUser.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let user = JSON.parse(this.responseText);
      getLastIdInserted(user);
    }
  }
  xhttp.send();
}

function getLastIdInserted(user) {
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/reimbursement/getRidOfLastInserted.do');

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let rId = JSON.parse(this.responseText);
      submitRequestWithUser(user, rId);
    }
  }
  xhttp.send();
}

function submitRequestWithUser(user, rId) {

  let reimbursement, attachment;
  const obj = createReimbursementObject(user.u_id, rId);

  if (!obj) {
    return;
  } else {
    reimbursement = obj.reimbursement,
      attachment = obj.attachment
  }
  let body =
    `event_type=${reimbursement.event_type}&percent_coverage=${reimbursement.percent_coverage}&status=${reimbursement.status}&submission_date=${reimbursement.submission_date}&event_start_date=${reimbursement.event_start_date}&event_end_date=${reimbursement.event_end_date}&event_daily_start_time=${reimbursement.event_daily_start_time}&event_address=${reimbursement.event_address}&event_description=${reimbursement.event_description}&amount_requested=${reimbursement.amount_requested}&amount_granted=${reimbursement.amount_granted}&event_grading_format=${reimbursement.event_grading_format}&event_passing_grade=${reimbursement.event_passing_grade}&justification_comment=${reimbursement.justification_comment}&stage=${reimbursement.stage}&u_id=${reimbursement.u_id}`;

  let xhttp = new XMLHttpRequest();

  xhttp.open('POST', 'http://localhost:8080/project1/api/reimbursement/createReimbursement.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      if (attachment) {
        postAttachment(attachment);
      } else {
        window.location.href = "http://localhost:8080/project1/dashboard.html";
      }
    }
  }
  xhttp.send(body);
}





function createReimbursementObject(uId, rId) {
  if (!validateInput()) {
    return null;
  }

  let {
    eventStartDate,
    eventEndDate,
    eventDailyStartTime,
    eventAddress,
    eventDescription,
    amountRequested,
    eventPassingGrade,
    justificationComment
  } = getDetails();
  let selectedEventTypeOption = findOption('event_type');
  let eventType = selectedEventTypeOption[0];
  let percentCoverage = selectedEventTypeOption[1];
  let status = 'pending_supervisor_approval';
  let submissionDate = Date.now();
  eventStartDate = new Date(eventStartDate.value).getTime();
  eventEndDate = new Date(eventEndDate.value).getTime();
  eventDailyStartTime = eventDailyStartTime.value;
  eventAddress = eventAddress.value;
  eventDescription = eventDescription.value;
  amountRequested = amountRequested.value;
  let amountGranted = 0;
  let eventGradingFormat;
  eventPassingGrade = eventPassingGrade.value;
  if (document.getElementById('event_grading_format_2').value) {
    eventGradingFormat = 'Custom';
  } else {
    let selectedEventGradingFormatOption = findOption('event_grading_format');
    eventGradingFormat = selectedEventGradingFormatOption[0];
  }

  justificationComment = justificationComment.value;
  let stage = 0;

  let reimbursement = {
    'event_type': eventType,
    'percent_coverage': percentCoverage,
    'status': status,
    'submission_date': submissionDate,
    'event_start_date': eventStartDate,
    'event_end_date': eventEndDate,
    'event_daily_start_time': eventDailyStartTime,
    'event_address': eventAddress,
    'event_description': eventDescription,
    'amount_requested': amountRequested,
    'amount_granted': amountGranted,
    'event_grading_format': eventGradingFormat,
    'event_passing_grade': eventPassingGrade,
    'justification_comment': justificationComment,
    'stage': stage,
    'u_id': uId
  };

  console.log(reimbursement);

  let attachment = null;
  if (attachment = getAttachments()) {
    attachment = {
      'title': attachment.name,
      'r_id': rId + 1,
      'type': attachment.type,
      'u_id': uId,
      'src': attachment.name
    };
  }
  return {
    reimbursement,
    attachment
  };
}