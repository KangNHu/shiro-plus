<template>
  <el-select :placeholder="hint"  clearable>
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    >
    </el-option>
  </el-select>
</template>

<script>
import du from "../utils/DictUtils";
export default {
  data() {
    return {
      options: [],
      dict: {},
    };
  },
  props: {
    //提示
    hint: {
      type: String,
      default: "请选择",
    },
    //字典编码
    dictCode: {
      type: String,
      required: true,
    },
  },
  created() {
    //初始化字典数据
    this.options = du.getDictAll()[this.dictCode];
  },
};
</script>

<style>
</style>