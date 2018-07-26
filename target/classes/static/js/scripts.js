
const loginButton = document.getElementById("login-button");
loginButton.disabled = true;

function validate() {
    const login = document.getElementById("login").value;
    const pass = document.getElementById("pass").value;
    const loginREGEX = /^[\w@\.-][^<>{}\[\]"~;$^%?#&]{1,20}$/;
    const passREGEX = /^([\w^<>{}\[\]"~;$^%?#&]{4,20})$/;


    if (!(login).match(loginREGEX)) {
        document.getElementById("err-login-fn").innerHTML="Enter valid login";
        return false;
    }
    else if (!(pass).match(passREGEX)) {
        document.getElementById("err-pass-fn").innerHTML="Enter valid password (at least 4 symbols)";
        return false;
    } else {
        loginButton.disabled = false;
        document.getElementById("err-login-fn").innerHTML="";
        document.getElementById("err-pass-fn").innerHTML="";
        return true;
    }
}

function validateUser() {
    const login = document.getElementById("login").value;
    const pass = document.getElementById("pass").value;
    const mentorLogin = "mentor";
    const mentorPass = "12345";
    const adminLogin = "admin";
    const adminPass = "12345";
    const studentLogin = "student";
    const studentPass = "12345";
    if (!(login).match(mentorLogin) && !(login).match(adminLogin) && !(login).match(studentLogin)) {
        document.getElementById("err-login-fn").innerHTML="Can't match your name";
    } else if (!(pass).match(mentorPass) || !(pass).match(adminPass) || !(pass).match(studentPass)) {
        document.getElementById("err-pass-fn").innerHTML="Can't match your password";
    } else if ((login).match(mentorLogin) && (pass).match(mentorPass)) {
        window.location.replace("mentor-top.html");
    } else if ((login).match(adminLogin) && (pass).match(adminPass)) {
        window.location.replace("admin/mentor.html");
    } else if ((login).match(studentLogin) && (pass).match(studentPass)) {
        window.location.replace("Student/codecooler.html");
    }
    return false;
}

