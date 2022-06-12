const verificationEmail = window.document.querySelector('[rel = "submit-search"]');
const email = window.document.getElementById("username");

const username = window.document.querySelector('[name="username"]');
let csrfToken = window.document.body.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken = csrfToken.value
}

function changePassword(e) {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (email.value !== "") {
        formData.append("username", email.value);
    } else {
        alert("이메일을 적어주세요.");
        return;
    }
    $.ajax({
        url: e.target.parentElement.getAttribute("action"),
        data: formData,
        method: 'POST',
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            window.location.href = window.document.location.pathname.replace("/searchPassword", "/");
            alert("입력하신 이메일로 메일을 전송하였습니다.")
        },
        error: function (e) {
            alert("존재하지 않는 이메일입니다.");
        }
    })
    e.preventDefault();
}

username.addEventListener('keydown', (e) => {
    if (e.keyCode === 13) {
        changePassword(e);
    }
})

verificationEmail.addEventListener('click',changePassword)
