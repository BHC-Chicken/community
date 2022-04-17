const USERNAME = "^(?=.{8,50}$)([0-9a-z_]{4,})@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
const PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";
const NICKNAME = "^([0-9a-zA-Z가-힣]{1,10})$";

const formSignUp = window.document.body.querySelector('[rel="form-signup"]');
const username = formSignUp.querySelector('[name="username"]');

username.addEventListener('focusout', () => {
    const usernameMessage = window.document.body.querySelector('[rel="email-message"]');
    usernameMessage.innerText='';
    usernameMessage.classList.remove('good');
    usernameMessage.classList.remove('warning');

    if (!username.value.match(USERNAME)) {
        usernameMessage.innerText="올바른 이메일 형식이 아닙니다.";
        usernameMessage.classList.add('warning');
        return false;
    }
    $.ajax({
        url: '/signup/check',
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

formSignUp.onsubmit = () => {
    const username = document.getElementById('username').value;
    if (!username.match(USERNAME)) {
        console.log(username)
        alert('올바른 아이디를 입력해주세요.')
    }

    const password = document.getElementById('password').value;
    if (!password.match(PASSWORD)) {
        alert('올바른 비밀번호를 입력해주세요.')
    }

    const nickname = document.getElementById('nickname').value;
    if (!password.match(NICKNAME)) {
        alert('올바른 별명을 입력해주세요.')
    }
}
