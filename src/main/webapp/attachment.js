function postAttachment(attachment, callback) {
  let xhttp = new XMLHttpRequest();
  let body = `r_id=${attachment.r_id}&title=${attachment.title}&type=${attachment.type}&src=${attachment.src}&u_id=${attachment.u_id}`;

  xhttp.open('POST', 'http://localhost:8080/project1/api/attachment/createAttachment.do');

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      if(callback) {
        callback();
      } else {
        window.location.href = "http://localhost:8080/project1/dashboard.html";
      }
    }
  }
  xhttp.send(body);
}

function getAttachmentsByUserForReimbursement(uId, rId, callback) {
  let xhttp = new XMLHttpRequest();
  let params = `u_id=${uId}&r_id=${rId}`;

  xhttp.open('GET', `http://localhost:8080/project1/api/attachment/getAttachmentsByUserForReimbursement.do?${params}`);

  xhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

  xhttp.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      if(callback) {
        callback(JSON.parse(this.response));
      }
    }
  }
  xhttp.send();
}

function getAttachments(container = 'attachments') {
  let attachmentElement = document.getElementById(container);
  if (attachmentElement) {
    attachmentElement = attachmentElement.files[0];
    return attachmentElement;
  }
  return null;
}

function handleAddAttachment(event, callback) {
  let container = 'attachment_label';
  const rId = event.getAttribute('data-rid');
  if(rId) {
    container = `attachment_label_for_${rId}`;
  }
  let label = document.getElementById(container);
  event = event.files[0];
  label.innerText = event.name;
  if(callback && rId) {    
    callback(event, rId);
  }
}

function prepareAttachment(event, rId) {
  const uId = readCookie('loggedInUid');
  attachment = {
    'r_id': rId,
    'title': event.name,
    'src': event.name,
    'type': event.type,
    'u_id': uId
  };
  
  postAttachment(attachment, () => {
    let ele = document.getElementById(`actions_for_${rId}`);
    ele.insertAdjacentHTML('afterend', `
      <span class="text-success">Successfully added ${attachment.title} to this reimbursement(ID: ${rId})!</span>
    `);
    ele = document.getElementById(`attachments_for_${reimbursement.rId}_by_${uId}`);
    ele.insertAdjacentHTML('beforeend', attachmentHtml(attachment));
  });
}

function attachmentHtml(attachment) {
  return `
    <button type="button" id="attachment_${attachment.a_id}" class="list-group-item list-group-item-action" disabled>${attachment.title} - type: ${attachment.type}</button>
  `;
}