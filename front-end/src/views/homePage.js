const homeSection = document.getElementById("homeView");
const ul = document.querySelector("ul");

let ctx = null;

export function showHome(context) {
    ul.innerHTML = "";
    ctx = context
    createHomePage();
    ctx.render(homeSection);
}

async function createHomePage() {
    const response = await fetch("http://localhost:8080/api/recipe", {
        method: "GET"
    });

    const recipe = await response.json();

    if (recipe.length === 0) {
        const errorMsg = "<h2>No recipe info yet.</h2>";
        homeSection.appendChild(errorMsg);
    }

    for (let el of recipe) {
        ul.innerHTML += `<li class="cards_item">
            <div class="card">
                <div class="card_image">
                    <img src="${el.imageUrl}" alt="" />
                </div>
                <div class="card_content">
                    <h2 class="card_title">${el.title}</h2>
                    <center><a class="details-btn" href="details" data-id="${el.id}">Show More</a></center>
                </div>
            </div>
        </li>`;

        homeSection.appendChild(ul);
    }
    homeSection.querySelectorAll(".details-btn").forEach(a => a.addEventListener("click", onDetails));
}

function onDetails(event) {
    event.preventDefault();
    const data = event.target.dataset.id;
    ctx.goTo("/details", data);
}