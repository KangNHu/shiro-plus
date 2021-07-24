<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 系统设置
        </el-breadcrumb-item>
        <el-breadcrumb-item>全局配置</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="form-box">
        <el-form ref="form" :model="form" label-width="230px" :rules="rules">
          <el-form-item label="系统版本">
            {{ form.version }}
          </el-form-item>
          <el-form-item label="心跳的最大时间" prop="heartbeatMaxTime">
            <el-input-number
              controls-position="right"
              v-model="form.heartbeatMaxTime"
            ></el-input-number>
            <span>（单位毫秒）</span>
            <el-tooltip effect="dark" content="大于这个时间将自动清理客服端实例" placement="right-start">
              <span class="el-icon-info" ></span>
            </el-tooltip>
          </el-form-item>
          <el-form-item label="token签名密钥(影响本系统登录)" prop="keyPair">
            <el-input
              type="textarea"
              autosize
              placeholder="请输入token签名密钥"
              v-model="form.keyPair"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="客户端token" prop="clientToken">
            <el-input
              placeholder="请输入客户端token"
              v-model="form.clientToken"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="系统初始密码" prop="initPassword">
            <el-input
              placeholder="请输入系统初始密码"
              v-model="form.initPassword"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="登录有效时间" prop="loginValidTime">
            <el-input-number
              controls-position="right"
              v-model="form.loginValidTime"
            ></el-input-number>
            <span>（小时）</span>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">表单提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import sa from "../../api/SystemApi";

export default {
  data() {
    return {
      form: {
        version: "1.0.0",
        heartbeatMaxTime: 0,
        keyPair: "{xxxxxxxxx}",
        clientToken: "123456",
        loginValidTime: 2,
        initPassword:''
      },
      rules: {
        heartbeatMaxTime: [
          { required: true, message: "心跳的最大时间不能为空", trigger: "blur" },
        ],
         keyPair: [
          { required: true, message: "token签名密钥不能为空", trigger: "blur" },
        ],
        clientToken: [
          { required: true, message: "客户端token不能为空", trigger: "blur" },
        ],
         loginValidTime: [
          { required: true, message: "登录有效时间不能为空", trigger: "blur" },
        ],
        initPassword:[
            {required: true, message: "系统初始密码不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getData();
  },
  methods: {
    //详情 获取系统配置信息  
    getData() {
      sa.getSystemInfo().then((systemInfo) => {
        this.form = systemInfo;
        this.form.eventTime = this.form.eventTime / 3600000; // 后台为毫秒 -> 小时
        this.form.loginValidTime = this.form.loginValidTime / 60; // 后台为分钟 -> 小时
      });
    },
    //提交
    onSubmit() {
      this.$refs.form.validate((vali) => {
        if (vali) {
            let systemUpdate = {};
            for(let key in this.form){
                systemUpdate[key] = this.form[key];     
            }
            if(systemUpdate.eventTime){
                systemUpdate.eventTime = this.form.eventTime * 3600000;
            }
            if(systemUpdate.loginValidTime){
                systemUpdate.loginValidTime = this.form.loginValidTime * 60;
            }
            sa.update(systemUpdate).then(() =>{
                this.$message.success("编辑成功");
                this.getData();
            }) 
        }
      });
    },
  },
};
</script>