const USERNAME = "^(?=.{8,50}$)([0-9a-z_]{4,})@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
const PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";
const NICKNAME = "^([0-9a-zA-Z가-힣]{1,10})$";

const formSignUp = window.document.body.querySelector('[rel="form-signup"]');
formSignUp.onsubmit = () => {
    const username = document.getElementById('username').value;
    if (!username.match(USERNAME)){
        console.log(username)
        alert('올바른 아이디를 입력해주세요.')
    }

    const password = document.getElementById('password').value;
    if (!password.match(PASSWORD)){
        alert('올바른 비밀번호를 입력해주세요.')
    }
}