import signInApi from "@/api/signInApi.js";

export default function useUpdateTeacherInfo() {
  const updateTeacherInfoPopup = ref(false);
  const updateTeacherInfoFormRef = ref()
  const updateTeacherInfoForm = reactive({
    name: "",
    username: "",
    password: "",
    id: ""
  })
  const updateTeacherInfoFormRules = reactive({
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

  const initUpdateTeacherInfoForm = () => {
    const info = JSON.parse(localStorage.getItem("user")).data;
    updateTeacherInfoForm.name = info.name;
    updateTeacherInfoForm.username = info.username;
    updateTeacherInfoForm.password = info.password;
    updateTeacherInfoForm.id = info.id;
  }


  const changeTeacherInfo = async (formEl) => {
    if (!formEl) return
    await formEl.validate(async (valid, fields) => {
      if (valid) {
        const { data } = await signInApi.updateTeacher({ ...updateTeacherInfoForm })
        if (data.code === 200) {
          updateTeacherInfoPopup.value = false
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
    updateTeacherInfoForm,
    updateTeacherInfoPopup,
    updateTeacherInfoFormRef,
    updateTeacherInfoFormRules,
    initUpdateTeacherInfoForm,
    changeTeacherInfo
  }
}
