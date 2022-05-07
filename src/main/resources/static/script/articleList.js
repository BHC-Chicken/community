const articleList = document.getElementsByTagName("tbody")[0];
const csrfToken = articleList.getAttribute("data-csrfToken");

articleList.addEventListener('click', (e) => {

    if (e.target.classList.contains("delete")) {
        let formData = new FormData();
        formData.append("_csrf", csrfToken);

        $.ajax({
            url:e.target.href,
            method:'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success:function () {
                window.location.reload();
            },
            error:function (e) {
                alert("삭제 실패");
            }
        });
        e.preventDefault();
    }
});




