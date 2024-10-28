function setUserData(user, jwtToken) {
    sessionStorage.setItem("userData", JSON.stringify(user));
    sessionStorage.setItem("jwtToken", JSON.stringify(jwtToken));
}

function getUser() {
    return JSON.parse(sessionStorage.getItem("userData"));
}

function getJwtToken() {
    return JSON.parse(sessionStorage.getItem("jwtToken"));
}

function getUserToken() {
    return getJwtToken();
}

function getUserId() {
    const userData = getUser();
    return userData?.id;
}

export {setUserData, getUser, getUserToken, getUserId}