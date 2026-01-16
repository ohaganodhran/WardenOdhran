const loginTab = document.getElementById("loginTab");
const signupTab = document.getElementById("signupTab");
const loginForm = document.getElementById("loginForm");
const signupForm = document.getElementById("signupForm");
const forgotPasswordButton = document.getElementById("forgotPassword");

function toggleEye(eye) {
    const input = eye.previousElementSibling;
    if (!input || input.tagName !== "INPUT") return;

    eye.style.transform = "translateY(-50%) scale(0) rotateY(90deg)";

    setTimeout(() => {
        const isPassword = input.type === "password";
        input.type = isPassword ? "text" : "password";
        eye.src = isPassword ? "/Assets/hide.png" : "/Assets/eye.png";

        eye.style.transform = "translateY(-50%) scale(1) rotateY(0deg)";
    }, 150);
}

document.querySelectorAll(".password-eye").forEach(eye => {
    eye.addEventListener("click", () => toggleEye(eye));
});

loginTab.addEventListener("click", () => {
    loginTab.classList.add("active");
    forgotPasswordButton.style.visibility = "visible"; // Changed from display="unset"
    signupTab.classList.remove("active");
    loginForm.classList.remove("hidden");
    signupForm.classList.add("hidden");
});
signupTab.addEventListener("click", () => {
    signupTab.classList.add("active");
    forgotPasswordButton.style.visibility = "hidden"; // Changed from display="none"
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


