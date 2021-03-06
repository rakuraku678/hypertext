$(document).ready(function () {
    

    $('.validatedate').datepicker(
        {
            dateFormat: 'dd/mm/yy',
            changeMonth: true,
            changeYear: true,
            yearRange: "-100:+0",
            maxDate: '-1D'
        });
    
    $('.validatedatePas').datepicker(
            {
                dateFormat: 'dd/mm/yy',
                changeMonth: true,
                changeYear: true,
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
                    stringLength : {
                        min: 2,
                        message: "Minimo 2 caracteres"
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
                    stringLength : {
                        min: 2,
                        message: "Minimo 2 caracteres"
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
            validatepassport: {
                selector: '.validatepassport',
                validators: {
                    regexp: {
                    	regexp: /^[a-zA-Z0-9]*$/i,
                        message: 'Campo no válido'
                    },
                    stringLength : {
                    	max: 25,
                    	message: "Máximo 25 caracteres"
                    },
                    notEmpty: {
                        message: 'El campo esta vacio.'
                    }
                }
            },
            validatedocumentnum: {
                selector: '.validatedocumentnum',
                validators: {
                    notEmpty: {
                        message: ' '
                    },
                    regexp: {
                        regexp: /^[0-9kK]*$/,
                        message: ' '
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
            validateCountryCombo: {
            	selector: '.validateCountryCombo',
            	validators: {
                    notEmpty: {
                        message: 'Debe seleccionar un País.'
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

    $( ".validatedate" ).change(function() {
    	checkFNacDates(this);
    });
    $( ".validatedatePas" ).change(function() {
    	checkFPassDates(this);
    });
    
    $( ".validatedate" ).keyup(function() {
    	checkFNacDates(this);
    });
    $( ".validatedatePas" ).keyup(function() {
    	checkFPassDates(this);
    });
    
 
    $( ".validatedocumentnum" ).change(function( index ) {
        if (!checkRut(this)){
        	$(this).css("border-color","#a94442 !important");
        	//$("#btnContinue").attr("disabled","disabled");
        }
        else {
        	$("#btnContinue").removeAttr("disabled");
        }
		 
    });
    
	$(".validatedocument").change(function( event ) {
		var pNum = $(this).data("pnum");
		if ($(this).val() == "PAS") {
			$("#pasaporteBox"+pNum).show();
			$("#pasNum"+pNum).show();
			$("#rutNum"+pNum).hide();
			$("#rutNum"+pNum).siblings("small").hide();
		}
		else {
			$("#pasaporteBox"+pNum).hide();
			$("#rutNum"+pNum).show();
			$("#pasNum"+pNum).hide();
		}
	});
	
	$("#btnContinue").click(function( event ) {
		checkAllDates();
	})
    
	$(".validateCountryCombo").change(function( index ) {
		if ($(this).val()!=''){
			$(this).css("border-color","#3c763d");
		}
	});
});

function checkFNacDates(obj) {
	var ok = true;
	var re = new RegExp(/^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/);
	var pnum = $(obj).data("pnum");
	
	if ($(obj).val().trim()==''){
		$(obj).parent().addClass("has-error");
		$("#emptyfnac"+pnum).show();
		ok=false;
	}
	else if (!re.test($( ".validatedate" ).val())){
		$(obj).parent().addClass("has-error");
		$("#invfnac"+pnum).show();
		ok=false;
	}
	else {
		$(obj).parent().removeClass("has-error");
		$("#invfnac"+pnum).hide();
		$("#emptyfnac"+pnum).hide();
	}
	return ok;
}

function checkFPassDates(obj) {
	var ok = true;
	var re = new RegExp(/^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/);
	var pnum = $(obj).data("pnum");

	if ($( "#pasaporteBox"+pnum ).css("display")=="block" && $( obj ).val().trim()==''){
		$( obj ).parent().addClass("has-error")
		$("#emptyfpas"+pnum).show();
		$(obj).parent().parent().removeClass("has-success");
		$("#emptyfpas"+pnum).css("color","#a94442 !important")
		ok=false;
	}
	else if ($( "#pasaporteBox"+pnum ).css("display")=="block" && !re.test($(obj).val()) ){
		$( obj ).parent().addClass("has-error");
		$("#invfpas"+pnum).show();
		$("#invfpas"+pnum).css("color","#a94442 !important")
		$(obj).parent().parent().removeClass("has-success");
		ok=false;
	}
	else {
		$(obj ).parent().removeClass("has-error");
		$("#invfpas"+pnum).hide();
		$("#emptyfpas"+pnum).hide();
	}
	
	return ok;
}



function checkAllDates() {
	var ok = true;
	$( ".validatedate" ).each(function( index ) {
		if (!checkFNacDates(this)){
			ok = false;
		}
	});
	$( ".validatedatePas" ).each(function( index ) {
		if (!checkFPassDates(this)) {
			ok = false;
		}
	});
	return ok;
}


function startBooking() {
	var error = false;

	$( ".validatedocumentnum" ).each(function( index ) {
		if (!checkRut(this) && $("#selectDoc").val()=="RUT"){
	    	$(this).css("border-color","#a94442 !important");
	    	return false;
	    }
	    else {
	    	$("#btnContinue").removeAttr("disabled");
	    }
	});

	if (!checkAllDates()){
		error = true;
	}
	
	if (error) {
		var errors = $('.has-error')
        $('html, body').animate({ scrollTop: errors.offset().top - 50 }, 500);
		return false;
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
    formSerializeJson.origin = window.location.origin;
    if (typeof finalSelectionArr !== 'undefined' && finalSelectionArr.length>0){
    	formSerializeJson.seatsSelected =  finalSelectionArr.join();
    }
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
    var cuerpo = valor.slice(0,-1);
    var dv = valor.slice(-1).toUpperCase();
    
    // Formatear RUN
    rut.value = cuerpo + '-'+ dv
    
    // Si no cumple con el mínimo ej. (n.nnn.nnn)
    if(cuerpo.length < 7) {  rut.value = valor; return false;}
    
    // Calcular Dígito Verificador
    var suma = 0;
    var multiplo = 2;
    
    // Para cada dígito del Cuerpo
    for(var i=1;i<=cuerpo.length;i++) {
    
        // Obtener su Producto con el Múltiplo Correspondiente
        var index = multiplo * valor.charAt(cuerpo.length - i);
        
        // Sumar al Contador General
        suma = suma + index;
        
        // Consolidar Múltiplo dentro del rango [2,7]
        if(multiplo < 7) { multiplo = multiplo + 1; } else { multiplo = 2; }
  
    }
    
    // Calcular Dígito Verificador en base al Módulo 11
    var dvEsperado = 11 - (suma % 11);
    
    // Casos Especiales (0 y K)
    dv = (dv == 'K')?10:dv;
    dv = (dv == 0)?11:dv;
    
    // Validar que el Cuerpo coincide con su Dígito Verificador
    if(dvEsperado != dv) {  rut.value = valor; return false; }
    
    // Si todo sale bien, eliminar errores (decretar que es válido)
    rut.value = valor;
    return true;
}