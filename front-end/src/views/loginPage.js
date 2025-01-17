import {setUserData} from "../helper/userHelper.js";

const loginSection = document.getElementById("loginView");
const loginForm = document.getElementById("loginForm");
loginForm.addEventListener("submit", onLogin);

let ctx = null;

export function showLoginPage(context) {
    ctx = context;
    ctx.render(loginSection);
}

async function onLogin(event) {
    event.preventDefault();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    if (!email || !password) {
        return alert("Invalid email or password!");
    }

    const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email, password})
    });
    if (response.status === 401) {
        return alert("Invalid email or password!");
    }

    const data = await response.json();

    setUserData(data.user, data.jwtToken);
    ctx.updateNav();
    ctx.goTo("/home");
    loginForm.reset();
}