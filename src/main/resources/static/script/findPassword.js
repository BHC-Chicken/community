const PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";

const formChangePassword = window.document.body.querySelector('[rel="form-changePass"]');

const password = formChangePassword.querySelector('[rel="password"]');
const passwordCheck = formChangePassword.querySelector('[rel="password-check"]');
const changeInfo = formChangePassword.querySelector('[rel="submit-changeInfo"]')

let csrfToken = window.document.body.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken = csrfToken.value
}

changeInfo.addEventListener('click', (e)=> {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (password.value !== "" && passwordCheck.value !== "") {
        formData.append("modifyPassword", passwordCheck.value);
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
            window.location.href = window.document.location.pathname.replace("/findPassword", "/");
            alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.")
        },
        error: function (e) {
            alert("비밀번호 변경에 실패하였습니다.");
        }
    })
    e.preventDefault();
})

password.addEventListener('focusout', () => {
    const passwordMessage = window.document.body.querySelector('[rel="password-message"]');
    passwordMessage.innerText = '';
    passwordMessage.classList.remove('good');
    passwordMessage.classList.remove('warning');

    if (!password.value.match(PASSWORD)) {
        passwordMessage.innerText = "올바른 비밀번호 형식이 아닙니다.";
        passwordMessage.classList.add('warning');
    } else {
        passwordMessage.innerText = "사용 가능한 비밀번호입니다.";
        passwordMessage.classList.add('good');
    }
});

passwordCheck.addEventListener('focusout', () => {
    const passwordMessage = window.document.body.querySelector('[rel="password-check-message"]');

    passwordMessage.innerText = '';
    passwordMessage.classList.remove('good');
    passwordMessage.classList.remove('warning');

    const password = window.document.body.querySelector('[rel="password"]').value;
    console.log(password);
    const password2 = window.document.body.querySelector('[rel="password-check"]').value;
    console.log(password2);

    if (password2 === '') {
        passwordMessage.innerText = "비밀번호를 확인해주세요.";
        passwordMessage.classList.add('warning');
        return false;
    }
    if (password2 !== password) {
        passwordMessage.innerText = '비밀번호가 일치하지 않습니다.';
        passwordMessage.classList.add('warning');
    } else {
        passwordMessage.innerText = '비밀번호가 일치합니다.';
        passwordMessage.classList.remove('warning');
    }
})

formChangePassword.onsubmit = () => {
    const password = document.getElementById('password').value;
    if (!password.match(PASSWORD)) {
        alert('올바른 비밀번호를 입력해주세요.')
        formChangePassword['password'].focus();
        formChangePassword['password'].select();
        return false;
    }
}
