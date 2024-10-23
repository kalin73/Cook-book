import {getUser, getUserId} from "../helper/userHelper.js";

const detailsSection = document.getElementById("details");
let ctx = null;

export async function showDetails(context, data) {
    ctx = context;
    ctx.render(detailsSection);

    const id = data[0];
    const recipeData = await fetch(`http://localhost:8080/api/recipe/${id}`, {
        method: "GET"
    });
    const detailsData = await recipeData.json();
    const isOwner = getUserId() === detailsData.userId;


    detailsSection.innerHTML = createDetails(detailsData, isOwner);

    if (isOwner) {
        const editBtn = document.getElementById("edit-btn");
        editBtn.addEventListener("click", onEdit);

        const deleteBtn = document.getElementById("delete-btn");
        deleteBtn.addEventListener("click", onDelete);
    }
}

async function onDelete(event) {
    event.preventDefault();
    const id = event.target.dataset.id;
    const choice = confirm("Are you sure you want delete this recipe?");

    if (choice) {
        await fetch(`http://localhost:8080/api/recipe/${id}`, {
            method: "DELETE"
        });
        ctx.goTo("/home");
    }
}

function onEdit(event) {
    event.preventDefault();
    const data = event.target.dataset.id;
    ctx.goTo("/edit", data);
}

function createDetails(detailsData, isOwner) {
    let ingredients = '';
    for (let product of detailsData.ingredients) {
        ingredients += `<li>${product.name} - ${product.quantity}</li>`;
    }
    return `<div class="card">
                <div class="details_image">
                    <center><img src="${detailsData.imageUrl}" alt="" /></center>
                </div>
                <div class="card_content">
                    <h2 class="card_title">${detailsData.title}</h2>
                    <div class="card_text">
                        <p>Product:
                            <ul>
                                ${ingredients}
                            </ul>
                        </p>
                        <p>${detailsData.preparation}</p>
                    </div>
                </div>
                <div class="edit-delete-btn">
                ${isOwner ? `<a href="" id="edit-btn" data-id=${detailsData.id}>Edit</a>
                    <a href="" id="delete-btn" data-id=${detailsData.id}>Delete</a>` : ""}
                </div>
            </div>`;
}