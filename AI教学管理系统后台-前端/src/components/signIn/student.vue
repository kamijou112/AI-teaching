<template>
  <div class="signIn-form">
    <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
      <el-form-item prop="username">
        <el-input placeholder="请输入账号" v-model="loginForm.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input :type="passwordFieldType" placeholder="请输入密码" v-model="loginForm.password">
          <template #suffix>
            <img :src="passwordFieldType === 'password' ? eyeOffIcon : eyeIcon" class="icon" @click="togglePasswordVisibility" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="signIn-box-form-btn" @click="login">登录</el-button>
        <span class="register-link" @click="studentRegister = true">没有账号？去注册</span>
      </el-form-item>
    </el-form>
  </div>
  <!-- 学生注册 -->
  <el-dialog v-model="studentRegister" title="注册账号" width="500">
    <el-form :model="registerForm" ref="registerFormRef" :rules="registerRules" class="box-border p-20px">
      <el-form-item prop="name" label="学生姓名" label-width="100px">
        <el-input placeholder="请输入您的姓名" v-model="registerForm.name"></el-input>
      </el-form-item>
      <el-form-item prop="username" label="账号" label-width="100px">
        <el-input placeholder="请输入您的账号" v-model="registerForm.username"></el-input>
      </el-form-item>
      <el-form-item prop="password" label="密码" label-width="100px">
        <el-input :type="passwordFieldType" placeholder="请输入您的密码" v-model="registerForm.password">
          <template #suffix>
            <img :src="passwordFieldType === 'password' ? eyeOffIcon : eyeIcon" class="icon" @click="togglePasswordVisibility" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="sex" label="性别" label-width="100px">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div class="dialog-footer">
      <el-button type="primary" @click="register(registerFormRef)">注册</el-button>
    </div>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import signInApi from "@/api/signInApi.js";
import { ElMessage } from 'element-plus';
import eyeIcon from '@/assets/icons/eye.svg';
import eyeOffIcon from '@/assets/icons/eye-off.svg';

const loginForm = reactive({
  username: "",
  password: "",
});

const studentRegister = ref(false);
const registerForm = reactive({
  name: "",
  username: "",
  password: "",
  sex: ""
});
const registerFormRef = ref();
const loginFormRef = ref();

const router = useRouter();

const loginRules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ]
});

const registerRules = reactive({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
  ],
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
  sex: [
    { required: true, message: '请选择您的性别', trigger: 'blur' },
  ],
});

const passwordFieldType = ref('password');

const togglePasswordVisibility = () => {
  passwordFieldType.value = passwordFieldType.value === 'password' ? 'text' : 'password';
};

const register = async (formEl) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      const { data } = await signInApi.studentRegister({ ...registerForm });
      if (data.code === 200) {
        loginForm.username = registerForm.username;
        loginForm.password = registerForm.password;
        studentRegister.value = false;
        ElMessage.success('注册成功');
      } else {
        ElMessage.error(data.msg);
      }
    } else {
      console.log('Validation failed:', fields);
    }
  });
};

// const login = async () => {
//   await loginFormRef.value.validate(async (valid, fields) => {
//     if (valid) {
//       const { data } = await signInApi.studentLogin({ ...loginForm });
//       if (data.code === 200) {
//         ElMessage.success('登录成功');
//         localStorage.setItem('isSignIn', 'true');
//         localStorage.setItem("user", JSON.stringify(data.data));
//         await router.push({
//           name: 'studentCourse1'
//         });
//       } else {
//         ElMessage.error(data.msg);
//       }
//     } else {
//       console.log('Validation failed:', fields);
//     }
//   });
// };
const login = async () => {
  await loginFormRef.value.validate(async (valid, fields) => {
    if (valid) {
      const { data } = await signInApi.studentLogin({ ...loginForm });
      // 打印返回的data到控制台
      console.log('Received data:', data);
      
      if (data.code === 200) {
        ElMessage.success('登录成功');
        localStorage.setItem('isSignIn', 'true');
        localStorage.setItem("user", JSON.stringify(data.data));
        await router.push({
          name: 'studentCourse1'
        });
      } else {
        ElMessage.error(data.msg);
      }
    } else {
      console.log('Validation failed:', fields);
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

.register-link {
  color: #409EFF; /* or any color you prefer */
  cursor: pointer;
  margin-left: 10px;
}

.dialog-footer {
  text-align: right;
  padding: 1px 0;
}
</style>
