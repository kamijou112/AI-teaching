<template>
  <C_canavs></C_canavs>
  <div class="signIn">
    <div class="signIn-box">
      <h2 class="signIn-box-title">后台登录系统</h2>
      <el-tabs class="signIn-box-form" v-model="activeName">
        <el-tab-pane label="我是学生" name="first">
          <student />
        </el-tab-pane>
        <el-tab-pane label="我是老师" name="second">
          <teacher />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import C_canavs from "@/components/ui/C_canvas.vue";
import teacher from "@/components/signIn/teacher.vue";
import student from "@/components/signIn/student.vue";

const activeName = ref('first')
</script>

<style scoped lang="scss">
@import "@/sass/signin/index"
</style>
