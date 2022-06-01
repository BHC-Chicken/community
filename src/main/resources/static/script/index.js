const collegeList = window.document.getElementsByClassName('college-list')[0];
const collegeNone = window.document.getElementsByClassName('college-none')[0];

collegeList.addEventListener('click', ()=> {
    collegeNone.classList.toggle('college-none');
})