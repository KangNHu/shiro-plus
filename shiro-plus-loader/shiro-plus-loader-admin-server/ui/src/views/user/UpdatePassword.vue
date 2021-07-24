<template>
  <el-dialog
    title="修改密码"
    :model-value="open"
    @close="cancel"
    width="40%"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="旧密码" prop="oldPassword" >
        <el-input v-model="form.oldPassword" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword" >
        <el-input v-model="form.newPassword" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码"  prop="confirmNewPassword" >
        <el-input v-model="form.confirmNewPassword" show-password></el-input>
      </el-form-item>
      <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import userApi from "../../api/UserApi";


export default {
  data() {
    return {
      form: {
        userId: "",
        oldPassword: "",
        newPassword: "",
        confirmNewPassword:"",
      },
      rules: {
        oldPassword: [
          {validator: this.checkPassword, trigger:"blur" }
        ],
        newPassword: [
          {validator: this.checkPassword, trigger:"blur"  }
        ],
        confirmNewPassword: [
          {validator: this.checkPassword, trigger:"blur"  }
        ]
      },
    };
  },
  props: ["open"],
  emits: ["cancel", "operationComplete"],
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
            console.log("confirmNewPaasword" , this.form.confirmNewPaasword)
            console.log("newPassword" ,this.form.newPassword )
            if(this.form.confirmNewPassword != this.form.newPassword){
                this.$message({
                   message: "两次输入密码不一致，请重新输入密码",
                   type: "warning"
                })
                return;
            }
             userApi.updatePassword(this.form).then(() =>{
                 this.$message.success("修改密码成功")
             })
             this.sendOperationComplete();
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
    //校验密码
    checkPassword(rule, value, callback) {
        //校验密码
        if (value === '') {
          callback(new Error("密码不能为空"));
        } else if (value.length < 6 || value.length > 16) {
          callback(new Error("密码长度不能小于6 或大于16"));
        }
        callback();
      }
  },
};
</script>