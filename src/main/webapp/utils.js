function include(filename) {
  var head = document.getElementsByTagName('head')[0];

  var script = document.createElement('script');
  script.src = filename;
  script.type = 'text/javascript';

  head.appendChild(script)
}

String.prototype.replaceAll = function (search, replacement) {
  var target = this;
  return target.replace(new RegExp(search, 'g'), replacement);
};

String.prototype.capitalize = function () {
  return this.replace(/(?:^|\s)\S/g, function (a) {
    return a.toUpperCase();
  });
};

function readCookie(cookieName, all = 1) {
  let re = new RegExp('[; ]' + cookieName + '=([^\\s;]*)');
  let sMatch = (' ' + document.cookie).match(re);
  if (cookieName && sMatch) return unescape(sMatch[all]);
  return '';
}

function isAuthenticated(callback1, callback2) {
  if (!readCookie('loggedIn')) {
    window.location.replace('http://localhost:8080/project1/login.html');
  }

  if (callback1) {
    callback1();
  }
  if (callback2) {
    callback2();
  }
}

function eraseCookie(name) {
  setCookie(name, '', 1);
}

function logout() {
  eraseCookie('loggedIn');
  eraseCookie('loggedInUsername');
  eraseCookie('loggedInUid');
  roles = getRoles(true);
  if (roles) {
    roles.forEach(role => {
      eraseCookie(role.name);
    });
  }
  window.location.replace('http://localhost:8080/project1/login.html');
}


//cookie will expire in 300 seconds
function setCookie(cname, cvalue, time = 300000) {
  var d = new Date();
  d.setTime(d.getTime() + (time));
  var expires = "expires=" + d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function prettyPipe(input) {
  return input.replaceAll('_', ' ').capitalize();
}

function getRoles(whole = false) {
  let rawRoles = readCookie(`userRole_.*`, 0);
  if (rawRoles) {
    rawRoles = rawRoles.split(';');
  } else {
    //no roles (cookie probably expired or was deleted)
    return null;
  }
  let roles = [];
  rawRoles.forEach(role => {
    if (whole) {
      roles.push({
        name: role.split(`=`)[0].trim(),
        value: role.split(`=`)[1].trim()
      });
    } else {
      roles.push(role.split(`=`)[1].trim());
    }
  });
  return roles;
}

include('./request_template.js');
include('./request.js');