 var uploadImageFiles = [];
 function haveInputImage() {
     uploadImageFiles = [];
     let haveImageFile = false;
     let fileInput = document.getElementById("fileInput_");
     if(fileInput.files!=null && fileInput.files.length > 0){
         uploadImageFiles.push(fileInput.files[0]);
         haveImageFile = true;
     }
     return haveImageFile
 }

 function createImageZipFile(signatureJson){
    haveInputImage();
    let imageZipFile = new JSZip();
    for (let i = 0; i < uploadImageFiles.length; i++) {
        imageZipFile.file(i.toString() + ".jpg", uploadImageFiles[i]);
    }
    imageZipFile.generateAsync({type: "blob"})
    .then(function (blob) {
        let ossFormData = new FormData();
        ossFormData.append("file", new File([blob], "zip.zip", {type: "application/zip" }));
        uploadAliyunOss("./hello3", ossFormData);
    })
    .catch(function (error) {
        showTip(lng === "en" ? "Failed to create zip file:" + error : "创建压缩文件失败:" + error);
        updateuBtnTxtStart("Start generate", "开始生成");
    });
}

function uploadAliyunOss(hostUrl, ossFormData){
    axios.post(hostUrl, ossFormData,{headers: {"Content-Type": "multipart/form-data"}})
    .then((res)=>{
        if (res.status == 200){
//            resizeImage(uploadImageFiles[0], uploadImageFiles[0].name, 64);
        }
     })
    .catch(error => {
        let a = 1;
    });
}
