$(function(){
	 $('input.i-checks').iCheck({
	    checkboxClass: 'icheckbox_square-${cookie.theme.value==null?"blue":cookie.theme.value}',
	    radioClass: 'iradio_square-${cookie.theme.value==null?"blue":cookie.theme.value}',
	    increaseArea: '20%' // optional
	  });
	});