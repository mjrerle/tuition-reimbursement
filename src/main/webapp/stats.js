function getStats() {
  const uId = readCookie('loggedInUid');
  //make api call
  let params = `?u_id=${uId}`;
  let xhttp = new XMLHttpRequest();

  xhttp.open('GET', 'http://localhost:8080/project1/api/reimbursement/getReimbursementsByUser.do' + params);

  // xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhttp.onreadystatechange = function () {
    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
      let reimbursements = JSON.parse(this.responseText);
      populateStatsContainer(reimbursements, 'stats_container');
    } else if (this.readyState == XMLHttpRequest.DONE && this.status === 404) {
      let reimbursements = [{
        amount_requested: 0,
        amount_granted: 0
      }];
      populateStatsContainer(reimbursements, 'stats_container');
      if (!document.getElementById(`err_no_re`) && document.getElementById(`reimbursements`)) {
        if(document.getElementById(`reimbursements`).length < 1) {
          document.getElementById(`reimbursements`).parentElement.insertAdjacentHTML('afterend', `<span id="err_no_re" class="text-danger">No reimbursements found!</span>`);
        }
      }
    }
  }
  xhttp.send();
}

function populateStatsContainer(reimbursements, container, location = 'beforeend') {
  let stats = parseReimbursementData(reimbursements);
  let successClass = 'success';
  let availableHtml = `$${stats.Available}`;
  if(stats.Available < 0) {
    successClass = 'danger';
    availableHtml = `-$${-1 * stats.Available}`
  }
  let html = `
  <table class="table">
    <caption class="text-white">My available tuition reimbursement funds</caption>
    <thead class="thead-dark">
      <tr>
        <th scope="col">Available</th>
        <th scope="col">Total</th>
        <th scope="col">Pending</th>
        <th scope="col">Awarded</th>
      </tr>
    </thead>
    <tbody class="bg-light">
      <tr>
        <td class="text-${successClass}">${availableHtml}</td>
        <td>$${stats.Total}</td>
        <td>$${stats.Pending}</td>
        <td>$${stats.Awarded}</td>
      </tr>
    </tbody>
  </table>`;

  if (container) {
    container = document.getElementById(container);
    container.insertAdjacentHTML(location, html);
  } else {
    return html;
  }
}

function parseReimbursementData(reimbursements) {
  const total = 1000;
  let available = 0;
  let pending = 0;
  let awarded = 0;

  //loop through reimbursements
  reimbursements.forEach(reimbursement => {
    pending += reimbursement.amount_requested;
    awarded += reimbursement.amount_granted;
  });

  available = total - pending - awarded
  return {
    'Available': available,
    'Awarded': awarded,
    'Pending': pending,
    'Total': total
  };
}