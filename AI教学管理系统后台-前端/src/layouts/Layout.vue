<template>
  <div class="layout">
    <!-- 侧边栏内容 -->
    <aside class="sidebar">
      <el-col>
        <h5 class="mb-2  b-b-solid  b-b-light">管理系统</h5>
        <el-menu default-active="0" class="el-menu-vertical-demo">
          <div v-for="(item, index) in asideStore.asideData" :key="index">
            <template v-if="item.children.length === 0">
              <el-menu-item :index="String(index)" @click="pageJump(item)">
                <span>{{ item.title }}</span>
              </el-menu-item>
            </template>
            <template v-else>
              <el-sub-menu :index="String(index)">
                <template #title>
                  <div>{{ item.title }}</div>
                </template>
                <el-menu-item v-for="(child, I) in item.children" :index="String(index - I)" :key="I"
                  @click="pageJump(child)">
                  <span>{{ child.title }}</span>
                </el-menu-item>
              </el-sub-menu>
            </template>
          </div>
        </el-menu>
      </el-col>
    </aside>
    <!-- 渲染嵌套路由的内容 -->
    <main class="content">
      <div class="content-top">
        <ul class="content-top-history">
          <li class="content-top-history-item" v-for="(history, index) in historyStore.history" :key="index"
            :class="historyStore.setStyle(history.isActivated)">
            <template v-if="history.title === '首页'">
              <span @click="changeHistory(history)">
                {{ history.title }}
              </span>
            </template>
            <template v-else>
              <span @click="changeHistory(history)">
                {{ history.title }}
              </span>
              <el-icon @click="deleteHistory(index)">
                <CloseBold />
              </el-icon>
            </template>
          </li>
        </ul>
        <div class="administrator">
          <span class="administrator-img">{{ infoData.identity }}</span>
          <span class="administrator-text">{{ infoData.name }}</span>

          <div class="administrator-hover">
            <span v-if="infoData.identity === '学'" @click="updateStudentInfoPopup = true"
              class="administrator-hover-text">修改密码</span>
            <span v-else @click="updateTeacherInfoPopup = true" class="administrator-hover-text">修改密码</span>
            <span class="administrator-hover-text" @click="exitLogin">退出登录</span>
          </div>
        </div>
      </div>
      <div class="content-box">
        <router-view></router-view>
      </div>
    </main>
  </div>


  <!-- 修改学生信息 -->
  <el-dialog v-model="updateStudentInfoPopup" title="修改账号信息" width="500">
    <el-form :model="updateStudentInfoForm" ref="updateStudentInfoFormRef" :rules="updateStudentInfoFormRules"
      class="box-border p-20px">
      <el-form-item prop="name" label="学生姓名" label-width="100px">
        <el-input placeholder="请输入您的姓名" v-model="updateStudentInfoForm.name"></el-input>
      </el-form-item>

      <el-form-item prop="username" label="账号" label-width="100px">
        <el-input placeholder="请输入您的账号" v-model="updateStudentInfoForm.username"></el-input>
      </el-form-item>

      <el-form-item prop="password" label="密码" label-width="100px">
        <el-input placeholder="请输入您的密码" v-model="updateStudentInfoForm.password"></el-input>
      </el-form-item>

      <el-form-item class="but">
        <el-button type="primary" class="signIn-box-form-btn"
          @click="changeStudentInfo(updateStudentInfoFormRef)">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 修改老师 -->
  <el-dialog v-model="updateTeacherInfoPopup" title="修改账号信息" width="500">
    <el-form :model="updateTeacherInfoForm" ref="updateTeacherInfoFormRef" :rules="updateTeacherInfoFormRules"
      class="box-border p-20px">
      <el-form-item prop="name" label="教师姓名" label-width="100px">
        <el-input placeholder="请输入您的姓名" v-model="updateTeacherInfoForm.name"></el-input>
      </el-form-item>

      <el-form-item prop="username" label="账号" label-width="100px">
        <el-input placeholder="请输入您的账号" v-model="updateTeacherInfoForm.username"></el-input>
      </el-form-item>

      <el-form-item prop="password" label="密码" label-width="100px">
        <el-input placeholder="请输入您的密码" v-model="updateTeacherInfoForm.password"></el-input>
      </el-form-item>

      <el-form-item class="but">
        <el-button type="primary" class="signIn-box-form-btn"
          @click="changeTeacherInfo(updateTeacherInfoFormRef)">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>


<script setup>
import useUpdateStudentInfo from "@/composables/useUpdateStudentInfo"

const {
  updateStudentInfoForm,
  updateStudentInfoPopup,
  updateStudentInfoFormRef,
  updateStudentInfoFormRules,
  initUpdateStudentInfoForm,
  changeStudentInfo
} = useUpdateStudentInfo();

const {
  updateTeacherInfoForm,
  updateTeacherInfoPopup,
  updateTeacherInfoFormRef,
  updateTeacherInfoFormRules,
  initUpdateTeacherInfoForm,
  changeTeacherInfo
} = useUpdateTeacherInfo();

const asideStore = useAside();
const historyStore = useHistory();
const router = useRouter();

const infoData = reactive({
  identity: "",
  name: ""
})


