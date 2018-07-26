function validateform() {

    var name = document.myform.name.value;
    var namePattern = new RegExp("^[a-zA-Z ]+$");

    var lastName = document.myform.lastName.value;
    var lastNamePattern = new RegExp("^[a-zA-Z ]+$");

    var phone = document.myform.phone.value;
    var phonePattern = new RegExp("^[0-9\-\+]{9,15}$");


    var email = document.myform.email.value;
    var emailPattern = new RegExp("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$");

    var mentorClass = document.myform.class.value;
    var mentorClassPattern = new RegExp("^\\d{4}(.)\\d{2}$");


    if (!namePattern.test(name)) {
        console.log("wrong name");
        document.getElementById("err-name").innerHTML="Enter valid first name";
        return false;
    }

    else if(!lastNamePattern.test(lastName)) {
        console.log("wrong last name");
        document.getElementById("err-lastname").innerHTML="Enter valid last name";
        return false;
    }

    else if(!phonePattern.test(phone)) {
        console.log("wrong phone");
        document.getElementById("err-phone").innerHTML="Enter valid phone number";
        return false;

    }

    else if (!emailPattern.test(email)) {
        console.log("wrong mail");
        document.getElementById("err-email").innerHTML="Enter valid emial";
        return false;
    }

    else if (!mentorClassPattern.test(mentorClass)) {
        console.log("wrong class");
        document.getElementById("err-class").innerHTML="Enter valid class";
        return false;
    }
    else {

        console.log("VALID INPUT");
        alert("SUBMITED");
        return true;
    }
}



function validateformLevel() {
    var name = document.myform.name.value;
    var namePattern = new RegExp("\\d");

    var experience = document.myform.experience.value;
    var experiencePattern = new RegExp("\\d");

    if (!namePattern.test(name)) {
        console.log("wrong name");
        document.getElementById("err-level-name").innerHTML="Enter valid first name";
        return false;
    }

    else if(!experiencePattern.test(experience)) {
        console.log("wrong exp");
        document.getElementById("err-level-exp").innerHTML="Enter valid experience";
        return false;
    }

     else {

        console.log("VALID INPUT");
        alert("SUBMITED");
        return true;
    }
}