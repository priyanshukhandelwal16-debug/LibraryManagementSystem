// =========================================================
// Library Management System - Client side JS
// Handles: mobile sidebar toggle, confirm dialogs, client validation
// =========================================================

document.addEventListener('DOMContentLoaded', function () {

    // ---- Mobile sidebar toggle ----
    var toggleBtn = document.getElementById('sidebarToggle');
    var sidebar = document.querySelector('.sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', function () {
            sidebar.classList.toggle('open');
        });
    }

    // ---- Confirm before delete ----
    document.querySelectorAll('.confirm-delete').forEach(function (el) {
        el.addEventListener('click', function (e) {
            var msg = el.getAttribute('data-confirm-message') || 'Are you sure you want to delete this record?';
            if (!confirm(msg)) {
                e.preventDefault();
            }
        });
    });

    // ---- Confirm before return ----
    document.querySelectorAll('.confirm-return').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!confirm('Mark this book as returned?')) {
                e.preventDefault();
            }
        });
    });

    // ---- Auto-hide alerts after 4 seconds ----
    document.querySelectorAll('.alert').forEach(function (alertBox) {
        setTimeout(function () {
            alertBox.style.transition = 'opacity 0.5s';
            alertBox.style.opacity = '0';
            setTimeout(function () { alertBox.style.display = 'none'; }, 500);
        }, 4000);
    });

    // ---- Generic client-side validation for forms with class 'validate-form' ----
    document.querySelectorAll('.validate-form').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            var valid = true;
            var errorMessages = [];

            form.querySelectorAll('[required]').forEach(function (field) {
                if (!field.value || !field.value.toString().trim()) {
                    valid = false;
                    field.classList.add('input-error');
                    errorMessages.push((field.getAttribute('data-label') || field.name) + ' is required.');
                } else {
                    field.classList.remove('input-error');
                }
            });

            var emailField = form.querySelector('input[type="email"]');
            if (emailField && emailField.value) {
                var emailPattern = /^[\w.+-]+@[\w-]+\.[a-zA-Z]{2,}$/;
                if (!emailPattern.test(emailField.value)) {
                    valid = false;
                    emailField.classList.add('input-error');
                    errorMessages.push('Enter a valid email address.');
                }
            }

            var phoneField = form.querySelector('input[name="phone"]');
            if (phoneField && phoneField.value) {
                var phonePattern = /^[0-9]{10}$/;
                if (!phonePattern.test(phoneField.value)) {
                    valid = false;
                    phoneField.classList.add('input-error');
                    errorMessages.push('Phone number must be exactly 10 digits.');
                }
            }

            var qtyField = form.querySelector('input[name="quantity"]');
            if (qtyField && qtyField.value !== '') {
                if (isNaN(qtyField.value) || Number(qtyField.value) < 0) {
                    valid = false;
                    errorMessages.push('Quantity must be a valid non-negative number.');
                }
            }

            var dueDateField = form.querySelector('input[name="dueDate"]');
            var issueDateField = form.querySelector('input[name="issueDate"]');
            if (dueDateField && issueDateField && dueDateField.value && issueDateField.value) {
                if (new Date(dueDateField.value) < new Date(issueDateField.value)) {
                    valid = false;
                    errorMessages.push('Due date cannot be before issue date.');
                }
            }

            if (!valid) {
                e.preventDefault();
                var box = form.querySelector('.js-validation-error');
                if (box) {
                    box.textContent = errorMessages.join(' ');
                    box.style.display = 'block';
                } else {
                    alert(errorMessages.join('\n'));
                }
            }
        });
    });

    // ---- Live search filter for tables with class 'live-search' ----
    var liveSearchInput = document.getElementById('liveSearchInput');
    if (liveSearchInput) {
        liveSearchInput.addEventListener('keyup', function () {
            var value = liveSearchInput.value.toLowerCase();
            var rows = document.querySelectorAll('table.searchable tbody tr');
            rows.forEach(function (row) {
                row.style.display = row.textContent.toLowerCase().indexOf(value) > -1 ? '' : 'none';
            });
        });
    }
});
