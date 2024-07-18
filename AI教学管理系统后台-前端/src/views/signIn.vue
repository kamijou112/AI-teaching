<template>
  <div class="app-container">
    <header class="nav-bar">
      <div class="logo">
        <img src="../assets/logo.png" alt="Logo">
      </div>
      <nav>
        <ul>
          <li @click="showMessage">智能测评分析</li>
          <li @click="showMessage">AI助手</li>
          <li @click="showMessage">个性化资源推荐</li>
        </ul>
      </nav>
    </header>
    <div class="carousel-container">
      <el-carousel height="70vh" motion-blur>
        <el-carousel-item v-for="(item, index) in carouselImages" :key="index">
          <div class="carousel-item-content">
            <img :src="item" alt="Carousel Image" class="carousel-image">
          </div>
        </el-carousel-item>
      </el-carousel>
      <div class="signIn">
        <div class="signIn-box">
          <h2 class="signIn-box-title">登录</h2>
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
    </div>
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-left">
          <div class="footer-section">
            <img src="../assets/logo1.png" alt="Icon" class="footer-icon" />
            <div>
              <h4>学生管理与评估</h4>
              <p>教师可以实时了解学生作业完成情况，并通过AI技术生成学习成果评估报告</p>
            </div>
          </div>
          <div class="footer-section">
            <img src="../assets/logo2.png" alt="Icon" class="footer-icon" />
            <div>
              <h4>课程与作业管理</h4>
              <p>支持AI自动出题和手动编辑题目，方便组织和安排教学任务</p>
            </div>
          </div>
          <div class="footer-section">
            <img src="../assets/logo3.png" alt="Icon" class="footer-icon" />
            <div>
              <h4>AI智能批改与错题本</h4>
              <p>自动批改学生提交的作业，生成错题解析，并添加到错题本中，方便学生进行复习和巩固</p>
            </div>
          </div>
          <div class="footer-section">
            <img src="../assets/logo4.png" alt="Icon" class="footer-icon" />
            <div>
              <h4>资源推荐助手</h4>
              <p>AI学习助手根据学生的学习情况，提供学习资源推荐</p>
            </div>
          </div>
        </div>
        <div class="footer-right">
          <div class="footer-section">
            <div>
              <h4>联系我们</h4>
            </div>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import teacher from "@/components/signIn/teacher.vue";
import student from "@/components/signIn/student.vue";
import img1 from '../assets/img/img1.png';
import img2 from '../assets/img/img2.png';
import img3 from '../assets/img/img3.png';
import { ElMessage } from 'element-plus';

const activeName = ref('first');
const carouselImages = ref([img1, img2, img3]);

const showMessage = () => {
  ElMessage({
    message: '请登录后使用。',
    type: 'warning',
    duration: 3000, // 4秒后自动消失
  });
};
</script>

<style scoped lang="scss">
@import "@/sass/signin/index";

.app-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f0f2f5; /* 添加背景色 */
}

.nav-bar {
  width: 100%; /* 设置宽度为100% */
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #409EFF;
  position: relative;

  .logo {
    margin-left: 150px; /* 向右移动 */
    
    img {
      width: 110px;
    }
  }

  nav {
    position: absolute;
    right: 15%; /* 调整位置到大约页面宽度的5/7处 */
    bottom: 20%; /* 调整位置到大约从上往下的2/3处 */
    display: flex;
    align-items: center;
    
    ul {
      display: flex;
      list-style: none;
      margin: 0;
      padding: 0;
      
      li {
        margin: 0 10px; /* 调整间距 */
        color: white;
        cursor: pointer;
        font-size: 20px;

        &:not(:last-child)::after {
          content: '|';
          margin-left: 10px; /* 调整竖线和文字间距 */
        }
      }
    }
  }
}

.carousel-container {
  width: 100%;
  position: relative;

  .el-carousel__item .carousel-item-content {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    overflow: hidden;

    .carousel-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }
}

.signIn {
  position: absolute;
  top: 50%;
  right: 15%;
  transform: translateY(-50%);
  display: flex;
  justify-content: center;
  align-items: center;
  width: auto;
  height: auto;



  .signIn-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    background: #fff;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);

    .signIn-box-title {
      height: 70px;
      line-height: 70px;
      font-size: 25px;
      font-weight: 600;
      color: #409EFF;
      margin-top: 0px;
    }

    .signIn-box-form {
      flex-grow: 1;
      width: 65%;
      height: 100px;

      :deep(.el-tabs__nav) {
        width: 100%;

        .el-tabs__active-bar {
          width: 50%;
        }

        .el-tabs__item {
          width: 50%;
        }
      }

      :deep(.el-tabs__content) {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        height: 60%;

        .el-tab-pane {
          margin-top: 20px;
          width: 80%;

          .signIn-form {
            width: 100%;

            .el-form {
              .el-form-item {
                margin-bottom: 30px;
                &:last-child {
                  .el-form-item__content {
                    justify-content: center;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

.footer {
  width: 100%; /* 设置宽度为100% */
  background: #409EFF;
  display: flex;
  justify-content: center;
  padding: 20px 0;

  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: stretch;
    width: 100%;

    .footer-left, .footer-right {
      display: flex;
      align-items: center;
    }

    .footer-left {
      flex: 4;
      display: flex;
      background: #5454cc;
      padding: 20px;
      border-radius: 10px 0 0 10px;

      .footer-section {
        display: flex;
        align-items: center;
        margin: 0 10px;

        .footer-icon {
          margin-right: 10px;
          width: 50px;
          height: 50px;
          border-radius: 50%; /* 图标圆形 */
        }

        h4 {
          font-size: 18px;
          margin-bottom: 10px;
          color: aqua;
        }

        p {
          font-size: 14px;
          margin: 0;
          color: #fff;
        }
      }
    }

    .footer-right {
      flex: 1;
      display: flex;
      background: #4a4aaf url('../assets/message.png') no-repeat center center;
      background-size: contain; /* 调整背景图大小 */
      padding: 20px;
      border-radius: 0 10px 10px 0;
      margin-left: 20px;
      align-items: center;
      justify-content: center;

      .footer-section {
        display: flex;
        align-items: center;

        .footer-icon {
          margin-right: 10px;
          width: 50px;
          height: 50px;
          border-radius: 50%; /* 图标圆形 */
        }

        h4 {
          font-size: 30px;
          margin: 0;
          color: #fff;
        }
      }
    }
  }
}
</style>

