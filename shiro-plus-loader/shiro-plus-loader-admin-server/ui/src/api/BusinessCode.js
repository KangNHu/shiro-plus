/**
 * 日志业务码常量
 */
const business_code_head = "x-business-code";
//用户登录
const user_businesse_code =1;
//系统配置管理
const system_businesse_code = 2;
//权限管理
const permission_businesse_code  =3;
//全局元信息配置管理
const global_metadata_businesse_code = 4;
//权限元信息管理
const permission_metadata_businesse_code = 6;

//open api管理
const open_api_businesse_code = 7;
//登录
const login_businesse_code =5;

const dict = []

dict[5]={value:permission_metadata_businesse_code,label:"权限元信息配置管理"}
dict[4]={value:global_metadata_businesse_code,label:"全局元信息配置管理"}
dict[3]={value:system_businesse_code,label:"系统配置管理"}
dict[2]={value:open_api_businesse_code,label:"API管理"}
dict[1]={value:permission_businesse_code,label:"权限管理"}
dict[0]={value:user_businesse_code,label:"用户管理"}
const mapping = {
    "/login":login_businesse_code,
    "/admin/config/global" : global_metadata_businesse_code,
    "/admin/user":user_businesse_code,
    "/admin/permission":permission_businesse_code,
    "/admin/system":system_businesse_code,
    "/admin/config/permission":permission_metadata_businesse_code,
    "/admin/config/api":open_api_businesse_code
}
/**
 *
 * @param {*} config  请求对象配置
 */
function  handBusinesseCode(config){
    let businesseCode;
    for(let key in mapping){
        if(config.url.startsWith(key)){
            businesseCode = mapping[key];
            break;
        }
    }
    config.headers[business_code_head] = businesseCode;
}
export default{
    dict,
    handBusinesseCode

}