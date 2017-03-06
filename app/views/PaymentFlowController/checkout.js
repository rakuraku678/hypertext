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
                        regexp: /^[0-9kK]*$/,
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
                        regexp: /^((\d{9})|(\d{13}))$/i,
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
    
    $( ".validatedocumentnum" ).change(function( index ) {
        if (!checkRut(this)){
        	$(this).next().next().css("display","block");
        	$(this).next().next().css("color","#a94442");
        	$(this).css("border-color","#a94442 !important");
        	$("#btnContinue").attr("disabled","disabled");
        }
        else {
        	$("#btnContinue").removeAttr("disabled");
        }
		 
    });
    
    
});

function startBooking() {
	if (!checkRut($( ".validatedocumentnum" )[0])){
    	$(this).next().next().css("display","block");
    	$(this).next().next().css("color","#a94442");
    	$(this).css("border-color","#a94442 !important");
    	return false;
    }
    else {
    	$("#btnContinue").removeAttr("disabled");
    }
	
	
    $('#spinner-modal').modal({
        backdrop: 'static',
        keyboard: false,
        options: 'show'
    });

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
            if(result.status){
                window.location=result.data.location;
            } else {
                var error_location ="@{PaymentFlowController.processError(type: '_error_id_')}";
                window.location=error_location.replace("_error_id_", result.error);
            }
        },
        error: function (request, status, error) {
            window.location="@{PaymentFlowController.processError(type: 'internal')}";
        }
    });
}


function checkRut(rut) {
    // Despejar Puntos
    var valor = rut.value.replace('.','');
    // Despejar Guión
    valor = valor.replace('-','');
    
    // Aislar Cuerpo y Dígito Verificador
    cuerpo = valor.slice(0,-1);
    dv = valor.slice(-1).toUpperCase();
    
    // Formatear RUN
    rut.value = cuerpo + '-'+ dv
    
    // Si no cumple con el mínimo ej. (n.nnn.nnn)
    if(cuerpo.length < 7) {  rut.value = valor; return false;}
    
    // Calcular Dígito Verificador
    suma = 0;
    multiplo = 2;
    
    // Para cada dígito del Cuerpo
    for(i=1;i<=cuerpo.length;i++) {
    
        // Obtener su Producto con el Múltiplo Correspondiente
        index = multiplo * valor.charAt(cuerpo.length - i);
        
        // Sumar al Contador General
        suma = suma + index;
        
        // Consolidar Múltiplo dentro del rango [2,7]
        if(multiplo < 7) { multiplo = multiplo + 1; } else { multiplo = 2; }
  
    }
    
    // Calcular Dígito Verificador en base al Módulo 11
    dvEsperado = 11 - (suma % 11);
    
    // Casos Especiales (0 y K)
    dv = (dv == 'K')?10:dv;
    dv = (dv == 0)?11:dv;
    
    // Validar que el Cuerpo coincide con su Dígito Verificador
    if(dvEsperado != dv) {  rut.value = valor; return false; }
    
    // Si todo sale bien, eliminar errores (decretar que es válido)
    rut.value = valor;
    return true;
}