function requestForm(reimbursement) {
  //if id, we are editing a req
  if (reimbursement) {
    return `
    <div class="container" id="re_${reimbursement.r_id}_edit_form">
    <div class="col-md-2"></div>
    <div class="col-md-8">
      <div class="form-group">
        <label for="event_type">Event Type</label>

        <select class="form-control" name="event_type" id="re_${reimbursement.r_id}_event_type" required>
          <option value="" selected disabled hidden>Choose an event type format</option>
          <option value="university_courses:80">University Course</option>
          <option value="seminars:60">Seminar</option>
          <option value="certification_prep:75">Certification Preparation Class</option>
          <option value="certification:100">Certification</option>
          <option value="technical_training:90">Technical Training</option>
          <option value="other:0.30">Other</option>
        </select>
      </div>

      <div class="form-group">
        <label for="event_start_date">Event Start Date</label>
        <input class="form-control" type="date" name="event_start_date" id="re_${reimbursement.r_id}_event_start_date" required>
        <label for="event_end_date">Event End Date</label>
        <input class="form-control" type="date" name="event_end_date" id="re_${reimbursement.r_id}_event_end_date" required>
        <label for="event_start_time">Event Start Time</label>
        <input class="form-control" type="time" name="event_daily_start_time" id="re_${reimbursement.r_id}_event_daily_start_time" required>
      </div>
      <div class="form-group">
        <label for="event_address">Event Address</label>
        <input class="form-control" type="text" name="event_address" id="re_${reimbursement.r_id}_event_address" required placeholder="${reimbursement.event_address}">
        <label for="event_description">Event Description (< 128 characters)</label>
        <textarea class="form-control" name="event_description" id="re_${reimbursement.r_id}_event_description" cols="50" rows="5"
          required placeholder="${reimbursement.event_description}"></textarea>
        <label for="amount_requested">Amount Requested</label>
        <input class="form-control" type="number" name="amount_requested" id="re_${reimbursement.r_id}_amount_requested" required placeholder="${reimbursement.amount_requested}">
      </div>

      <div class="form-group" id="re_${reimbursement.r_id}_event_grading">
        <label for="event_grading_format">Event Grading Format</label>
        <select class="form-control" name="event_grading_format" id="re_${reimbursement.r_id}_event_grading_format">
          <option value="" selected disabled hidden>Choose a grading format</option>
          <option value="ABCDF:C">Traditional (A: 90, B: 80, C: 70, D: 60, F: &lt;60)</option>
          <option value="PF:P">Pass / Fail</option>
          <option value="AF:A">Presentation</option>
          <option value="Custom:">Other</option>
        </select>
        <label for="event_grading_format_2">Other: Specify passing grade for this event.</label>
        <input id="re_${reimbursement.r_id}_event_grading_format_2" type="text" class="form-control" name="event_grading_format_2" placeholder="${reimbursement.event_passing_grade}">
      </div>
      <div class="form-group">
        <label for="justification_comment">Justification</label>
        <textarea class="form-control" name="justification_comment" id="re_${reimbursement.r_id}_justification_comment" cols="50" rows="5"
          required placeholder="${reimbursement.justification_comment}"></textarea>
      </div>

      <button onclick="handleConfirm(this)" data-uid="${reimbursement.u_id}" value="${reimbursement.r_id}" type="button" class="btn btn-primary">Confirm</button>

    </div>
    <div class="col-md-2"></div>
  </div>
    `;
  }
  return `
  <div class="px-4 w-100">
    <div class="form-row">
      <div class="form-group col-md-4">

        <label for="username">Full Name</label>
        <input class="form-control" type="text" name="username" id="username" required>
        
      </div>
      <div class="form-group col-md-4">
        <label for="event_address">Event Address</label>
        <input class="form-control" type="text" name="event_address" id="event_address" required>
      </div>

      <div class="form-group col-md-4">
        <label for="amount_requested">Amount Requested</label>
        <input class="form-control" onchange="adjustRequestedAmount(this)" type="number" name="amount_requested" id="amount_requested" required>
      </div>
    </div>

    <div class="form-row">
      <div class="form-group col-md-4">
        <label for="event_start_date">Event Start Date</label>
        <input class="form-control" type="date" name="event_start_date" id="event_start_date" required>
      </div>
      <div class="form-group col-md-4">
        <label for="event_end_date">Event End Date</label>
        <input class="form-control" type="date" name="event_end_date" id="event_end_date" required>
      </div>
      <div class="form-group col-md-4">
        <label for="event_start_time">Event Start Time</label>
        <input class="form-control" type="time" name="event_daily_start_time" id="event_daily_start_time" required>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-4">
        <label for="event_type">Event Type</label>

        <select class="form-control" name="event_type" id="event_type" required>
          <option value="university_courses:80">University Course</option>
          <option value="seminars:60">Seminar</option>
          <option value="certification_prep:75">Certification Preparation Class</option>
          <option value="certification:100">Certification</option>
          <option value="technical_training:90">Technical Training</option>
          <option value="other:0.30">Other</option>
        </select>
      </div>

      <div class="form-group col-md-4" id="event_grading">
        <label for="event_grading_format">Event Grading Format</label>
        <select class="form-control" name="event_grading_format" id="event_grading_format">
          <option value="" selected disabled hidden>Choose a grading format</option>
          <option value="ABCDF:C">Traditional (A: 90, B: 80, C: 70, D: 60, F: &lt;60)</option>
          <option value="PF:P">Pass / Fail</option>
          <option value="AF:A">Presentation</option>
          <option value="Custom:">Other</option>
        </select>
      </div>
      <div class="form-group col-md-4">
        <label for="event_grading_format_2">(Optional) Specify passing grade</label>
        <input id="event_grading_format_2" type="text" class="form-control" name="event_grading_format_2">
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="event_description">Event Description</label>
        <textarea class="form-control" name="event_description" id="event_description" cols="30" rows="3" required></textarea>
      </div>
      
      <div class="form-group col-md-6">
        <label for="justification_comment">Justification: Explain yourself in 256 characters.</label>
        <textarea class="form-control" name="justification_comment" id="justification_comment" cols="30" rows="3" required></textarea>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-12">
        <div class="input-group mb-3">
          <div class="input-group-prepend">
            <span class="input-group-text">Upload</span>
          </div>
          <div class="custom-file">
            <input onchange="handleAddAttachment(this)" type="file" class="custom-file-input" id="attachments">
            <label class="custom-file-label" id="attachment_label" for="attachments">Add attachments (such as a transcript, email or signed paperwork)</label>
          </div>
        </div>
      <button onclick="handleSumbit()" type="button" class="btn btn-primary">Submit Request</button>
    </div>

  </div>
  `;
}