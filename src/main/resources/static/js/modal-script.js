
const submitButton = document.getElementById("submit-button");
const modal = document.getElementById("modalWindow");
const span = document.getElementsByClassName("close")[0];

submitButton.onclick = function () {console.log("aaaaaaaaaaaaaaaaa")};
submitButton.onclick = function () {addModal()};
span.onclick = function () {removeModal()};

function addModal() {
    modal.classList.add("show-modal");
    console.log("aaaaaaaaaaaaaaaaa");
}

function removeModal() {
    modal.classList.remove("show-modal");
}

// function submitData() {
//     console.log('user hit send button');
//     addModal()
// }

// submitButton.addEventListener("onclick", function(event) {
//     event.preventDefault();
//     submitData()
// });