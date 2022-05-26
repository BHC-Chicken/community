const articleList = document.getElementsByTagName("tbody")[0];
const csrfToken = articleList.getAttribute("data-csrfToken");
const searchKeyword = document.getElementById("search-keyword");
const searchInput = document.getElementById("search-input");
const searchButton = document.getElementById("search-button");
const hiddenBoardCode = document.getElementById("hidden-boardCode");
const title = document.getElementsByClassName("title");
const modify = document.getElementsByClassName("modify");

try {
    modify.addEventListener('click', (e) => {
        window.location.href = e.target.href
    })
} catch (e) {
    console.log(e);
    alert('수정이 불가능합니다.');
}

modify.addEventListener('click', ()=> {

})


articleList.addEventListener('click', (e) => {
    if (e.target.classList.contains("delete")) {
        let formData = new FormData();
        formData.append("_csrf", csrfToken);

        $.ajax({
            url: e.target.href,
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            success: function () {
                window.location.reload();
            },
            error: function (e) {
                alert("삭제에 실패했습니다.");
            }
        });
        e.preventDefault();
    }
});

searchButton.addEventListener('click', () => {
    if (searchInput.value.length === 0) {
        return false;
    }

    window.location.href = "/board/" + hiddenBoardCode.value + "/search?" + searchKeyword.value + "=" + encodeURIComponent(searchInput.value);

})