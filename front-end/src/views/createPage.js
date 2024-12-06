import {getUserId, getUserToken} from "../helper/userHelper.js";

const createSection = document.getElementById("createView");
const createForm = document.getElementById("createForm");
createForm.addEventListener("submit", onCreate);

let ctx = null;

export function showCreatePage(context) {
    ctx = context;
    ctx.render(createSection);
}

async function onCreate(event) {
    event.preventDefault();

    const imageUrl = document.getElementById("imageUrl").value;
    const title = document.getElementById("title").value;
    const ingredients = document.getElementById("ingredients").value;
    const preparation = document.getElementById("preparation").value;
    const userId = getUserId();

    if (!imageUrl || !title || !ingredients || !preparation) {
        return alert("Invalid input!");
    }
    const token = getUserToken();

    const response = await fetch("http://localhost:8080/api/recipe", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({userId, imageUrl, title, ingredients, preparation})
    });

    if (response.status === 403) {
        alert("Please login again")
        ctx.goTo("/logout")

    } else if (response.status === 400) {
        const body = await response.json();
        let message = "";

        for (let e of body) {
            message += `${e.reason}\n`;
        }

        alert(message);

    } else {
        ctx.goTo("/home");
        createForm.reset();

    }
}