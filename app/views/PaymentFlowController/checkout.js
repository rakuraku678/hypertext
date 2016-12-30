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
    var formSerializeJson = $("#checkoutForm").serializeJSON();
    formSerializeJson.bfmResultItem = $.parseJSON(formSerializeJson.bfmResultItem);
    var data = JSON.stringify(formSerializeJson);
    console.log(data);
    $.ajax({
        type: "POST",
        url: "@{BookingController.startBooking}",
        data: data,
        dataType: "json"
    });
}