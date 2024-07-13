<template>
  <C_canvas></C_canvas>
  <div class="signIn">
    <div class="signIn-box">
      <div class="logo-and-title">
        <img src="@/assets/logo.png" alt="Logo" class="logo">
        <h2 class="signIn-box-title">数智化教育系统</h2>
      </div>
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
import C_canvas from "@/components/ui/C_canvas.vue";
import teacher from "@/components/signIn/teacher.vue";
import student from "@/components/signIn/student.vue";

const activeName = ref('first')
</script>

<style scoped lang="scss">
@import "@/sass/signin/index";

.signIn {
  opacity: 0.95; /* Adjust the opacity level as needed */
}

.signIn-box {
  display: flex;
  align-items: center;
  flex-direction: column;
  
  .logo-and-title {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .logo {
    width: 80px; /* Adjusted size */
    margin-right: 30px;
  }

  .signIn-box-title {
    margin: 0; /* Remove default margin if any */
  }
}
</style>
