    $('.tickets-arrives').on('change', 'input[name^=resultSegmentIda]', function() {
        if($(this).is(':checked'))
        {
          var formId = $(this).parents('form').attr('id');
          $('#' + formId + ' input[name^=resultSegmentIda]').parent().parent().removeClass('shadow-checked');
          $(this).parent().parent().addClass('shadow-checked');
        }
        else
        {
          $(this).parent().parent().removeClass('shadow-checked');
        }
    });
/*
    $('.tickets-arrives').on('click', 'input[name^=resultSegmentIda]', function() {
        if($(this).is(':checked') && $(this).parent().parent().hasClass('shadow-checked'))
        {
          $(this).parent().parent().removeClass('shadow-checked');
          $(this).prop('checked', false);
        }
    });
*/
    $('.tickets-arrives').on('change', 'input[name^=resultSegmentVuelta]', function() {
        if($(this).is(':checked'))
        {
          var formId = $(this).parents('form').attr('id');
          $('#' + formId + ' input[name^=resultSegmentVuelta').parent().parent().removeClass('shadow-checked');
          $(this).parent().parent().addClass('shadow-checked');
        }
        else
        {
          $(this).parent().parent().removeClass('shadow-checked');
        }
    });
/*
    $('.tickets-arrives').on('click', 'input[name^=resultSegmentVuelta]', function() {
        if($(this).is(':checked') && $(this).parent().parent().hasClass('shadow-checked'))
        {
          $(this).parent().parent().removeClass('shadow-checked');
          $(this).prop('checked', false);
        }
    });
*/