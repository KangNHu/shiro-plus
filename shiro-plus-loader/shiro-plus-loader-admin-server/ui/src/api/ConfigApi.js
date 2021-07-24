import request from "../utils/request"

export default{
    //全局元信息分页查询
    globalPage(pageRequest){
        return request({
            url:"/admin/config/global/page",
            method:"post",
            data:pageRequest
        })
    },
    //新增全局元信息
    addGlobal(global){
        return request({
            url:"/admin/config/global",
            method:"post",
            data:global
        })
    },
    //全局元信息详情
    getGlobal(id){
        return request({
            url:"/admin/config/global",
            method:"get",
            params:{id:id}
        })
    },
    //全局元信息删除
    deleteGlobal(id){
        return request({
            url:"/admin/config/global",
            method:"delete",
            params:{id:id}
        })
    },
    //全局元信息删除
    updateGlobal(global){
        return request({
            url:"/admin/config/global",
            method:"put",
            data:global
        });
    },
    //新增权限元数据
    addPermission(permission){
        return request({
            url:"/admin/config/permission",
            method:"post",
            data:permission
        });
    },
    //删除权限元信息
    deletePermission(id){
        return request({
            url:"/admin/config/permission",
            method:"delete",
            params:{id:id}
        });
    },
    //更新权限元数据
    updatePermission(permission){
        return request({
            url:"/admin/config/permission",
            method:"put",
            data:permission
        });
    },
    //获取权限元数据
    getPermission(id){
        return request({
            url:"/admin/config/permission",
            method:"get",
            params:{id:id}
        })
    },
    //权限元信息分页
    permissionPage(page){
        return request({
            url:"/admin/config/permission/page",
            method:"post",
            data:page
        })
    }
}