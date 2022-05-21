const deleteComment = window.document.querySelector('#commentTable');
const commentList = window.document.querySelector('.commentList');
const createComment = window.document.querySelector('[rel="createComment"]');
const commentContent = window.document.querySelector('.commentContent');
const recommendArticle = window.document.querySelector('#recommend');
const reportButton = window.document.querySelector('#reportButton');
const reportDisplay = window.document.querySelector('#report');
const report = window.document.querySelector('#submit-report');
const reportReason = window.document.querySelector('#report-reason')

const display = window.document.querySelector('.display-none')

let csrfToken = window.document.body.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken = csrfToken.value
}

recommendArticle.addEventListener('click', (e) => {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (!e.target.classList.contains("recommend-article")) {
        return;
    }

    $.ajax({
        url: e.target.href,
        data: formData,
        method: 'POST',
        processData: false,
        contentType: false,
        cache: false,
        success: function (d) {

            document.getElementById('recommendCount').innerText = d['hit'];
        },
        error: function (e) {
            alert("실패");
        }
    })
    e.preventDefault()
})

createComment.addEventListener('click', (e) => {
    let formData = new FormData();
    formData.append("_csrf", csrfToken);
    formData.append("content", commentContent.value);

    $.ajax({
        url: window.document.location.pathname,
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        success: function () {
            loadComment();
            commentContent.value = "";
        },
        error: function (e) {
            alert("댓글 등록에 실패")
        }
    })
    e.preventDefault();
})


function loadComment() {
    $.ajax({
            url: '/board/comment/' + window.location.pathname.substring(7),
            method: 'GET',
            processData: false,
            contentType: false,
            cache: true,
        }
    ).done(function (data) {
        commentList.innerHTML = data
    })
}

window.addEventListener('load', loadComment)

commentList.addEventListener('click', (e) => {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (!e.target.classList.contains("deleteComment")) {
        return;
    }
    $.ajax({
        url: e.target.href,
        data: formData,
        method: 'POST',
        processData: false,
        contentType: false,
        cache: false,
        success: function (url) {
            alert("삭제되었습니다.");
            loadComment();
        },
        error: function (e) {
            alert("실패");
        }
    })
    e.preventDefault()
})

reportButton.addEventListener('click', () => {
    reportDisplay.classList.toggle('display-none')
})

report.addEventListener('click', (e) => {
    let formData = new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (reportReason) {
        formData.append("reportReason", reportReason.value)
    }

    $.ajax({
        url: window.document.location.pathname + "/report",
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
        success: function (e) {
            reportReason.value = "";
            window.location.href=window.document.location.pathname + "/../";
            alert("신고되었습니다.");
        },
        error: function (e) {
            alert("실패");
        }
    })
    e.preventDefault()
})