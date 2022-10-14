const detectText = window.document.body.querySelector('[rel = "upload"]');
const hidden = window.document.body.querySelector('[rel="hiddenCode"]');

var formOrc = window.document.body.querySelector('[rel="form-orc"]')
var csrfToken = formOrc.querySelector('[name="_csrf"]')

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
            let json = JSON.parse(data);
            alert("학생증 인증에 성공 하였습니다." +
                " 이용가능한 게시판은 " + json["college"] + " 입니다");
            window.history.back();
        },
        beforeSend:function () {
            $('.wrap-loading').removeClass('display-none');
        },
        complete:function() {
            $('.wrap-loading').addClass('display-none');
        },
        error:function (e) {
            alert("인증에 실패하였습니다.");
        }
    })
})

const dropzone = new Dropzone("div.my-dropzone", {
    url: "/orcSignup",
    disablePreviews: true,
    paramName: 'image',
    init: function () {
        this.on("sending", function (file, xhr, formData) {
            $('.wrap-loading').removeClass('display-none');
            formData.append("_csrf", csrfToken)
        });
        this.on("success", function (file, response) {
            // let json = JSON.parse(response);
            alert("학생증 인증에 성공 하였습니다." +
                " 이용가능한 게시판은 " + response["college"] + " 입니다");
            window.history.back();
        });
        this.on("error", function () {
            alert("인증에 실패하였습니다.");
        })
        this.on("complete", function () {
            $('.wrap-loading').addClass('display-none');
        })
    }
});