import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";
import ut from "../utils/userUtils"

const routes = [
    {
        path: '/',
        redirect: '/dashboard'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashboard",
                name: "dashboard",
                meta: {
                    title: '系统首页'
                },
                component: () => import (
                /* webpackChunkName: "dashboard" */
                "../views/Dashboard.vue")
            },{
                path: "/user",
                name: "user",
                meta: {
                    title: '用户管理'
                },
                component: () => import (
                /* webpackChunkName: "table" */
                "../views/user/Page.vue")
            },{
                path: "/permission",
                name: "permission",
                meta: {
                    title: '权限管理'
                },
                component: () => import (
                /* webpackChunkName: "table" */
                "../views/permission/Page.vue")
            },{
                path: "/globalSettings",
                name: "globalSettings",
                meta: {
                    title: '系统全局配置'
                },
                component: () => import (
                /* webpackChunkName: "table" */
                "../views/system/GlobalSetting.vue")
            },{
                path: "/instancePage",
                name: "instancePage",
                meta: {
                    title: '实例管理'
                },
                component: () => import (
                /* webpackChunkName: "table" */
                "../views/system/InstancePage.vue")
            },{
                path: "/globalMetadata",
                name: "globalMetadata",
                meta: {
                    title: '全局元数据管理'
                },
                component: () => import (
                /* webpackChunkName: "charts" */
                "../views/config/global/Page.vue")
            },{
                path: "/globalMetadata/addOrUpdate",
                name: "globalMetadata-addOrUpdate",
                meta: {
                    title: '全局元数据管理-新增或编辑'
                },
                component: () => import (
                /* webpackChunkName: "charts" */
                "../views/config/global/AddOrUpdate.vue")
            }, {
                path: "/permissionMetadata",
                name: "permissionMetadata",
                meta: {
                    title: '权限元数据管理'
                },
                component: () => import (
                /* webpackChunkName: "form" */
                "../views/config/permission/Page.vue")
            }, {
                path: "/permissionMetadata/addOrUpdate",
                name: "permissionMetadata-addOrUpdate",
                meta: {
                    title: '权限元数据管理-新增或编辑'
                },
                component: () => import (
                /* webpackChunkName: "form" */
                "../views/config/permission/AddOrUpdate.vue")
            }, {
                path: '/404',
                name: '404',
                meta: {
                    title: '找不到页面'
                },
                component: () => import (/* webpackChunkName: "404" */
                '../views/404.vue')
            }, {
                path: '/403',
                name: '403',
                meta: {
                    title: '没有权限'
                },
                component: () => import (/* webpackChunkName: "403" */
                '../views/403.vue')
            }
        ]
    }, {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import (
        /* webpackChunkName: "login" */
        "../views/Login.vue")
    }
];

const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | shiro plus`;
    const token = ut.getToken();
    //如果是登录状态 且方法登录页，则跳转到home
    if(token && to.path == '/login'){
        next("/")
        return;
    }
    //如果访问登录页且用户并没有登录
    if(to.path == '/login' && !token){
        next();
        return;
    }
    //如果登录失效
    if(!token){
        //设置回调url
        to.call_url = from.path;
        next('/login');
        return;
    }
    next();
});

export default router;