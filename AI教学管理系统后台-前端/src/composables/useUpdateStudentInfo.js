import signInApi from "@/api/signInApi.js";

export default function useUpdateStudentInfo() {
  const updateStudentInfoPopup = ref(false);
  const updateStudentInfoFormRef = ref()
  const updateStudentInfoForm = reactive({
    name: "",
    username: "",
    password: "",
    id: ""
  })
  const updateStudentInfoFormRules = reactive({
    name: [
      { required: true, message: '请输入姓名', trigger: 'blur' },
    ],
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
    ],
  });

  const initUpdateStudentInfoForm = () => {
    const info = JSON.parse(localStorage.getItem("user")).data;
    updateStudentInfoForm.name = info.name;
    updateStudentInfoForm.username = info.username;
    updateStudentInfoForm.password = info.password;
    updateStudentInfoForm.id = info.id;
  }


  const changeStudentInfo = async (formEl) => {
    if (!formEl) return
    await formEl.validate(async (valid, fields) => {
      if (valid) {
        const { data } = await signInApi.updateStudent({ ...updateStudentInfoForm })
        if (data.code === 200) {
          updateStudentInfoPopup.value = false
          ElMessage.success('修改成功，请重新登录')
          // 清除本地存储
          localStorage.removeItem("user");
          localStorage.removeItem("isSignIn");
          // 跳转到登录页面
          location.reload();
        } else {
          ElMessage.error(data.msg)
        }
      }
    })
  }

  return {
    updateStudentInfoForm,
    updateStudentInfoPopup,
    updateStudentInfoFormRef,
    updateStudentInfoFormRules,
    initUpdateStudentInfoForm,
    changeStudentInfo
  }
}
