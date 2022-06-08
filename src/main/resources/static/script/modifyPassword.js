const PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";

const formSignUp = window.document.body.querySelector('[rel="form-modifyInfo"]');

const password = formSignUp.querySelector('[rel="change-password"]');
const passwordCheck = formSignUp.querySelector('[rel="change-password-check"]');


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

    const password = window.document.body.querySelector('[rel="change-password"]').value;
    const password2 = window.document.body.querySelector('[rel="change-password-check"]').value;

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