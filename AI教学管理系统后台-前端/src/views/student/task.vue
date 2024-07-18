<!-- <script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import debounce from 'lodash/debounce';
import studentCourseApi from '@/api/studentCourse';

// 获取路由参数
const route = useRoute();
const router = useRouter();
const tasklogid = route.params.id;

// 定义题目列表
const questionList = ref([]);
const title = ref()

// 试卷状态
const taskStatus = ref(1); // 默认值为1，待提交

// 标志位，用于判断是否是初次加载
const isInitialLoad = ref(true);


// 获取题目详情
const fetchMissionDetails = async () => {
  try {
    const response = await studentCourseApi.getTaskProgress(tasklogid);
    if (response.data.code === 200) {
      questionList.value = response.data.data.data;
      taskStatus.value = response.data.data.taskLog.status; // 设置试卷状态
      title.value = response.data.data.course.taskname // 标题
      isInitialLoad.value = false; // 数据加载完成后设置为false
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('获取题目详情失败');
  }
};

// 更新答案
const updateAnswer = debounce(async (timuid, content) => {
  try {
    await studentCourseApi.updateAnswer(tasklogid, timuid, content);
    // ElMessage({
    //   message: "答题成功",
    //   type: 'success',
    // });
  } catch (error) {
    console.error(error);
    // ElMessage.error('更新答案失败');
  }
}, 100);

// 提交答案
const submitAnswers = async () => {
  // 检查所有题目是否已作答
  const allAnswered = questionList.value.every(question => question.zuoda !== null && question.zuoda !== '');

  if (!allAnswered) {
    ElMessage.error('请完成所有题目');
    return;
  }

  try {
    const response = await studentCourseApi.answerSubmission(tasklogid);
    if (response.data.code === 200) {
      ElMessage.success("提交成功");
      // 提交成功后重新获取试卷数据
      await fetchMissionDetails();
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('提交失败');
  }
};

// 返回上一页
const goBack = () => {
  router.back();
};

// 监听题目的作答内容变化
watch(
  () => questionList.value.map(question => question.zuoda),
  (newValues, oldValues) => {
    if (isInitialLoad.value || taskStatus.value !== 1) return; // 如果是初次加载或试卷状态不是待提交，直接返回
    newValues.forEach((newVal, index) => {
      if (newVal !== oldValues[index]) {
        const question = questionList.value[index];
        updateAnswer(question.timuid, newVal);
      }
    });
  },
  { deep: true }
);

// 页面加载时获取题目详情
onMounted(() => {
  fetchMissionDetails();
});
</script>

<template>
  <div class="question-page">
    <div class="absolute right-100px">
      <template v-if="taskStatus === 1">
        <el-button type="primary" @click="submitAnswers">提交答案</el-button>
        <el-button type="primary" @click="goBack">返回</el-button>
      </template>
      <template v-else>
        <el-button type="primary" @click="goBack">返回</el-button>
      </template>

    </div>
    <h1>{{ title }}</h1>
    <el-form :model="questionList" label-position="top" max-w-50vw>
      <div v-for="question in questionList" :key="question.id"
        :class="['question-item', { 'error': taskStatus === 3 && question.isdui === 2 }, { 'success': taskStatus === 3 && question.isdui === 1 }]">
        <template v-if="taskStatus === 3 && question.isdui === 1">
          <div flex justify-end>
            <svg t="1719818439512" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="2598" width="30" height="30">
              <path
                d="M511.999994 0C229.205543 0 0.020822 229.226376 0.020822 512.020827c0 282.752797 229.184721 511.979173 511.979173 511.979173s511.979173-229.226376 511.979173-511.979173C1023.979167 229.226376 794.794446 0 511.999994 0zM815.371918 318.95082l-346.651263 461.201969c-10.830249 14.370907-27.32555 23.409999-45.27877 24.742952-1.582882 0.124964-3.12411 0.166619-4.665338 0.166619-16.328682 0-32.074198-6.373185-43.779197-17.911565l-192.903389-189.44604c-24.617988-24.20144-24.992881-63.731847-0.791441-88.349835 24.20144-24.659643 63.731847-24.951226 88.349835-0.833096l142.042875 139.501932 303.788472-404.2182c20.744091-27.575479 59.899605-33.115568 87.516739-12.413131C830.534266 252.219827 836.116009 291.375341 815.371918 318.95082z"
                fill="#1afa29" p-id="2599"></path>
            </svg>
          </div>
        </template>
        <template v-if="taskStatus === 3 && question.isdui === 2">
          <div flex justify-end>
            <svg t="1719818284824" class="icon" viewBox="0 0 1028 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="1562" width="30" height="30">
              <path
                d="M875.086452 153.730058C676.053818-45.302575 353.260522-45.302575 154.128323 153.730058s-199.032634 521.825929 0 720.958129 521.825929 199.032634 720.958129 0 199.032634-521.825929 0-720.958129zM725.836868 725.438604c-9.757478 9.757478-25.488922 9.757478-35.246399 0L514.557604 549.405739 338.624306 725.438604c-9.757478 9.757478-25.488922 9.757478-35.2464 0s-9.757478-25.488922 0-35.2464l176.032865-176.032864-176.032865-175.933299c-9.757478-9.757478-9.757478-25.488922 0-35.246399 9.757478-9.757478 25.488922-9.757478 35.2464 0l176.032864 176.032865 176.032865-176.032865c9.757478-9.757478 25.488922-9.757478 35.246399 0 9.757478 9.757478 9.757478 25.488922 0 35.246399L549.804004 514.15934 725.836868 690.192204c9.657912 9.757478 9.657912 25.488922 0 35.2464z"
                fill="#F56C6C" p-id="1563"></path>
            </svg>
          </div>
        </template>
        <el-form-item :label="question.tigan">
          <div v-if="question.type === 1">
            <el-input v-model="question.zuoda" :disabled="taskStatus !== 1" placeholder="请输入答案"></el-input>
          </div>
          <div v-else-if="question.type === 2">
            <el-radio-group v-model="question.zuoda" :disabled="taskStatus !== 1">
              <el-radio v-for="(option, index) in question.xuanxiang.split('|')" :key="index" :label="index + 1"
                :value="String(index + 1)">
                {{ option }}
              </el-radio>
            </el-radio-group>
          </div>
          <div v-else-if="question.type === 3">
            <el-radio-group v-model="question.zuoda" :disabled="taskStatus !== 1">
              <el-radio label="1" value="1">正确</el-radio>
              <el-radio label="2" value="2">错误</el-radio>
            </el-radio-group>
          </div>
          <div v-if="taskStatus === 3 && question.aijiexi" class="ai-analysis">
            <strong>AI解析：</strong>
            <div whitespace-break-spaces>
              {{ question.aijiexi }}
            </div>
          </div>
        </el-form-item>
        <div v-if="taskStatus === 2" class="ai-reviewing">
          AI评卷中，请耐心等待...
        </div>
      </div>
    </el-form>
  </div>
</template>

<style scoped lang="sass">
.question-page
  overflow-y: scroll
  box-sizing: border-box
  display: flex
  flex-direction: column
  align-items: center
  padding: 20px
  height: 90vh

.question-page::-webkit-scrollbar
  display: none

h1
  width: 100%
  margin-left: 20px
  margin-bottom: 20px

.question-item
  margin-bottom: 20px

.question-item.error
  border: 3px solid #ff4d4f
  padding: 10px
  border-radius: 4px

.question-item.success
  border: 3px solid #58a01f
  padding: 10px
  border-radius: 4px

.el-form-item__label
  font-weight: bold

.ai-analysis
  margin-top: 10px
  color: #409EFF

.ai-reviewing
  color: #ff9800
  margin-top: 10px
  font-weight: bold

:deep(.el-form)
  .el-form-item__content
    display: flex
    flex-direction: column

    div
      width: 100%
</style> -->
<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import debounce from 'lodash/debounce';
import studentCourseApi from '@/api/studentCourse';

// 获取路由参数
const route = useRoute();
const router = useRouter();
const tasklogid = route.params.id;

// 定义题目列表
const questionList = ref([]);
const title = ref()

// 试卷状态
const taskStatus = ref(1); // 默认值为1，待提交

// 标志位，用于判断是否是初次加载
const isInitialLoad = ref(true);

// 从 localStorage 中加载已保存的答案
const loadSavedAnswers = () => {
  const savedAnswers = localStorage.getItem(`answers_${tasklogid}`);
  if (savedAnswers) {
    const parsedAnswers = JSON.parse(savedAnswers);
    questionList.value.forEach(question => {
      if (parsedAnswers[question.timuid]) {
        question.zuoda = parsedAnswers[question.timuid];
      }
    });
  }
};

// 获取题目详情
const fetchMissionDetails = async () => {
  try {
    const response = await studentCourseApi.getTaskProgress(tasklogid);
    if (response.data.code === 200) {
      questionList.value = response.data.data.data;
      taskStatus.value = response.data.data.taskLog.status; // 设置试卷状态
      title.value = response.data.data.course.taskname // 标题
      isInitialLoad.value = false; // 数据加载完成后设置为false
      loadSavedAnswers(); // 加载已保存的答案
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('获取题目详情失败');
  }
};

// 保存答案到 localStorage
const saveAnswers = () => {
  const answers = {};
  questionList.value.forEach(question => {
    answers[question.timuid] = question.zuoda;
  });
  localStorage.setItem(`answers_${tasklogid}`, JSON.stringify(answers));
};

// 更新答案
const updateAnswer = async (timuid, content) => {
  try {
    await studentCourseApi.updateAnswer(tasklogid, timuid, content);
    // ElMessage({
    //   message: "答题成功",
    //   type: 'success',
    // });
  } catch (error) {
    console.error(error);
    // ElMessage.error('更新答案失败');
  }
};

// 提交答案
const submitAnswers = async () => {
  // 检查所有题目是否已作答
  const allAnswered = questionList.value.every(question => question.zuoda !== null && question.zuoda !== '');

  if (!allAnswered) {
    ElMessage.error('请完成所有题目');
    return;
  }

  try {
    // 提交所有答案
    for (const question of questionList.value) {
      await updateAnswer(question.timuid, question.zuoda);
    }

    const response = await studentCourseApi.answerSubmission(tasklogid);
    if (response.data.code === 200) {
      ElMessage.success("提交成功");
      // 提交成功后清除已保存的答案
      localStorage.removeItem(`answers_${tasklogid}`);
      // 提交成功后重新获取试卷数据
      await fetchMissionDetails();
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('提交失败');
  }
};

// 返回上一页
const goBack = () => {
  router.back();
};

// 监听题目的作答内容变化
watch(
  () => questionList.value.map(question => question.zuoda),
  (newValues, oldValues) => {
    if (isInitialLoad.value || taskStatus.value !== 1) return; // 如果是初次加载或试卷状态不是待提交，直接返回
    newValues.forEach((newVal, index) => {
      if (newVal !== oldValues[index]) {
        const question = questionList.value[index];
        question.zuoda = newVal;
        saveAnswers(); // 保存答案
      }
    });
  },
  { deep: true }
);

// 页面加载时获取题目详情
onMounted(() => {
  fetchMissionDetails();
});
</script>

<template>
  <div class="question-page">
    <div class="absolute right-100px">
      <template v-if="taskStatus === 1">
        <el-button type="primary" @click="submitAnswers">提交答案</el-button>
        <el-button type="primary" @click="goBack">返回</el-button>
      </template>
      <template v-else>
        <el-button type="primary" @click="goBack">返回</el-button>
      </template>

    </div>
    <h1>{{ title }}</h1>
    <el-form :model="questionList" label-position="top" max-w-50vw>
      <div v-for="question in questionList" :key="question.id"
        :class="['question-item', { 'error': taskStatus === 3 && question.isdui === 2 }, { 'success': taskStatus === 3 && question.isdui === 1 }]">
        <template v-if="taskStatus === 3 && question.isdui === 1">
          <div flex justify-end>
            <svg t="1719818439512" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="2598" width="30" height="30">
              <path
                d="M511.999994 0C229.205543 0 0.020822 229.226376 0.020822 512.020827c0 282.752797 229.184721 511.979173 511.979173 511.979173s511.979173-229.226376 511.979173-511.979173C1023.979167 229.226376 794.794446 0 511.999994 0zM815.371918 318.95082l-346.651263 461.201969c-10.830249 14.370907-27.32555 23.409999-45.27877 24.742952-1.582882 0.124964-3.12411 0.166619-4.665338 0.166619-16.328682 0-32.074198-6.373185-43.779197-17.911565l-192.903389-189.44604c-24.617988-24.20144-24.992881-63.731847-0.791441-88.349835 24.20144-24.659643 63.731847-24.951226 88.349835-0.833096l142.042875 139.501932 303.788472-404.2182c20.744091-27.575479 59.899605-33.115568 87.516739-12.413131C830.534266 252.219827 836.116009 291.375341 815.371918 318.95082z"
                fill="#1afa29" p-id="2599"></path>
            </svg>
          </div>
        </template>
        <template v-if="taskStatus === 3 && question.isdui === 2">
          <div flex justify-end>
            <svg t="1719818284824" class="icon" viewBox="0 0 1028 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="1562" width="30" height="30">
              <path
                d="M875.086452 153.730058C676.053818-45.302575 353.260522-45.302575 154.128323 153.730058s-199.032634 521.825929 0 720.958129 521.825929 199.032634 720.958129 0 199.032634-521.825929 0-720.958129zM725.836868 725.438604c-9.757478 9.757478-25.488922 9.757478-35.246399 0L514.557604 549.405739 338.624306 725.438604c-9.757478 9.757478-25.488922 9.757478-35.2464 0s-9.757478-25.488922 0-35.2464l176.032865-176.032864-176.032865-175.933299c-9.757478-9.757478-9.757478-25.488922 0-35.246399 9.757478-9.757478 25.488922-9.757478 35.2464 0l176.032864 176.032865 176.032865-176.032865c9.757478-9.757478 25.488922-9.757478 35.246399 0 9.757478 9.757478 9.757478 25.488922 0 35.246399L549.804004 514.15934 725.836868 690.192204c9.657912 9.757478 9.657912 25.488922 0 35.2464z"
                fill="#F56C6C" p-id="1563"></path>
            </svg>
          </div>
        </template>
        <el-form-item :label="question.tigan">
          <div v-if="question.type === 1">
            <el-input v-model="question.zuoda" :disabled="taskStatus !== 1" placeholder="请输入答案"></el-input>
          </div>
          <div v-else-if="question.type === 2">
            <div class="radio-group">
              <div
                v-for="(option, index) in question.xuanxiang.split('|')"
                :key="index"
                class="radio-option"
                :class="{ 'is-checked': question.zuoda === String(index + 1) }"
                @click="() => { if (taskStatus === 1) question.zuoda = String(index + 1) }"
              >
                {{ option }}
              </div>
            </div>
          </div>
          <div v-else-if="question.type === 3">
            <div class="radio-group">
              <div
                class="radio-option"
                :class="{ 'is-checked': question.zuoda === '1' }"
                @click="() => { if (taskStatus === 1) question.zuoda = '1' }"
              >
                正确
              </div>
              <div
                class="radio-option"
                :class="{ 'is-checked': question.zuoda === '2' }"
                @click="() => { if (taskStatus === 1) question.zuoda = '2' }"
              >
                错误
              </div>
            </div>
          </div>
          <div v-if="taskStatus === 3 && question.aijiexi" class="ai-analysis">
            <strong>AI解析：</strong>
            <div whitespace-break-spaces>
              {{ question.aijiexi }}
            </div>
          </div>
        </el-form-item>
        <div v-if="taskStatus === 2" class="ai-reviewing">
          AI评卷中，请耐心等待...
        </div>
      </div>
    </el-form>
  </div>
</template>

<style scoped lang="sass">
.question-page
  overflow-y: scroll
  box-sizing: border-box
  display: flex
  flex-direction: column
  align-items: center
  padding: 20px
  height: 90vh

.question-page::-webkit-scrollbar
  display: none

h1
  width: 100%
  margin-left: 20px
  margin-bottom: 20px

.question-item
  margin-bottom: 20px

.question-item.error
  border: 3px solid #ff4d4f
  padding: 10px
  border-radius: 4px

.question-item.success
  border: 3px solid #58a01f
  padding: 10px
  border-radius: 4px

.el-form-item__label
  font-weight: bold

.ai-analysis
  margin-top: 10px
  color: #409EFF

.ai-reviewing
  color: #ff9800
  margin-top: 10px
  font-weight: bold

:deep(.el-form)
  .el-form-item__content
    display: flex
    flex-direction: column

    div
      width: 100%

.radio-group
  display: flex
  flex-direction: column

.radio-option
  margin-bottom: 10px
  padding: 10px
  border: 1px solid #dcdfe6
  border-radius: 4px
  cursor: pointer
  user-select: none

.radio-option.is-checked
  background-color: #409EFF
  color: #fff
</style>
