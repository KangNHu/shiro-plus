<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 全局元数据管理
        </el-breadcrumb-item>
        <el-breadcrumb-item>{{ titleDescribe }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="form-box">
        <el-form ref="form" :model="form" label-width="230px" :rules="rules">
          <el-form-item label="租户Id" prop="tenantId">
            <el-input
              autosize
              placeholder="请输入租户Id"
              v-model="form.tenantId"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="是否开启鉴权" prop="enableAuthentication">
            <el-switch
              v-model="form.enableAuthentication"
              :active-value="1"
              :inactive-value="0"
              active-text="开启"
              inactive-text="关闭"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="是否开启授权" prop="enableAuthorization">
            <el-switch
              v-model="form.enableAuthorization"
              :active-value="1"
              :inactive-value="0"
              active-text="开启"
              inactive-text="关闭"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="接口白名单" prop="anons">
            <dynamic-input
              v-model:values="form.anons"
              placeholder="请输入白名单规则"
              :di-disabled="islook"
              @change="values => form.anons = values"
            />
          </el-form-item>
          <el-form-item label="扩展字段" prop="extend">
            <dynamic-map-input
              v-model:values="form.extend"
              key-placeholder="key"
              value-placeholder="value"
              :dm-disabled="islook"
              @change="values => form.extend = values"
            />
          </el-form-item>
          <el-form-item>
            <el-button v-if="!islook" type="primary" @click="onSubmit"
              >表单提交</el-button
            >
            <el-button @click="cancel">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import ca from "../../../api/ConfigApi";
import ou from "../../../utils/ObjectUtils";
import DynamicInput from "../../../components/DynamicInput.vue";
import DynamicMapInput from "../../../components/DynamicMapInput.vue";
export default {
  data() {
    return {
      form: {
        id: "",
        tenantId: "",
        enableAuthentication: '',
        enableAuthorization: '',
        anons: [],
        extend: [
          
        ],
      },
      rules: {
        tenantId: [
          { required: true, message: "租户ID不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    // 1 为新增 2为更新 3 查看
    if (this.action != 1) {
      this.getData();
    }
  },
  computed: {
    action() {
      return this.$route.params.action;
    },
    id() {
      return this.$route.params.id;
    },
    //是否查看操作
    islook() {
      return this.$route.params.action == 3;
    },
    titleDescribe() {
      switch (this.action) {
        case "1":
          return "新增";
        case "2":
          return "更新";
        case "3":
          return "查看";
      }
      return "";
    },
  },
  components: {
    DynamicInput,
    DynamicMapInput,
  },
  methods: {
    //详情 获取系统配置信息
    getData() {
      ca.getGlobal(this.id).then((global) => {
        if (global) {
          if (global.anons) {
            global.anons = global.anons.split(",");
          }
          if (global.extend) {
            let extend = [];
            let i = 0;
            for (let key in global.extend) {
              extend[i] = {
                key: key,
                value: global.extend[key],
              };
              i++;
            }
            global.extend = extend;
          }
          this.form = global;
          this.form.enableAuthentication = global.enableAuthentication;
          this.form.enableAuthorization = global.enableAuthorization;
        }
      });
    },
    //关闭当前窗口
    cancel() {
      this.$store.commit("closeCurrentTag", this);
      this.$store.push("/globalMetadata");
    },
    //提交
    onSubmit() {
      console.log(this.form.extend);
      this.$refs.form.validate((vali) => {
        if (vali) {
          let global = ou.copyObject(this.form, (key, value) => {
            if (key === "anons") {
              return value.join(",");
            }
            if (key === "extend") {
              let extend = {};
              value.forEach((item) => {
                extend[item.key] = item.value;
              });
              return extend;
            }
            return value;
          });
          //新增
          if (this.action == 1) {
            ca.addGlobal(global).then(() => {
              this.$message.success("新增成功");
              this.$router.push("/globalMetadata");
            });
          }
          //编辑
          else if (this.action == 2) {
            ca.updateGlobal(global).then(() => {
              this.$message.success("编辑成功");
              this.$router.push("/globalMetadata");
            });
          }
        }
      });
    }
  },
};
</script>
<style scoped>
</style>