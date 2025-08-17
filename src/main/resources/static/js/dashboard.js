function toggleCard(headerElement) {
    const card = headerElement.closest(".credential-card");
    const isOpen = card.classList.toggle("open");

    const arrow = headerElement.querySelector(".arrow");
    arrow.classList.toggle("rotated");

    if(!isOpen) {
        const passwordInput = card.querySelector('.password-input');
        const eyeToggle = card.querySelector('.eye-toggle');
        passwordInput.type = 'password';
        eyeToggle.src = '/Assets/eye.png';
        eyeToggle.style.transform = 'scale(1) rotateY(0deg)';
    }
}

function toggleModal() {
    const modal = document.getElementById('credentialModal');
    modal.classList.toggle('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
    const addBtn = document.getElementById('addCredentialBtn');
    const closeBtn = document.querySelector('.close-btn');

    if(closeBtn) {
        closeBtn.addEventListener('click', toggleModal)
    }

    addBtn.addEventListener('click', toggleModal);

    document.querySelectorAll('.credential-header').forEach(header => {
        header.addEventListener('click', () => toggleCard(header));
    });

    document.querySelectorAll('.eye-toggle').forEach(eye => {
        eye.addEventListener('click', () => {
            const input = eye.closest('.password-field').querySelector('.password-input');
            if(!input) return;

            eye.style.transform = 'scale(0) rotateY(90deg)';

            setTimeout(() => {
                if(input.type === 'password') {
                    input.type = 'text';
                    eye.src = '/Assets/hide.png';
                } else {
                    input.type = 'password';
                    eye.src = 'Assets/eye.png';
                }
                eye.style.transform = 'scale(1) rotateY(0deg)';
            }, 200);
        });
    });
});
