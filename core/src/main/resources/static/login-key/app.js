const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");
const button = document.querySelector('form.sign-in-form > input')
const accountDoc = document.querySelector('form.sign-in-form > div:nth-child(2) > input[type=text]')
const passwordDoc = document.querySelector('form.sign-in-form > div:nth-child(3) > input[type=password]')

sign_up_btn.addEventListener("click", () => {
    container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
    container.classList.remove("sign-up-mode");
});

button.addEventListener('click', updateButton);

function updateButton() {
    if (location.search === "" || location.search === undefined) {
        alert("请重新扫码😄️")
    }
    let account = accountDoc.value
    let password = passwordDoc.value
    let key = location.search.split("=")[1]
    let host = location.origin
    let skipHost = `${host}/login/sure?codekey=${key}&phone=${account}&password=${password}`;
    console.log(skipHost)
    location.href = skipHost
}