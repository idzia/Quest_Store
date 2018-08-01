
const submitButton = document.getElementById("submit-button");
const modal = document.getElementById("modalWindow");
const span = document.getElementsByClassName("close")[0];

submitButton.onclick = function () {console.log("aaaaaaaaaaaaaaaaa")};
submitButton.onclick = function () {addModal()};
span.onclick = function () {removeModal()};

function addModal() {
    modal.classList.add("show-modal");
;
}

function removeModal() {
    modal.classList.remove("show-modal");
}

 function submitData() {
     var email = document.getElementById('email').value;
     var correctemail = /^[0-9a-zA-Z_.-]+@[0-9a-zA-Z.-]+\.[a-zA-Z]{2,3}$/;

     if (correctemail.test(email)) {
        return true;
     } else
        return false;
 }

// submitButton.addEventListener("onclick", function(event) {
//     event.preventDefault();
//     submitData()
// });