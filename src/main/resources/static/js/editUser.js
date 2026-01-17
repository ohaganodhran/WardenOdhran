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

document.addEventListener('click', (event) => {
    const eye = event.target.closest(".password-eye");

    if (eye) {
        toggleEye(eye);
    }
});