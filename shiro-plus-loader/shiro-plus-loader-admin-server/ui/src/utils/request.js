import axios from 'axios';
import message  from 'element-plus/lib/el-message';
import router from '../router';
import ut from '../utils/userUtils';
import bc from "../api/BusinessCode"
const service = axios.create({

    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    // easy-mock服务挂了，暂时不使用了
    baseURL: '.',
    timeout: 5000
});
//用户动态获取请求头
service.headersCall = () => {
    return {};
}
service.interceptors.request.use(
    config => {
        //添加token
        config.headers[ut.token_head_name] = ut.getToken();
        //设置业务码
        bc.handBusinesseCode(config);
        return config;
    },
    error => {
        message.error(error.message);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    response => {
            let reuslt =  response.data
            // 返回响应对象失败时
            if(!reuslt | !reuslt.succeed){
                console.log("全局处理" +reuslt);
                message({
                    message: reuslt ? reuslt.msg : "未获取到任何数据",
                    type: "warning"
                })
                return Promise.reject();
            }
            //成功响应数据时
            else{
                return reuslt.data;    
            }
       
       
    },
    error => {
        let resp = error.response;
        if(!resp){
            message.error(error.message);
            return Promise.reject(); 
        }
        let reuslt = resp.data;
        //重定向
        if(resp.status === 302){
            document.location = resp.headers["Location"];
        }
        //鉴权失败
        else if(resp.status === 401){
            //如果有提示信息则弹出提示信息
            if(reuslt.msg){
                message({
                    message: reuslt.msg,
                    type: "warning"
                });
             }
             //清空用户状态
             ut.removeUserState();
             //跳转到登录页
             router.push("/login");
            
        }
        //找不到页面
        else if(resp.status === 404){
            router.push("/404");
        }
         //授权失败
        else if(resp.status === 403){
            router.push("/403");
        }
        //服务器错误
        else if(resp.status >= 500 && resp.status < 600){
            message.error(reuslt ? reuslt.msg : error.message);
        }
        //其他警告
        else{
            message({
                message: reuslt ? reuslt.msg : error.message,
                type: "warning"
            })
        }
        return Promise.reject();
    }
);



export default service
