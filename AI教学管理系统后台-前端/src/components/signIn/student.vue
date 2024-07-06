<template>
  <div class="signIn-form">
    <el-form :model="loginForm">
      <el-form-item>
        <el-input placeholder="请输入账号" v-model="loginForm.username"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input placeholder="请输入密码" v-model="loginForm.password" type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="signIn-box-form-btn" @click="login">登录</el-button>
        <el-button type="primary" class="signIn-box-form-btn" @click="studentRegister = true">去注册</el-button>
      </el-form-item>
    </el-form>
  </div>


  <!-- 学生注册 -->
  <el-dialog v-model="studentRegister" title="注册账号" width="500">
    <el-form :model="registerForm" ref="registerFormRef" :rules="rules" class="box-border p-20px">
      <el-form-item prop="name" label="学生姓名" label-width="100px">
        <el-input placeholder="请输入您的姓名" v-model="registerForm.name"></el-input>
      </el-form-item>

      <el-form-item prop="username" label="账号" label-width="100px">
        <el-input placeholder="请输入您的账号" v-model="registerForm.username"></el-input>
      </el-form-item>

      <el-form-item prop="password" label="密码" label-width="100px">
        <el-input placeholder="请输入您的密码" v-model="registerForm.password"></el-input>
      </el-form-item>

      <el-form-item prop="sex" label="性别" label-width="100px">
        <el-radio-group v-model="registerForm.sex">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="2">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item class="but">
        <el-button type="primary" class="signIn-box-form-btn" @click="register(registerFormRef)">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>


<script setup>
import signInApi from "@/api/signInApi.js";


const loginForm = reactive({
  username: "",
  password: "",
})


const studentRegister = ref(false);
const registerForm = reactive({
  name: "",
  username: "",
  password: "",
  sex: ""
})
const registerFormRef = ref()

const router = useRouter()

const rules = reactive({
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

})

const register = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      const { data } = await signInApi.studentRegister({ ...registerForm })
      if (data.code === 200) {
        loginForm.username = registerForm.username
        loginForm.password = registerForm.password
        studentRegister.value = false
        ElMessage.success('注册成功')
      } else {
        ElMessage.error(data.msg)
      }
    }
  })
}

const login = async () => {
  const { data } = await signInApi.studentLogin({ ...loginForm })
  if (data.code === 200) {
    ElMessage.success('登录成功')
    localStorage.setItem('isSignIn', 'true')
    localStorage.setItem("user", JSON.stringify(data.data))
    await router.push({
      name: 'studentCourse1'
    })
  } else {
    ElMessage.error(data.msg)
  }
}
</script>


<style lang="sass" scoped>
.but
  :deep(.el-form-item__content)
    justify-content: center
</style>
