<script setup>
import studentCourseApi from '@/api/studentCourse.js';
import useTime from '@/composables/useTime.js';

onMounted(() => {
  fetchCourseList();
});


// 学生ID
const studentId = JSON.parse(localStorage.getItem("user")).data.id;


const courseList = ref([]);
const searchKeyword = ref('');
const searchResults = ref([]);
const selectedCourseId = ref(null);
const showDialog = ref(false);
const router = useRouter();

const fetchCourseList = async () => {
  try {
    const response = await studentCourseApi.getCourseListByStudent(studentId);
    if (response.data.code === 200) {
      courseList.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('获取课程列表失败');
  }
};

const searchCourses = async () => {
  try {
    const response = await studentCourseApi.studentGetCourse(searchKeyword.value);
    if (response.data.code === 200) {
      searchResults.value = response.data.data;
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('搜索课程失败');
  }
};

const addCourse = async () => {
  try {
    const response = await studentCourseApi.studentJoinCourse(studentId, selectedCourseId.value);
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg);
      showDialog.value = false;
      fetchCourseList();
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('加入课程失败');
  }
};

const openDialog = () => {
  searchKeyword.value = '';
  searchResults.value = [];
  selectedCourseId.value = null;
  showDialog.value = true;
};

const deleteCourse = async (kcid) => {
  try {
    const response = await studentCourseApi.studentQuitCourse(studentId, kcid);
    if (response.data.code === 200) {
      ElMessage.success(response.data.msg);
      await fetchCourseList();
    } else {
      ElMessage.error(response.data.msg);
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('退出课程失败');
  }
};

const handleDeleteCourse = (kcid) => {
  ElMessageBox.confirm(
    '确定要退出该课程吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    deleteCourse(kcid);
  }).catch(() => {
    ElMessage.info('已取消退出');
  });
};

const handleEnterCourse = (kcid) => {
  router.push(
    {
      name: 'courseDetails',
      params: {
        kcid
      }
    });
};

onMounted(() => {
  fetchCourseList();
});
</script>

<template>
  <div class="box box-border max-h-80vh overflow-scroll  p-20px">
    <el-button type="primary" @click="openDialog">加入课程</el-button>
    <el-table :data="courseList" style="width: 100%; margin-top: 20px">
      <el-table-column prop="kcname" label="课程名称"></el-table-column>
      <el-table-column prop="createtime" label="加入时间">
        <template #default="scope">
          {{ useTime(scope.row.createtime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="danger" size="small" @click="handleDeleteCourse(scope.row.kcid)">退出</el-button>
          <el-button type="primary" size="small" @click="handleEnterCourse(scope.row.kcid)">进入课程</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showDialog" title="加入课程">
      <el-input v-model="searchKeyword" placeholder="请输入课程id或课程名称">
        <template #append>
          <el-button type="danger" @click="searchCourses">搜索</el-button>
        </template>
      </el-input>
      <el-table :data="searchResults" style="width: 100%; margin-top: 20px"
        @selection-change="selection => selectedCourseId = selection[0]?.id">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="kcname" label="课程名称"></el-table-column>
        <el-table-column prop="studentcounts" label="学生人数"></el-table-column>
      </el-table>
      <el-button type="primary" m-t-10px @click="addCourse" :disabled="!selectedCourseId">加入</el-button>
    </el-dialog>
  </div>
</template>

<style scoped lang="sass">
.box::-webkit-scrollbar
  display: none
</style>
