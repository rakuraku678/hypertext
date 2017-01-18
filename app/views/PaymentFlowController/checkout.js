$(document).ready(function () {
    new Fingerprint2().get(function(result){
        $("#fingerprint").val(result);
    });

    $('#checkoutForm').bootstrapValidator({
        fields: {
            validatesex: {
                selector: '.validatesex',
                validators: {
                    notEmpty: {
                        message: 'Este campo está vacio.'
                    }
                }
            },
            validatefirstname: {
                selector: '.validatefirstname',
                validators: {
                    validMessage: 'Campo correcto',
                    notEmpty: {
                        message: 'El campo Nombre esta vacio.'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z_áéíóúñ\s]*$/i,
                        message: 'Campo no válido'
                    }
                }
            },
            validatelastname: {
                selector: '.validatelastname',
                validators: {
                    validMessage: 'Campo correcto',
                    notEmpty: {
                        message: 'El campo Apellido esta vacio.'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z_áéíóúñ\s]*$/i,
                        message: 'Campo no válido'
                    }
                }
            },
            validatedocument: {
                selector: '.validatedocument',
                validators: {
                    notEmpty: {
                        message: 'Tipo de documento esta vacio.'
                    }
                }
            },
            validatedocumentnum: {
                selector: '.validatedocumentnum',
                validators: {
                    notEmpty: {
                        message: 'Este campo esta vacio.'
                    },
                    regexp: {
                        regexp: /^([0-9])*$/,
                        message: 'Campo no válido'
                    }
                }
            },
            validatedate: {
                selector: '.validatedate',
                validators: {
                    notEmpty: {
                        message: 'El campo fecha de nacimiento está vacio.'
                    },
                    date: {
                        format: 'DD/MM/YYYY',
                        message: 'día/mes/año'
                    }
                }
            },
            validateemail: {
                selector: '.validateemail',
                validators: {
                    notEmpty: {
                        message: 'El campo Email está vacio.'
                    },
                    regexp: {
                        regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
                        message: 'mail@ejemplo.com'
                    }
                }
            },
            validatephone: {
                selector: '.validatephone',
                validators: {
                    notEmpty: {
                        message: 'El campo Teléfono está vacio.'
                    },
                    regexp: {
                        regexp: /^((\d{10})|(\d{13}))$/i,
                        message: 'Campo no válido'
                    }
                }
            },

            validatetermsconditions: {
                selector: '.validatetermsconditions',
                validators: {
                    notEmpty: {
                        message: 'Debes aceptar terminos y condiciones para continuar.'
                    }
                }
            },
            validatepay: {
                selector: '.validatepay',
                validators: {
                    notEmpty: {
                        message: 'Debes seleccionar una forma de pago.'
                    }
                }
            }
        },
        submitHandler: function(validator, form, submitButton) {
            startBooking();
        }
    });
});

function startBooking() {
    $('#spinner-modal').modal('show');

    var i = 1;
    window.setInterval(
            function(){
                i=i+5;
                $('.progress-bar').css({ width : i + "%" });
            },
        200);

    var formSerializeJson = $("#checkoutForm").serializeJSON({useIntKeysAsArrayIndex: true, checkboxUncheckedValue: false});
    formSerializeJson.bfmResultItem = $.parseJSON(formSerializeJson.bfmResultItem);
    var data = JSON.stringify(formSerializeJson);
    $.ajax({
        type: "POST",
        url: "@{BookingController.startBooking}",
        data: data,
        dataType: "json",
        success:function(result) {
            console.log("startBooking Debug");
            console.log(result);
            console.log(JSON.stringify(result.debug));
            window.location=result.travelpay_location;
        },
        error: function (request, status, error) {
            //alert(request.responseText);
            window.location="@{PaymentFlowController.processError}";
        }
    });
}