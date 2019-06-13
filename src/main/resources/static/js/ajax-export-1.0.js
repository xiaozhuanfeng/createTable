/**
 * AJAX导出文件测试
 * @param obj
 */
function ajaxExportFileTest(obj) {
    $.ajax({
        url : "ajaxExportFileTest",
        type : "post",
        success : function (result) {
            if (result){
                let filename = "ceshi.xlsx";
                let blob = getBlob(result,{type: "application/vnd.ms-excel"});
                let isDown = true;
                let url = downloadBlob(filename,blob,isDown);
                console.log(url);
            }
        }
    });

}

/**
 * 获取Blob
 * @param base64 base64字符串
 * @param contentType 导出格式 MIME 类型
 * @param sliceSize 分割大小
 * @returns {Blob}
 */
function getBlob(base64, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    let byteCharacters = atob(base64);
    let byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        let slice = byteCharacters.slice(offset, offset + sliceSize);

        let byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        let byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: contentType});
}

/**
 * 下载文件
 * @param fileName 文件名
 * @param blob BLOB对象
 * @param isDown 是否下载，默认不下载
 * @returns {string} 返回url
 */
function downloadBlob(fileName, blob,isDown){
    //默认不下载，返回url
    //判断是直接下载还是返回对应的URL
    let url = URL.createObjectURL(blob);
    if (isDown){
        //如果是直接下载,利用a标签来实现下载
        let docEle = document;
        let link = docEle.createElement("a");
        link.innerHTML = fileName;
        link.download = fileName;
        link.href = url;
        docEle.getElementsByTagName("body")[0].appendChild(link);
        link.click();
        $(link).remove();
    }
    return url;
}

function batchDownLoadFile(objArr){
   $.each(objArr,function(key,val){
       downLoadFile(val["fileName"],val["content"]);
   })
}

function downLoadFile(fileName,content){
    let blob = getBlob(content,{type: "application/octet-stream"});
    let isDown = true;
    let url = downloadBlob(fileName,blob,isDown);
}