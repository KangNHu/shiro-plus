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
          <el-form-item label="智能搜索URL" v-if="!islook">
            <el-autocomplete
              clearable
              filterable
              remote
              v-model="queryApiSelectResult.path"
              placeholder="请输入接口URL"
              :popper-append-to-body="false"
              popper-class="my-kqoption"
              :fetch-suggestions="queryPath"
              @select="updateApiData"
            >
              <template #default="{ item }">
                <div>
                  {{
                    "[" +
                    du.getDictValue("method_code", item.method) +
                    "]" +
                    item.path
                  }}
                </div>
                <span>{{ item.summary }}</span>
              </template>
            </el-autocomplete>
          </el-form-item>
          <el-form-item label="接口URL" prop="path">
            <el-input
              autosize
              placeholder="请输入接口URL"
              v-model="form.path"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="请求方式" prop="method">
            <dict-select v-model="form.method" dict-code="method_code" />
          </el-form-item>
          <el-form-item label="权限标识列表" prop="permis">
            <dynamic-input
              v-model:values="form.permis"
              :di-disabled="islook"
              :required="true"
              placeholder="请输入权限标识"
              @change="(values) => (form.permis = values)"
            />
          </el-form-item>
          <el-form-item label="逻辑符" prop="logical">
            <dict-select
              dict-code="logical_code"
              v-model="form.logical"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="授权模式" prop="permiModel">
            <dict-select
              dict-code="permission_model_code"
              v-model="form.permiModel"
              :disabled="islook"
            />
          </el-form-item>
          <el-form-item label="扩展字段">
            <dynamic-map-input
              v-model:values="form.extend"
              key-placeholder="key"
              value-placeholder="value"
              :dm-disabled="islook"
              @change="(values) => (form.extend = values)"
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
import DictSelect from "../../../components/DictSelect.vue";
import DynamicInput from "../../../components/DynamicInput.vue";
import DynamicMapInput from "../../../components/DynamicMapInput.vue";
import du from "../../../utils/DictUtils";
export default {
  data() {
    return {
      form: {
        id: "",
        path: "",
        method: 1,
        permis: [],
        logical: 1,
        permiModel: 1,
        extend: [],
      },
      du: du,
      apiSummary: "",
      queryApiResult: [], //搜索的所有结果,
      queryApiSelectResult: {},
      rules: {
        path: [{ required: true, message: "接口URL不能为空", trigger: "blur" }],
        method: [
          { required: true, message: "请求方式不能为空", trigger: "blur" },
        ],
        permis: [
          {
            validator: (rule, value, callback) =>{
                if(!value || value.length == 0){
                  callback(new Error("权限标识不能为空或者空字符串"));
                  return;
                }
                for(let item in value){
                  if(item){
                    callback()
                    return;
                  }
                }
                callback(new Error("权限标识不能为空或者空字符串"));
            },
            trigger:"blur"
          },
        ],
        logical: [
          { required: true, message: "逻辑符不能为空", trigger: "blur" },
        ],
        permiModel: [
          { required: true, message: "授权模式不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    // 1 为新增 2为更新 3 查看 ,4 模版式新增
    if (this.action != 1) {
      this.getData();
    }

    if (this.action == 4) {
      //设置模版
      this.form.path = this.$route.params.path;
      this.form.logical = +this.$route.params.logical;
      this.form.permiModel = +this.$route.params.permiModel;
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
  },
  components: {
    DictSelect,
    DynamicInput,
    DynamicMapInput,
  },
  methods: {
    //详情 获取系统配置信息
    getData() {
      ca.getPermission(this.id).then((permi) => {
        if (permi) {
          permi.permis = permi.permis.split(",");
          if (permi.extend) {
            let extend = [];
            let i = 0;
            for (let key in permi.extend) {
              extend[i] = {
                key: key,
                value: permi.extend[key],
              };
              i++;
            }
            permi.extend = extend;
          }
          this.form = permi;
        }
      });
    },
    queryPath(query, cb) {
      if (!query || query == "") {
        return;
      }
      ca.queryOpenApi(query).then((apis) => {
        cb(apis);
      });
    },
    //同步api数据
    updateApiData(value) {
      if (value === "") {
        return;
      }
      this.form.path = value.path;
      this.form.method = value.method;
      this.queryApiSelectResult = value;
    },
    //关闭当前窗口
    cancel() {
      this.$store.commit("closeCurrentTag", this);
      this.$store.push("/permissionMetadata");
    },
    //提交
    onSubmit() {
      console.log(this.form.extend);
      this.$refs.form.validate((vali) => {
        if (vali) {
          let permi = ou.copyObject(this.form, (key, value) => {
            if (key === "permis") {
              return value.filter(item => item&&item.length > 0).join(",");
            }
            if (key === "extend") {
              let extend = {};
              value.filter(item => item.key&&item.key.length>0).forEach((item) => {
                extend[item.key] = item.value;
              });
              return extend;
            }
            return value;
          });
          //新增
          if (this.action == 1 || this.action == 4) {
            ca.addPermission(permi).then(() => {
              this.$message.success("新增成功");
              this.$router.push("/permissionMetadata");
            });
          }
          //编辑
          else if (this.action == 2) {
            ca.updatePermission(permi).then(() => {
              this.$message.success("编辑成功");
              this.$router.push("/permissionMetadata");
            });
          }
        }
      });
    },
  },
};
</script>
<style scoped>
::v-deep  .my-kqoption {
       width: 500px;
}
::v-deep .my-kqoption   .el-autocomplete-suggestion__list{
       width: 500px;
}
::v-deep .my-kqoption .el-autocomplete-suggestion__wrap{
  width: 500px;
}
::v-deep .my-kqoption .el-scrollbar{
   width: 500px;
}
</style>