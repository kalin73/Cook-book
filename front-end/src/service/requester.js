import {getUserToken} from "../helper/userHelper.js";

async function requester(methods, url, data) {
    const option = {
        methods
    };

    const userData = getUserToken();
    const headers = {
        "Content-Type": "application/json"
    };

    if (userData) {
        headers["X-Authorization"] = userData.accessToken;
    }

    option[headers] = headers;

    if (data) {
        option.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(url, option);

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
        }

        if (response.status === 204) {
            return response;
        }

        return await response.json();
    } catch (error) {
        return alert(error.message);

    }
}

const get = (url) => {
    return requester("GET", url);
}

const post = (url, data) => {
    return requester("POST", url, data);
}

const update = (url, data) => {
    return requester("PUT", url, data);
}

const del = (url) => {
    return requester("DELETE", url);
}

export {
    get,
    post,
    update,
    del
}