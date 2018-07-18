function calculetePrice() {
    var quantityStudents = document.getElementById('qS').value;
    var price = 4;
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
    document.getElementById('topay').innerHTML = calculetePrice();
}

function goBack() {
    alert("Congratulations! You bought artifact");
    window.history.back();
}

function calculetePrice2() {
    var quantityStudents = document.getElementById('qS2').value;
    var price = 1;
    var buyButton = document.getElementById('buy2');

    if (price % quantityStudents != 0 ) {
        buyButton.setAttribute("disabled", "true")
        return price;
    } else
        buyButton.removeAttribute("disabled")
        return (price / quantityStudents);
}

function refresh2() {
    document.getElementById('topay2').innerHTML = calculetePrice2();
}