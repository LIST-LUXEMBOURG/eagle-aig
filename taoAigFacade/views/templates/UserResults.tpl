<?php
use oat\tao\helpers\Template;

Template::inc ( 'form_context.tpl', 'tao' );
?>

<header class="section-header flex-container-full">
	<h3><?=__('Results of')?> '<?=get_data("userName")?>'</h3>
</header>

<form action="/taoAigFacade/UserResults/getExcelFile" method="post">
	<input type="hidden" name="uri" value="<?=get_data("uri")?>" />
	<button id="custom_excel" class="result-export btn-info small">
		<span class="icon-export"></span><?=__('Export Excel File')?></button>
</form>

<!--
<button id="custom_excel" onclick="$.post( '/taoAigFacade/UserResults/getExcelFile', {uri:'<?=get_data("uri")?>'},function(data){
    var fileName = <?=get_data('fileName')?>;
    var dataString = data;//JSON.stringify(data, null, 4);
    if(window.navigator.msSaveOrOpenBlob) {
        var bb = window.MSBlobBuilder;
            var blobObject = new Blob([dataString], {type : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
            window.navigator.msSaveOrOpenBlob(blobObject, fileName);
        }else{
            var link = document.createElement('a');
            link.setAttribute('href', 'data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8,'+encodeURIComponent(dataString));
            link.setAttribute('download', fileName);
            //document.getElementById('panel-manage_by_users').appendChild(link);
            if(MouseEvent){
                var event = new MouseEvent('click', {
                'view': window,
                'bubbles': true,
                'cancelable': true
              });
              link.dispatchEvent(event);      
            }else{
                link.click();
            }
        }
});" class="result-export btn-info small"><span class="icon-export"></span><?=__('Export Excel File')?></button>
-->

<?php
Template::inc ( 'footer.tpl', 'tao' );
?>






