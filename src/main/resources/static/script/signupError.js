const USERNAME = "^(?=.{8,50}$)([0-9a-z_]{4,})@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
const PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";
const NICKNAME = "^([0-9a-zA-Z가-힣]{1,10})$";

const formSignUp = window.document.body.querySelector('[rel="form-signup"]');
const username = formSignUp.querySelector('[name="username"]');
const password = formSignUp.querySelector('[rel="password"]');
const passwordCheck = formSignUp.querySelector('[name="password"]');
const nickname = formSignUp.querySelector('[name="nickname"]');

username.addEventListener('focusout', () => {
    const usernameMessage = window.document.body.querySelector('[rel="email-message"]');
    usernameMessage.innerText = '';
    usernameMessage.classList.remove('good');
    usernameMessage.classList.remove('warning');

    if (!username.value.match(USERNAME)) {
        usernameMessage.innerText = "올바른 이메일 형식이 아닙니다.";
        usernameMessage.classList.add('warning');
        return false;
    }
    $.ajax({
        url: '/signup/check-username',
        data: {
            username: formSignUp['username'].value
        },
        method: 'POST',
        success: function (check) {
            if (check === 0) {
                usernameMessage.innerText = '이미 사용 중인 이메일입니다.';
                usernameMessage.classList.add('warning');
            } else {
                usernameMessage.innerText = '사용 가능한 이메일입니다.';
                usernameMessage.classList.add('good');
            }
        }
    })
});

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

nickname.addEventListener('focusout', () => {
    const nickname = window.document.body.querySelector('[name="nickname"]').value;
    const nicknameMessage = window.document.body.querySelector('[rel="nickname-message"]');

    nicknameMessage.innerText = '';
    nicknameMessage.classList.remove('good');
    nicknameMessage.classList.remove('warning');

    if (nickname === '') {
        nicknameMessage.innerText = "닉네임을 입력해주세요.";
        nicknameMessage.classList.add('warning');
        return false;
    }
    if (!nickname.match(NICKNAME)) {
        nicknameMessage.innerText = "올바른 닉네임 형식이 아닙니다.";
        nicknameMessage.classList.add('warning');
        return false;
    }
    $.ajax({
        url: '/signup/check-nickname',
        data: {
            nickname: formSignUp['nickname'].value
        },
        method: 'POST',
        success: function (check) {
            if (check === 0) {
                nicknameMessage.innerText = '이미 사용 중인 별명입니다.';
                nicknameMessage.classList.add('warning');
            } else {
                nicknameMessage.innerText = '사용 가능한 별명입니다.';
                nicknameMessage.classList.add('good');
            }
        }
    })
});

formSignUp.onsubmit = () => {
    const username = document.getElementById('username').value;
    if (!username.match(USERNAME)) {
        console.log(username)
        alert('올바른 아이디를 입력해주세요.')
        formSignUp['username'].focus();
        formSignUp['username'].select();
        return false;
    }

    const password = document.getElementById('password').value;
    if (!password.match(PASSWORD)) {
        alert('올바른 비밀번호를 입력해주세요.')
        formSignUp['password'].focus();
        formSignUp['password'].select();
        return false;
    }

    const nickname = document.getElementById('nickname').value;
    if (!nickname.match(NICKNAME)) {
        alert('올바른 별명을 입력해주세요.')
        formSignUp['nickname'].focus();
        formSignUp['nickname'].select();
        return false;
    }
}
