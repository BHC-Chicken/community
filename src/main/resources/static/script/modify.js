const postArticle = window.document.querySelector('.postArticle');
const title = window.document.querySelector('#title');
const content = window.document.querySelector('#inputText');

let csrfToken = window.document.body.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken = csrfToken.value
}

postArticle.addEventListener('click', (e) => {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (title.value !== "" && content.value !== "") {
        formData.append("title", title.value);
        formData.append("content", content.value)
    } else {
        alert("제목과 내용을 적어주세요.");
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
            window.location.href = window.document.location.pathname.replace("/modify/", "/");
        },
        error: function (e) {
            alert("제목과 내용을 적어주세요.");
        }
    })
    e.preventDefault();
})

