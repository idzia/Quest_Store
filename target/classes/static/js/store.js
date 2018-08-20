function calculatePrice() {
    var quantityStudents = document.getElementById('qS').value;
    var price = document.getElementById('price').value;
    var buyButton = document.getElementById('buy');
    document.getElementsByClassName("alert_button")[0].innerHTML = "";

    if (price % quantityStudents != 0 ) {
        buyButton.setAttribute("disabled", "true")
        document.getElementsByClassName("alert_button")[0].innerHTML = "*Not possible";
        return price;
    } else
        buyButton.removeAttribute("disabled")
        return (price / quantityStudents);
}

function refresh() {
    document.getElementById('topay').innerHTML = calculatePrice();
}

function goBack() {
    alert("Congratulations! You bought artifact");
    window.history.back();
}


function refresh2() {
    var price = document.getElementById('price').value;
    document.getElementById('topay').innerHTML = price;
}
