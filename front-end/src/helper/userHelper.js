function setUser(user) {
    sessionStorage.setItem("userData", JSON.stringify(user));
}

function getUser() {
    return JSON.parse(sessionStorage.getItem("userData"));
}

function getUserToken(){
    const userData = getUser();
    return userData?.accessToken;
}

function getUserId() {
    const userData = getUser();
    return userData?.id;
}

export {setUser, getUser, getUserToken, getUserId}