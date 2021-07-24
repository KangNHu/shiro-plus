const token_head_name = "x-token";
const user_info_cache_key =  "user-info";

//保存token
function saveToken( token){
    localStorage.removeItem(token_head_name);
    localStorage.setItem(token_head_name ,token )
}
//获取token
function getToken(){
    return localStorage.getItem(token_head_name)
}
//移除用户状态
function removeUserState(){
    localStorage.removeItem(token_head_name);
    localStorage.removeItem(user_info_cache_key);
}
//设置当前用户信息
function setUserInfo(userInfo){
    localStorage.setItem(user_info_cache_key ,JSON.stringify(userInfo));
}
//获取当前用户
function getCurrentUser(){
    let userJson = localStorage.getItem(user_info_cache_key);
    if(!userJson){
        return userJson;
    }
    return JSON.parse(userJson);
}
//获取当前用户id
function getCurrentUserId(){
    let userInfo = getCurrentUser();
    return userInfo ? userInfo.id : userInfo;
}
const ut = {
    getToken,
    saveToken,
    setUserInfo,
    getCurrentUserId,
    getCurrentUser,
    removeUserState,
    token_head_name,
    user_info_cache_key
}

export default ut;