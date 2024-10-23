const searchSection = document.getElementById("search");
const searchForm = document.querySelector(".search-form");
searchForm.addEventListener("submit", onSearch);

const searchResult = document.querySelector(".search-result");
let ctx = null;

export function showSearch(context) {
    ctx = context;
    ctx.render(searchSection);
    searchResult.innerHTML = '<p class="no-result">No result.</p>';
}

async function onSearch(event) {
    event.preventDefault();

    const searchInput = document.getElementById("search-input").value;

    if (!searchInput) {
        return alert("Invalid input!");
    }

    const response = await fetch(`http://localhost:8080/api/recipe/search?title=${searchInput}`, {
        method: "GET"
    });
    const data = await response.json();

    searchResult.innerHTML = "";

    if (data.length === 0) {
        searchResult.innerHTML = '<p class="no-result">No result.</p>';
    }

    for (let el of data) {
        searchResult.innerHTML += createSearchResult(el);
    }

    searchForm.reset();

    const detailsBtn = searchResult.querySelectorAll("a.details-btn");
    detailsBtn.forEach(a => a.addEventListener("click", onDetails));
}

function onDetails(event) {
    event.preventDefault();

    const showMote = event.target.dataset.id;
    ctx.goTo("/details", showMote);
}

function createSearchResult(data) {
    return `<li>
<div class="card">
                <div class="card_image">
                    <img src="${data.imageUrl}" alt=""/>
                </div>
                <div class="card_content">
                    <h2 class="card_title">${data.title}</h2>
                    <center><a class="details-btn" href="details" data-id=${data.id}>Show More</a></center>
                </div>
            </div>
</li>`;
}