const articleList = document.getElementsByTagName("tbody")[0];
const csrfToken = articleList.getAttribute("data-csrfToken");
const searchKeyword = document.getElementById("search-keyword");
const searchInput = document.getElementById("search-input");
const searchButton = document.getElementById("search-button");
const hiddenBoardCode = document.getElementById("hidden-boardCode");
const title = document.getElementsByClassName("title");

searchButton.addEventListener('click', () => {
    if (searchInput.value.length === 0) {
        return false;
    }

    window.location.href = "/board/" + hiddenBoardCode.value + "/search?" + searchKeyword.value + "=" + encodeURIComponent(searchInput.value);

})