<template>
  <el-dialog
    :title="titleDescribe"
    :model-value="open"
    @close="cancel"
    width="80%"
    @open="initData"
  >
    <div>
      <el-form :disabled="islook" ref="form" :model="form" label-width="80px">
        <el-form-item label="用户姓名" prop="username">
          {{ form.username }}
        </el-form-item>
        <el-form-item label="权限列表">
          <el-transfer
            v-model="rightData"
            filterable
            :titles="['待选择的角色列表', '已选择的角色列表']"
            :button-texts="['到左边', '到右边']"
            :right-check-change="rightCheckChange"
            style="text-align: left; display: inline-block"
            filter-placeholder="请输入角色名称"
            :data="leftData"
          />
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

<script>
import pa from "../../api/PermissionApi";
export default {
  data() {
    return {
      form: {
        id: "",
        username: "",
        roles: [],
      },
      roles: [],
      rightData: [],
    };
  },
  computed: {
    islook() {
      return this.actionSate === 1;
    },
    //穿梭框左边数据
    leftData() {
      return this.roles.map((item) => {
        return {
          key: item.id,
          label: item.name + "-" + item.description,
        };
      });
    },
  },
  // 1查看 2 更新
  props: ["actionSate", "titleDescribe", "open", "userId"],
  emits: ["cancel", "operationComplete"],
  methods: {
    onSubmit() {
      this.form.roles = this.rightData.map(key => {
        return {
          id:key
        }
      })
      //编辑
      pa.update(this.form).then(() => {
        this.$message.success("编辑成功");
        this.sendOperationComplete();
      });
      this.sendCancelEvent()
    },
    //取消
    cancel() {
      //重置表单
      this.$refs["form"].resetFields();
      this.rightData = [];
      this.sendCancelEvent();
    },
    //发射 cancel事件
    sendCancelEvent() {
      this.$emit("cancel");
    },
    //发射 操作完成事件
    sendOperationComplete() {
      this.$emit("operationComplete");
    },
    rightCheckChange(key) {
      console.log(key);
    },
    //初始化表达数据
    initData() {
      //如果是查看或更新
      console.log("查询条件", this.userId);
      //获取角色列表
      pa.getRoles().then((roles) => {
        this.roles = roles;
        //获取用户权限详情
        pa.get(this.userId).then((user) => {
          if (user) {
            this.form.id = user.id;
            this.form.username = user.username;
            if (user.roles) {
              this.rightData = user.roles.map((item) => {
                return item.id;
              });
            }
          }
        });
      });
    },
  },
};
</script>
<style>
.el-transfer-panel {
  width: 400px;
  height: 500px;
}
.el-transfer-panel__list.is-filterable {
  height: 500px;
}
</style>