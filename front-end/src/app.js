import {getUser} from "./helper/userHelper.js";
import {showHome} from "./views/homePage.js";
import {showLoginPage} from "./views/loginPage.js";
import {showRegister} from "./views/registerPage.js";
import {showCreatePage} from "./views/createPage.js";
import {showDetails} from "./views/detailsPage.js";
import {showEdit} from "./views/editPage.js";
import {showSearch} from "./views/searchPage.js";

document.querySelectorAll("section").forEach(section => section.remove());

const main = document.querySelector("main");
const nav = document.querySelector("nav");
nav.addEventListener("click", onNavigate);

const userNav = document.querySelectorAll("li.user");
const guestNav = document.querySelectorAll("li.guest");
const userMsg = document.getElementById("userMsg");

updateNav();

const routes = {
    "/home": showHome,
    "/login": showLoginPage,
    "/register": showRegister,
    "/create": showCreatePage,
    "/details": showDetails,
    "/edit": showEdit,
    "/search": showSearch,
    "/logout": async () => {
        await sessionStorage.removeItem("userData");
        updateNav();
        showHome(context);
    },
    "*": () => console.error("404 Page not found!")
}

export function updateNav() {
    const user = getUser();

    if (user) {
        userNav.forEach(li => li.style.display = "block");
        guestNav.forEach(li => li.style.display = "none");
        userMsg.textContent = `Welcome, ${user.firstName} ${user.lastName}!`;
    } else {
        userNav.forEach(li => li.style.display = "none");
        guestNav.forEach(li => li.style.display = "block");
        userMsg.textContent = "";
    }
}

function render(view) {
    main.replaceChildren(view);
}

function onNavigate(event) {
    event.preventDefault();

    const element = event.target;

    if (event.target.tagName !== "A") {
        return;
    }

    const url = new URL(element.href).pathname;
    goTo(url);
}

let context = {
    render,
    goTo,
    updateNav
}

function goTo(url, ...params) {
    const handler = routes[url];

    if (typeof (handler) !== "function") {
        return routes["*"]();
    }

    handler(context, params);
}

showHome(context)