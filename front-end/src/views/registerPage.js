import {setUserData} from "../helper/userHelper.js";

const registerSection = document.getElementById("registerView");
const registerForm = document.getElementById("registerForm");
registerForm.addEventListener("submit", onRegister);

let ctx = null;

export function showRegister(context) {
    ctx = context;
    ctx.render(registerSection);
}

async function onRegister(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const password = document.getElementById("password").value;
    const isCheckedBtn = document.getElementById("registerCheck");


    if (!isCheckedBtn.checked) {
        return alert("Please check box \"I agree to the terms\"");
    }

    if (!email || !password || !firstName || !lastName) {
        return alert("Invalid input!");
    }

    const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email, firstName, lastName, password})
    });

    if (response.status === 400) {
        const data = await response.json();
        let message = '';

        for (let e of data) {
            message += `${e.reason}\n`;
        }

        alert(message);

    } else if (response.status === 409) {
        alert("Email already exists!")
    }

    const data = await response.json();

    setUserData(data.user, data.jwtToken);
    ctx.goTo("/home");
    ctx.updateNav();
    registerForm.reset();
}