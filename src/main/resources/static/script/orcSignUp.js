const detectText = window.document.body.querySelector('[rel = "upload"]');
const hidden = window.document.body.querySelector('[rel="hiddenCode"]');

const formOrc = window.document.body.querySelector('[rel="form-orc"]')
let csrfToken = formOrc.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken=csrfToken.value
}

detectText.addEventListener('change', () => {
    let formData = new FormData();
    formData.append("code", hidden.value);
    formData.append("image", detectText.files[0])

    if (csrfToken) {
        formData.append("_csrf", csrfToken)
    }
    $.ajax({
        url:"/orcSignup",
        data: formData,
        method:'POST',
        processData:false,
        contentType: false,
        cache:false,
        success:function (data) {

            alert("성공");
            window.history.back();
        },
        beforeSend:function () {
            $('.wrap-loading').removeClass('display-none');
        },
        complete:function() {
            $('.wrap-loading').addClass('display-none');
        },
        error:function (e) {
            alert("실패");
        }
    })
})