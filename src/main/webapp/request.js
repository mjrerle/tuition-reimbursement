function findOption(selectId) {
  let select = document.getElementById(selectId);
  if (select) {
    let selectedOption = null;
    for (let i = 0; i < select.length; i++) {
      let option = select.options[i];
      if (option.selected) {
        selectedOption = option.value;
      }
    }
    if (selectedOption) {
      return selectedOption.split(':');
    }
  }
  return [];
}

function isElementValueValid(element) {
  return (element.value !== '' || element.value.length !== 0) && typeof (element.value) !== 'undefined';
}

function populateUserBox() {
  let userBox = document.getElementById('userBox');
  const username = prettyPipe(readCookie('loggedInUsername'));
  let roles = getRoles();
  let role = prettyPipe(roles[roles.length - 1]);
  userBox.insertAdjacentHTML('afterbegin', `
    Hello, ${username}. <small class="text-white">What kind of <abbr title="Employee Role At INITECH, Premier Bank Software INC">${role}</abbr> stuff are you doing today?</small>
  `);
}

function insertForm(container) {
  let body = document.getElementById(container);
  populateUserBox();
  body.insertAdjacentHTML('beforeend', requestForm());
}

function inputEntered(element, message, validated, valueIsValid = true) {
  if(!element) {
    return false;
  }
  let errName = element.id + '_err';
  let err = document.getElementById(errName);
  if (!isElementValueValid(element) || !valueIsValid) {
    if (!err && element.parentElement) {
      element.parentElement.insertAdjacentHTML('afterend', badInput(message,
        errName));
      //if the message is not the same, change it
    } else if (err && (err.innerText !== message)) {
      err.innerHTML = message;
    }
    return false;
  } else if (err) {
    err.parentNode.removeChild(err);
  }
  return validated;
}

function badInput(warning, id) {
  return `
    <div id="${id}" class="alert alert-danger" role="alert">
      ${warning}
    </div>
  `;
}

function getDetails(id) {
  
  if (id !== null && id !== undefined) {

    let eventStartDate = document.getElementById(`re_${id}_event_start_date`);
    let eventEndDate = document.getElementById(`re_${id}_event_end_date`);
    let eventDailyStartTime = document.getElementById(`re_${id}_event_daily_start_time`);
    let eventAddress = document.getElementById(`re_${id}_event_address`);
    let eventDescription = document.getElementById(`re_${id}_event_description`);
    let amountRequested = document.getElementById(`re_${id}_amount_requested`);
    let eventPassingGrade;
    if (document.getElementById(`re_${id}_event_grading_format_2`) && document.getElementById(`re_${id}_event_grading_format_2`).value) {
      eventPassingGrade = document.getElementById(`re_${id}_event_grading_format_2`);
    } else {
      let selectedEventGradingFormatOption = findOption(`re_${id}_event_grading_format`);
      if (selectedEventGradingFormatOption) {
        eventPassingGrade = selectedEventGradingFormatOption[1];
      } else {
        eventPassingGrade = {
          id: `re_${id}_event_grading_format`,
          value: ''
        };
      }
    }
    let justificationComment = document.getElementById(`re_${id}_justification_comment`);
    return {
      eventStartDate,
      eventEndDate,
      eventDailyStartTime,
      eventAddress,
      eventDescription,
      amountRequested,
      eventPassingGrade,
      justificationComment
    };
  }
  let eventStartDate = document.getElementById('event_start_date');
  let eventEndDate = document.getElementById('event_end_date');
  let eventDailyStartTime = document.getElementById('event_daily_start_time');
  let eventAddress = document.getElementById('event_address');
  let eventDescription = document.getElementById('event_description');
  let amountRequested = document.getElementById('amount_requested');
  let eventPassingGrade;
  //trying to find the passing grade(can be in the select list as well as the input box below it)
  if (document.getElementById('event_grading_format_2') && document.getElementById('event_grading_format_2').value) {
    eventPassingGrade = document.getElementById('event_grading_format_2');
  } else {
    let selectedEventGradingFormatOption = findOption('event_grading_format');
    if (selectedEventGradingFormatOption) {
      eventPassingGrade = {
        id: 'event_grading_format',
        value: selectedEventGradingFormatOption[1]
      }
    } else {
      eventPassingGrade = {
        id: 'event_grading_format',
        value: ''
      };
    }
  }
  console.log(eventPassingGrade);
  
  let justificationComment = document.getElementById('justification_comment');
  return {
    eventStartDate,
    eventEndDate,
    eventDailyStartTime,
    eventAddress,
    eventDescription,
    amountRequested,
    eventPassingGrade,
    justificationComment
  };
}

function validateInput() {
  let validated = true;
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
  validated = inputEntered(document.getElementById('username'), 'Please enter your full name.', validated);
  validated = validateDate(eventStartDate, validated);
  validated = inputEntered(eventEndDate, 'Please enter a valid end date.', validated);
  validated = inputEntered(eventDailyStartTime, 'Please enter a valid daily start time.', validated);
  validated = inputEntered(eventAddress, 'Please enter an event address.', validated);
  validated = inputEntered(eventDescription, 'Please enter a valid event description.', validated);
  validated = inputEntered(amountRequested, 'Please enter a valid amount to request.', validated);
  validated = inputEntered(eventPassingGrade, 'Please enter a valid passing grade.', validated);
  validated = inputEntered(justificationComment, 'Please enter a valid justification.', validated);

  return validated;
}

function validateDate(dateElement, validated) {
  let now = Date.now();
  let onTime = true;
  let message = 'Please enter a valid start date.';
  // if true, then there is input
  if (isElementValueValid(dateElement)) {
    let then = new Date(dateElement.value).getTime();
    //if the request is less than a week old, cannot submit it
    if (Math.floor((then - now) / (1000 * 60 * 60 * 24)) <= 7) {
      message += ' Requests must be submitted at least a week before the start date.'
      return inputEntered(dateElement, message, validated, onTime = false);
    }
  }
  //preserve the old value of validated (in case it was false)
  return inputEntered(dateElement, message, validated, onTime = true);

}