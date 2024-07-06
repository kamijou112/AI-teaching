<template>
  <div class="signIn-form">
    <el-form :model="formData" :rules="rules" ref="ruleFormRef">
      <el-form-item prop="username">
        <el-input placeholder="请输入教师账号" v-model="formData.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input placeholder="请输入教师账号密码" v-model="formData.password" type="password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="signIn-box-form-btn" @click="signIn(ruleFormRef)">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import signInApi from "@/api/signInApi.js";

const router = useRouter()
const ruleFormRef = ref();
const formData = reactive({
  username: '',
  password: '',
})
const rules = reactive({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
  ],
})



const signIn = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      const { data } = await signInApi.login({ ...formData })

      if (data.code === 200) {
        localStorage.setItem('isSignIn', 'true')
        localStorage.setItem("user", JSON.stringify(data.data))
        await router.push({
          name: 'myStudents'
        })
        ElMessage.success('登录成功')
      } else {
        ElMessage.error(data.msg)
      }
    } else {
      ElMessage.error('请填写账号和密码')
    }
  })
}


</script>


<style scoped lang="sass"></style>
