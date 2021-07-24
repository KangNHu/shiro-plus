<template>
  <el-dialog
    :title="titleDescribe"
    :model-value="open"
    @close="cancel"
    width="40%"
    @open="initData"
  >
    <div>
      <el-form
        :disabled="islook"
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户昵称" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="用户姓名" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item v-show="isShowPassword" prop="password" label="密码">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col span="6">
          <el-button v-show="!islook" type="primary" @click="onSubmit"
            >提交</el-button
          >
          <div class="grid-content bg-purple"></div>
        </el-col>
        <el-col span="6">
          <el-button @click="cancel">取消</el-button>
          <div class="grid-content bg-purple"></div>
        </el-col>
      </el-row>
    </div>
  </el-dialog>
</template>

<script >
import userApi from "../../api/UserApi";
export default {
  data() {
    return {
      form: {
        id: "",
        nickname: "",
        username: "",
        password: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        nickname: [
          { required: true, message: "请求输入昵称", trigger: "blur" },
        ],
        password: [{ validator: this.checkPassword, trigger: "blur" }],
      },
    };
  },
  computed: {
    islook() {
      return this.actionSate === 2;
    },
    isShowPassword() {
      return this.actionSate === 1;
    },
  },
  // 1 新增 2 查看 3 更新
  props: ["actionSate", "titleDescribe", "open", "userId"],
  emits: ["cancel", "operationComplete"],
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          //新增
          if (this.actionSate === 1) {
            userApi.add(this.form).then(() => {
              this.$message.success("新增成功");
              this.sendOperationComplete();
            });
          }
          //编辑
          if (this.actionSate === 3) {
            userApi.update(this.form).then(() => {
              this.$message.success("编辑成功");
              this.sendOperationComplete();
            });
          }
         
        }
      });
    },
    //取消
    cancel() {
      //重置表单
      this.$refs["form"].resetFields();
      this.sendCancelEvent();
    },
    //发射 cancel事件
    sendCancelEvent() {
      this.$emit("cancel");
    },
    //发射 操作完成事件
    sendOperationComplete() {
      this.sendCancelEvent();
      this.$emit("operationComplete");
    },
    //初始化表达数据
    initData() {
      //如果是查看或更新
      if (this.actionSate == 2 || this.actionSate == 3) {
        console.log("查询条件", this.userId);
        userApi.get(this.userId).then((user) => {
          if (user) {
            this.form.id = user.id;
            this.form.username = user.username;
            this.form.nickname = user.nickname;
          }
        });
      }
    },
    //校验密码
     checkPassword(rule, value, callback) {
        //为新增的时校验密码
        if (this.actionSate == 1) {
          if (value === '') {
            callback(new Error("密码不能为空"));
          } else if (value.length < 6 || value.length > 16) {
            callback(new Error("密码长度不能小于6 或大于16"));
          }
        }
         callback();
      }
  },
};
</script>