
/**
 * 拷贝对象
 * @param {*} obj1  待拷贝对象
 * @param {*} call  字段回调 用于处理特殊字段
 */
function copyObject(obj1 ,call){
    let obj2 = {};
    if(!obj1){
        return obj2;
    }
    for(let key in obj1){
       let value = obj1[key];  
        if(call){
            value = call(key ,obj1[key]);
        }
        obj2[key] = value;
    }
    return obj2;
}



export default{
    copyObject
}