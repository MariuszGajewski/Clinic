    var form  = document.getElementById('regForm')
 /*   var login = document.getElementById('loginInput');
    var name = document.getElementById('nameInput');
    var surname = document.getElementById('surnameInput');
    var password = document.getElementById('passwordInput');
    var passwordRepeat = document.getElementById('passwordRepeatInput');
    var email = document.getElementById('emailInput');*/
    var error = document.querySelector('.error');


    var j = form.children.length;
    for (var i = 0; i < j - 2; i++) {
        form.children[i].firstElementChild.addEventListener("input", function (event) {
            if (!this.validity.valid) {
                error.innerHTML = this.validationMessage;
            } else {
                error.innerHTML = "";
            }
        });
        if (form.children[i].firstElementChild.id === "passwordRepeatInput") {
            form.children[i].firstElementChild.addEventListener("blur", function (event) {
                if (password.value !== passwordRepeat.value) {
                    error.innerHTML = "Podane hasła różnią się";
                    passwordRepeat.focus();
                } else {
                    error.innerHTML = "";
                }
            });
        }
    }
