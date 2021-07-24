
import bc from "../api/BusinessCode"

const dictAll = {};

//初始化数据
dictAll['business_code'] =  bc.dict;
let dicts = localStorage.getItem('dicts');
if(dicts){
    dicts = JSON.parse(dicts);
}
for(let key in dicts){
    dictAll[key] = dicts[key];
}

/**
 * 获取字典 label
 * @param {*} code  字典编码
 * @param {*} key  字典value
 */
function getDictValue(code , key){
    if(!code || !key){
        return ""
    }
    let dictList = dictAll[code]
    if(!dictList){
        return "";
    }
    let dict = dictList.find(item =>{
       return item.value == key
    });
    return dict ? dict.label : dict;
}

/**
 * 获取所有字典数据
 */
function getDictAll(){
    return dictAll;
}

export default{
    getDictValue,
    getDictAll
}