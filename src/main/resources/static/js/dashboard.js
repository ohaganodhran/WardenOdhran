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

function toggleModal(modal) {
    document.getElementById(modal).classList.toggle("hidden");
}

function search() {
    const searchInput = document.getElementById("search");
    const cards = document.querySelectorAll('.credential-card');

    const query = searchInput.value.toLowerCase();

    cards.forEach(card => {
        const siteNameElement = card.querySelector('.site-name');
        const siteNameText = siteNameElement ? siteNameElement.textContent.toLowerCase() : "";

        card.style.display = siteNameText.includes(query) ? "" : "none";
    });
}

function toggleEye(eye, passwordField, passwordInput) {
    const input = eye.closest(passwordField).querySelector(passwordInput);
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
}

document.addEventListener('DOMContentLoaded', () => {
    const addCredBtn = document.getElementById('addCredentialBtn');
    const closeCredBtn = document.getElementById('closeAddBtn');

    if (addCredBtn) {
        addCredBtn.addEventListener('click', () => toggleModal("credentialModal"));
    }

    if (closeCredBtn) {
        closeCredBtn.addEventListener('click', () => toggleModal("credentialModal"));
    }

    document.querySelectorAll('.credential-header').forEach(header => {
        header.addEventListener('click', () => toggleCard(header));
    });

    document.querySelectorAll('.eye-toggle').forEach(eye => {
        eye.addEventListener('click', () => toggleEye(eye, '.password-field', '.password-input'));
    });

    document.querySelectorAll('.eyeForAddCred').forEach(eye => {
        eye.addEventListener('click', () => toggleEye(eye, '.password-field-add', '.password-input-add'));
    });

    document.querySelectorAll('.eyeForEditCred').forEach(eye => {
        eye.addEventListener('click', () => toggleEye(eye, '.password-field-edit', '.password-input-edit'));
    });

    document.querySelectorAll('.deleteCredential').forEach(btn => {
        btn.addEventListener('click', () => {
            const card = btn.closest('.credential-card');
            const siteName = card.querySelector('.site-name').textContent;
            const credentialId = card.dataset.id;

            const template = document.getElementById('deleteModal');
            const modal = template.cloneNode(true);
            modal.id = '';
            modal.classList.remove('hidden');

            modal.querySelector('.deleteSiteName').textContent = siteName;

            modal.querySelector('.deleteCredentialId').value = credentialId;

            modal.querySelector('.close-delete-cred-btn').addEventListener('click', () => {
                modal.remove();
            });

            document.body.appendChild(modal);
        });
    });

    document.querySelectorAll('.editCredential').forEach(btn => {
        btn.addEventListener('click', () => {

            const card = btn.closest('.credential-card');

            const credentialId = card.dataset.id;
            const siteName = card.querySelector('.site-name').textContent;
            const username = card.querySelector('.username').textContent;
            const password = card.querySelector('.password-input').value;
            const notes = card.querySelector('.notes').textContent;

            const template = document.getElementById('credentialEditModal');
            const modal = template.cloneNode(true);
            modal.id = '';
            modal.classList.remove('hidden');

            modal.querySelector('.siteNameToEdit').value = siteName;
            modal.querySelector('.usernameToEdit').value = username;
            modal.querySelector('.password-input-edit').value = password;
            modal.querySelector('.notesToEdit').value = notes;


            modal.querySelector('.editCredentialId').value = credentialId;

            modal.querySelector('.closeEditBtn').addEventListener('click', () => {
                modal.remove();
            });

            modal.querySelector('.eyeForEditCred').addEventListener('click', () => {
                toggleEye(
                    modal.querySelector('.eyeForEditCred'),
                    '.password-field-edit',
                    '.password-input-edit'
                );
            });

            document.body.appendChild(modal);
        });
    });

    document.getElementById("search").addEventListener('input', search);
});
