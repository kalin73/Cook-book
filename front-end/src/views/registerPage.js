import {setUser} from "../helper/userHelper.js";

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

    const email = document.getElementById("registerEmail").value;
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const password = document.getElementById("registerPassword").value;
    const isCheckedBtn = document.getElementById("registerCheck");

    if (!email || !password || !firstName || !lastName || !isCheckedBtn.checked) {
        return alert("Invalid input!");
    }

    const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email, firstName, lastName, password})
    });
    const data = await response.json();

    setUser(data);
    ctx.goTo("/home");
    ctx.updateNav();
    registerForm.reset();
}