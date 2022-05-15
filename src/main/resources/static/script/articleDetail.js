const deleteComment = window.document.querySelector('#commentTable');
const commentList = window.document.querySelector('.commentList');
const createComment = window.document.querySelector('[rel="createComment"]')
const commentContent = window.document.querySelector('.commentContent')
const recommendArticle = window.document.querySelector('#recommend')

const display = window.document.querySelector('.display-none')

let csrfToken = window.document.body.querySelector('[name="_csrf"]')

if (csrfToken) {
    csrfToken=csrfToken.value
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
        data:formData,
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
        url:window.document.location.pathname,
        method:'POST',
        data:formData,
        processData: false,
        contentType: false,
        cache: false,
        success:function () {
            loadComment();
            commentContent.value = "";
        },
        error:function (e) {
            alert("댓글 등록에 실패")
        }
    })
    e.preventDefault();
})



function loadComment() {
    $.ajax( {
            url: '/board/comment/'+window.location.pathname.substring(7),
            method: 'GET',
            processData: false,
            contentType: false,
            cache: true,
        }

    ).done(function (data) {
        commentList.innerHTML=data
    })
}

window.addEventListener('load', loadComment)

commentList.addEventListener('click', (e) => {
    let formData =new FormData();
    if (csrfToken) {
        formData.append("_csrf", csrfToken);
    }
    if (!e.target.classList.contains("deleteComment")) {
        return;
    }
    $.ajax({
        url: e.target.href,
        data:formData,
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