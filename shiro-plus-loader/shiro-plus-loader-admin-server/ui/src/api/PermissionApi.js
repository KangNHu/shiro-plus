import request from "../utils/request"


export default{
    //获取系统信息
    page(pageRequest){
        return request({
            url:"/admin/permission/page",
            method:"post",
            data:pageRequest
        })
    },
    //获取角色列表
    getRoles(){
        return request({
            url:"/admin/permission/getRoles",
            method:"get"
        })
    },
    //详情
    get(userId){
        return request({
            url:"/admin/permission",
            method:"get",
            params:{userId : userId}
        })
    },
    //更新系统信息
    update(systemInfo){
        return request({
            url:"/admin/permission",
            method:"put",
            data:systemInfo
        })
    }
}