$(document).ready(function () {
    new Fingerprint2().get(function(result){
        $("#fingerprint").val(result);
    });
    $("#checkoutForm").submit(function (event) {
        event.preventDefault();
        startBooking();
    });


});

function startBooking() {
    var data = JSON.stringify($("#checkoutForm").serializeJSON());
    $.ajax({
        type: "POST",
        url: "@{BookingController.startBooking}",
        data: data,
        dataType: "json"
    });
}