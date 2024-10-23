import {getUserId} from "../helper/userHelper.js";

const editSection = document.getElementById("editView");
let ctx = null;

export async function showEdit(context, data) {
    ctx = context;
    ctx.render(editSection);

    const id = data[0];
    const response = await fetch(`http://localhost:8080/api/recipe/${id}`, {
        method: "GET"
    });
    const dataDetails = await response.json();
    editSection.innerHTML = createEdit(dataDetails);

    const submitBtn = document.querySelector("input[type='submit']");
    submitBtn.addEventListener("click", async (event) => {
        event.preventDefault();

        const imageUrl = document.getElementById("editImgURL").value;
        const title = document.getElementById("editTitle").value;
        const ingredients = document.getElementById("editProduction").value;
        const preparation = document.getElementById("editDescription").value;
        const userId = getUserId();

        if (!imageUrl || !title || !ingredients || !preparation) {
            return alert("Error input");
        }

        const item = {userId, title, imageUrl, ingredients, preparation}
        await fetch(`http://localhost:8080/api/recipe/${id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(item)
        });
        ctx.goTo("/details", id);
    });
}

function createEdit(dataDetails) {
    let ingredients = '';

    for (let product of dataDetails.ingredients) {
        ingredients += `${product.name}-${product.quantity}\n`;
    }

    return `<h2>Edit recipe</h2>
            <form class="editForm" id="editForm">
                <label>Image URL</label>
                <input type="url" id="editImgURL" value="${dataDetails.imageUrl}" />
                <label>Recipe Title</label>
                <input type="text" id="editTitle" value="${dataDetails.title}" />
                <label>Products -> <b>one per line</b> / Example: <b>Eggs-2br</b></label>
                <textarea type="text" id="editProduction">${ingredients}</textarea>
                <label>Method of preparation:</label>
                <textarea type="text" id="editDescription">${dataDetails.preparation}</textarea>
                <input type="submit" id="editeBtn" value="Update" />
            </form>`;
}