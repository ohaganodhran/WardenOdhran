const loginTab = document.getElementById("loginTab");
const signupTab = document.getElementById("signupTab");
const loginForm = document.getElementById("loginForm");
const signupForm = document.getElementById("signupForm");
const forgotPasswordButton = document.getElementById("forgotPassword");

loginTab.addEventListener("click", () => {
    loginTab.classList.add("active");
    forgotPasswordButton.style.display="unset";
    signupTab.classList.remove("active");
    loginForm.classList.remove("hidden");
    signupForm.classList.add("hidden");
});
signupTab.addEventListener("click", () => {
    signupTab.classList.add("active");
    forgotPasswordButton.style.display="none";
    loginTab.classList.remove("active");
    signupForm.classList.remove("hidden");
    loginForm.classList.add("hidden");
});

window.addEventListener("DOMContentLoaded", () => {
    if (typeof showSignup !== "undefined" && showSignup === true) {
        signupTab.classList.add("active");
        loginTab.classList.remove("active");
        signupForm.classList.remove("hidden");
        loginForm.classList.add("hidden");
    }
});


