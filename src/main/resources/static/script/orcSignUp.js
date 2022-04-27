const detectText = window.document.body.querySelector('[rel = "upload"]');
const hidden = window.document.body.querySelector('[rel="hiddenCode"]');

detectText.addEventListener('change', () => {
    let formData = new FormData();
    formData.append("code", hidden.value);
    formData.append("image", detectText.files[0])
    $.ajax({
        url:"/verificationEmail",
        data: formData,
        method:'POST',
        processData:false,
        contentType: false,
        cache:false,
        success:function (data) {
            alert("성공");
        },
        error:function (e) {
            alert("실패");
        }
    })
})