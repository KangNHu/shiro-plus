<template>
    <div class="about" v-if="init">
        <v-header />
        <v-sidebar />
        <div class="content-box" :class="{ 'content-collapse': collapse }">
            <v-tags></v-tags>
            <div class="content">
                <router-view v-slot="{ Component }">
                    <transition name="move" mode="out-in">
                        <keep-alive :include="tagsList">
                            <component :is="Component" />
                        </keep-alive>
                    </transition>
                </router-view>
                <!-- <el-backtop target=".content"></el-backtop> -->
            </div>
        </div>
    </div>
</template>
<script>
import vHeader from "../components/Header";
import vSidebar from "../components/Sidebar";
import vTags from "../components/Tags.vue";
import ua from "../api/UserApi"
import sa from "../api/SystemApi"
import ut from "../utils/userUtils"
export default {
    data(){
        return{
            init:false,
            count:0,
            initCount:2
        }
    },
    components: {
        vHeader,
        vSidebar,
        vTags
    },
    beforeCreate(){
         const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        //获取当前用户信息
        ua.getCurrentUser().then(user =>{
            ut.setUserInfo(user)
            this.checkInit();
        })
        //获取字典数据
        sa.getDicts().then(dicts =>{
            if(dicts){
                localStorage.setItem("dicts" , JSON.stringify(dicts))
            }
            this.checkInit();
        })
        let id = setInterval(() =>{
             if(this.init){
                loading.close();
                clearInterval(id);
             }
        } , 1000)
    },
    computed: {
        tagsList() {
            return this.$store.state.tagsList.map(item => item.name);
        },
        collapse() {
            return this.$store.state.collapse;
        }
    },
    methods:{
        checkInit(){
            this.count++;
            if(this.count == this.initCount){
                this.init = true;
            }
        }
    }
};
</script>
