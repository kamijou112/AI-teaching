<script setup>
import studentCourseApi from '@/api/studentCourse';
import useTime from "@/composables/useTime.js"

const router = useRouter();

// 学生ID
const studentId = JSON.parse(localStorage.getItem("user")).data.id;

// 获取路由参数
const route = useRoute();
const kcid = route.params.kcid;

// 定义任务列表
const taskList = ref([]);


// 获取课程任务列表
const fetchCourseTasks = async () => {
  try {
    const response = await studentCourseApi.getCourseTask(kcid, studentId);
    if (response.data.code === 200) {
      taskList.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('获取课程任务失败');
  }
};

// 查看任务的逻辑
const viewTask = async (taskId) => {
  const { data } = await studentCourseApi.studentAnswer(studentId, taskId);
  if (data.code === 200) {
    router.push({
      name: "task",
      params: { id: data.data.id }
    })
  } else {
    ElMessage.error(data.msg);
  }
};

const goBack = () => {
  router.push({
    name: "studentCourse1"
  })
}

// 页面加载时获取任务列表
onMounted(() => {
  fetchCourseTasks();
});
</script>

<template>
  <div class="box box-border p-30px max-h80vh overflow-scroll">
    <div flex flex-items-center>
      <h1>课程详情</h1>
      <el-button m-l-30px type="primary" @click="goBack">返回</el-button>
    </div>
    <el-table :data="taskList" style="width: 99%; margin-top: 20px">
      <!-- 时间 -->
      <el-table-column prop="status" label="任务发布的时间">
        <template #default="scope">
          <span>{{ useTime(scope.row.datetime) }}</span>
        </template>
      </el-table-column>
      <!-- 名称 -->
      <el-table-column prop="taskname" label="任务名称"></el-table-column>
      <!-- 任务类型 -->
      <el-table-column prop="status" label="任务类型">
        <template #default="scope">
          <el-tag type="info"> {{ scope.row.type === 1 ? '作业' : '考试' }} </el-tag>
        </template>
      </el-table-column>
      <!-- 操作 -->
      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-if="scope.row.status === 0 || scope.row.status === 1" type="primary" size="small"
            @click="viewTask(scope.row.taskid)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped lang="sass">
.box::-webkit-scrollbar
  display: none

.el-table
  margin-top: 20px
</style>