// 设置信息
const setInfo = () => {
  const info = JSON.parse(localStorage.getItem("user"));
  info.identity === 'isTeacher' ? infoData.identity = "教" : infoData.identity = "学"

  if (info.identity === 'isTeacher') {
    router.push({
      name: 'myStudents'
    })
    historyStore.setHistory({
      name: "myStudents",
      title: "我的学生",
      isActivated: true
    })
    initUpdateTeacherInfoForm();
  } else {
    router.push({
      name: 'studentCourse1'
    })
    historyStore.setHistory({
      name: "studentCourse1",
      title: "我的课程",
      isActivated: true
    })
    initUpdateStudentInfoForm();
  }

  infoData.name = info.data.name
}

// 页面跳转
const pageJump = (item) => {
  router.push({
    name: item.name
  })
  historyStore.setHistory({
    name: item.name,
    title: item.title,
    isActivated: true
  })
}

// 点击历史记录
const changeHistory = (history) => {
  historyStore.setHistory(history)
  router.push({
    name: history.name
  })
}

// 删除历史记录
const deleteHistory = (index) => {
  if (historyStore.history.length == 1) return
  historyStore.deleteHistory(index)
  router.push({
    name: historyStore.history[historyStore.history.length - 1].name
  })
}

// 退出登录
const exitLogin = () => {
  localStorage.removeItem("user");
  localStorage.removeItem("isSignIn");
  router.go(0)
}




onMounted(() => {
  setInfo();
})
</script>


<style scoped lang="scss">
.layout {
  display: flex;
  width: 100%;
  height: 100vh;
}

.sidebar {
  width: 200px;
  height: 100%;

  :deep(.el-col) {
    width: 100%;
    height: 100%;
    background: #04112A;

    .mb-2 {
      width: 100%;
      height: 70px;
      text-align: center;
      line-height: 70px;
      font-size: 20px;
      color: #fff;
    }

    // 一级菜单
    .el-menu {
      --el-menu-text-color: rgb(255 255 255 / 65%);
      --el-menu-hover-bg-color: #041028;
      background: transparent;
      border-right: none;

      .el-menu-item {
        justify-content: center;

        &:hover {
          span {
            border-radius: 5px;

          }
        }

        span {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 90%;
          height: 60%;
        }

        &:hover {
          color: #ffffff;
        }
      }

      .is-active {
        color: #ffffff;

        span {
          background: #563EFF;
          border-radius: 5px;
        }
      }
    }

    .el-sub-menu {

      .el-sub-menu__title {
        justify-content: center;
      }

    }

    // 二级菜单
    .el-menu--inline {
      --el-menu-text-color: rgb(255 255 255 / 65%);

      .el-menu-item {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 0 10%;
        background: #010917;
      }
    }
  }
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: 100px;
  height: 100%;

  .content-top {
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    width: 100%;
    height: 50px;
    background: #ffffff;
    border-bottom: 1px solid #eee;

    .content-top-history {
      display: flex;
      align-items: center;
      height: 100%;
      width: 70%;

      .active {
        position: relative;
        color: #1677ff;
        background-color: #EEEEEE;

        &::after {
          content: "";
          position: absolute;
          bottom: 1px;
          width: 100%;
          height: 2px;
          background: #1677ff;
        }
      }

      .content-top-history-item {
        box-sizing: border-box;
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 5px 15px;
        margin-right: 30px;
        font-weight: bold;
        font-size: 15px;
        border-radius: 5px;
        transition: all 0.5s;
        cursor: pointer;

        :deep(.el-icon) {
          margin-left: 5px;
          margin-top: 2px;
        }


        &:hover {
          background-color: #EEEEEE;
        }
      }
    }

    .administrator {
      position: relative;
      box-sizing: border-box;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 0 20px;
      height: 50px;
      margin-right: 100px;
      transition: all 0.5s;
      cursor: pointer;
      border-radius: 2px;

      &:hover {
        background: #F8F8F8;

        .administrator-text {
          color: #1677ff;
        }

        .administrator-hover {
          display: flex;
        }
      }

      .administrator-img {
        width: 25px;
        height: 25px;
        text-align: center;
        line-height: 25px;
        font-size: 15px;
        color: #ffffff;
        border-radius: 50%;
        background-color: #1677ff;
      }

      .administrator-text {
        margin-left: 10px;
      }

      .administrator-hover {
        z-index: 99999;
        box-sizing: border-box;
        position: absolute;
        display: none;
        flex-direction: column;
        justify-items: center;
        align-items: center;
        top: 50px;
        ;
        padding: 5px;
        width: 100%;
        box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        background: #ffffff;
        transition: all 0.5s;


        .administrator-hover-text {
          box-sizing: border-box;
          margin: 5px 0;
          padding: 5px 0;
          width: 90%;
          text-align: center;
          transition: all 0.5s;
          border-radius: 5px;

          &:hover {
            background: #F8F8F8;
          }
        }
      }
    }
  }

  .content-box {
    flex: 1;
    width: 100%;
    height: 1px;
    background-color: #F5F5F5;
  }
}

.but {
  :deep(.el-form-item__content) {
    justify-content: center
  }
}
</style>
