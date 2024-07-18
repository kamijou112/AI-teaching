<template>
  <div class="signIn-form">
    <el-form :model="formData" :rules="rules" ref="ruleFormRef">
      <el-form-item prop="username">
        <el-input placeholder="请输入教师账号" v-model="formData.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input :type="passwordFieldType" placeholder="请输入教师账号密码" v-model="formData.password">
          <template #suffix>
            <img :src="passwordFieldType === 'password' ? eyeOffIcon : eyeIcon" class="icon" @click="togglePasswordVisibility" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="signIn-box-form-btn" @click="signIn(ruleFormRef)">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import signInApi from "@/api/signInApi.js";
import { ElMessage } from 'element-plus';
import eyeIcon from '@/assets/icons/eye.svg';
import eyeOffIcon from '@/assets/icons/eye-off.svg';

const router = useRouter();
const ruleFormRef = ref();
const formData = reactive({
  username: '',
  password: '',
});
const rules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
});

const passwordFieldType = ref('password');

const togglePasswordVisibility = () => {
  passwordFieldType.value = passwordFieldType.value === 'password' ? 'text' : 'password';
};

const signIn = async (formEl) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      const { data } = await signInApi.login({ ...formData });

      if (data.code === 200) {
        localStorage.setItem('isSignIn', 'true');
        localStorage.setItem("user", JSON.stringify(data.data));
        await router.push({
          name: 'myStudents'
        });
        ElMessage.success('登录成功');
      } else {
        ElMessage.error(data.msg);
      }
    } else {
      ElMessage.error('请填写账号和密码');
    }
  });
};
</script>

<style scoped>
.el-input__suffix {
  color: var(--el-input-icon-color, #000); /* 确保图标颜色可见 */
  display: inline-flex;
  flex-shrink: 0;
  flex-wrap: nowrap;
  height: 100%;
  pointer-events: auto; /* 确保图标可交互 */
  text-align: center;
  transition: all var(--el-transition-duration);
  white-space: nowrap;
  visibility: visible; /* 确保图标可见 */
}

.icon {
  width: 16px; /* 设置图标宽度 */
  height: 16px; /* 设置图标高度 */
  cursor: pointer; /* 确保图标可点击 */
}
</style>
