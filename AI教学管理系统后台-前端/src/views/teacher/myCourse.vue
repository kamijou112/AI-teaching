<script setup>
import topic from "@/components/topic.vue"
import courseApi from '@/api/courseApi';
import useTime from '@/composables/useTime';
import useSeeAddStudents from "@/composables/useSeeAddStudents";

const {
  addStudentsList,
  seeStudentsPopus,
  openSeeAddStudentsPopup
} = useSeeAddStudents();

// 课程
const courseList = ref([]);
const search = ref('');

const getCourse = async () => {
  const { data } = await courseApi.courseApi();
  courseList.value = data.data;
};

const searchCourse = () => {
  if (search.value === '') {
    ElMessage({
      message: '请输入课程名称',
      type: 'warning',
    })
  } else {
    courseList.value = courseList.value.filter(item => item.kcname.includes(search.value))
    ElMessage({
      message: '搜索成功',
      type: 'success',
    })
  }
}

const deleteCourse = async (id) => {
  try {
    await ElMessageBox.confirm(`确定要删除该课程吗?`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "error"
    })
    try {
      await courseApi.deleteCourseApi(id);
      ElMessage({
        message: '删除成功',
        type: 'success',
      })
      await getCourse();
    } catch (e) {
      ElMessage({
        message: '删除失败',
        type: 'error',
      })

    }
  } catch (err) {
    ElMessage('已取消')
  }
}


// 添加课程
const addCoursePopUps = ref(false)
const addCourseForm = reactive({
  name: "",
})

const addCourse = async () => {
  if (addCourseForm.name === '') ElMessage({
    message: '请输入课程名称',
    type: 'warning',
  })
  else {
    try {
      await courseApi.addCourseApi(addCourseForm.name);
      ElMessage({
        message: '添加成功',
        type: 'success',
      })
      await getCourse();
      addCoursePopUps.value = false;
      addCourseForm.name = '';
    } catch (err) {
      ElMessage({
        message: '添加失败',
        type: 'error',
      })
    }
  }
}


// 课程详情
const courseDetailsPopUps = ref(false);
const courseDetails = ref({
  name: "",
  id: ""
});


// 课程任务
const courseTasksList = ref();

const getCourseTask = async (id) => {
  try {
    const result = await courseApi.findCourseTask(id);
    courseTasksList.value = result.data.data
  } catch (e) {
    ElMessage({
      message: '获取课程任务失败',
      type: 'error',
    })
  }
}

const checkOutTheCourse = async (data) => {
  courseDetails.value.name = '课程名称：' + data.kcname;
  courseDetails.value.id = data.id;
  await getCourseTask(data.id);
  courseDetailsPopUps.value = true;
}

const deleteCourseTask = async (id) => {
  try {
    await ElMessageBox.confirm(`确定要删除该任务吗?`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "error"
    })
    try {
      await courseApi.deleteCourseTask(id);
      ElMessage({
        message: '删除成功',
        type: 'success',
      })
      await getCourseTask(courseDetails.value.id);
    } catch (e) {
      ElMessage({
        message: '删除失败',
        type: 'error',
      })
    }
  } catch (err) {
    ElMessage('已取消')
  }
}

// 添加课程任务
const addCourseTaskPopUps = ref(false);
const addCourseTaskForm = reactive({
  taskname: "",
  type: "",
  kcid: ""
})

const addCourseTask = async () => {
  if (addCourseTaskForm.taskname !== '' && addCourseTaskForm.type !== '') {
    try {
      addCourseTaskForm.kcid = courseDetails.value.id;
      await courseApi.addCourseTask(addCourseTaskForm);
      ElMessage({
        message: '添加课程任务成功',
        type: 'success',
      })
      await getCourseTask(courseDetails.value.id);
      addCourseTaskPopUps.value = false;
      addCourseTaskForm.taskname = '';
      addCourseTaskForm.type = '';
      addCourseTaskForm.kcid = '';
    } catch (err) {
      ElMessage({
        message: '添加课程任务失败',
        type: 'error',
      })
    }
  } else {
    ElMessage({
      message: '请输入任务名称',
      type: 'warning',
    })
  }
}



// 课程任务详情
const courseTaskDetailsPopUps = ref(false);

const courseTaskDetailsId = ref("")

const viewCourseAssignmentDetails = async (id) => {
  courseTaskDetailsId.value = String(id)
  courseTaskDetailsPopUps.value = true;
}

const topicRef = ref();

const hide = () => {
  topicRef.value.forEach((i) => {
    i.resetData();
  })
}

const show = () => {
  topicRef.value.forEach((i) => {
    i.getTopic(courseTaskDetailsId.value);
  })
}

// 发布任务
const publishCourseTask = async (id) => {
  try {
    await ElMessageBox.confirm(`确定要发布该任务吗?`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    })
    try {
      await courseApi.publishTask(id);
      ElMessage({
        message: '发布成功',
        type: 'success',
      })
      await getCourseTask(courseDetails.value.id);
    } catch (e) {
      ElMessage({
        message: '发布失败',
        type: 'error',
      })
    }
  } catch (err) {
    ElMessage('已取消')
  }
}


onMounted(getCourse);
</script>

<template>
  <div class="myStudents">
    <el-row class="box-border p-20px m-b-30px bg-#fff flex-row">
      <!-- 搜索 -->
      <el-col :span="24" class="flex-row">
        <el-input v-model="search" placeholder="请输入课程名称" class="search-input" />
        <el-button type="primary" class="m-l-10px" @click="searchCourse">
          <el-icon class="m-r-5px" size="16">
            <Search />
          </el-icon>
          查询课程
        </el-button>
        <el-button type="primary" class="m-l-10px" @click="getCourse">
          <el-icon class="m-r-5px" size="16">
            <Refresh />
          </el-icon>
          重置
        </el-button>
      <!-- 创建课程 -->
        <el-button type="primary" class="m-l-10px" @click="addCoursePopUps = true">
          <el-icon class="m-r-5px" size="16">
            <Plus />
          </el-icon>
          创建课程
        </el-button>
      </el-col>
    </el-row>
    <!-- 表单 -->
    <el-row class="form box-border overflow-scroll justify-end p-20px  max-h-70vh bg-#fff">
      <el-col :span="24" class="m-b-30px">
        <el-table :data="courseList" border class="w-100%">
          <el-table-column prop="createtime" label="课程创建时间" min-width="180">
            <template #default="scope">
              {{ useTime(scope.row.createtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="kcname" label="课程名称" min-width="180" />
          <el-table-column prop="studentcounts" label="加入学生人数" min-width="180">
            <template #default="scope">
              已加入
              <el-text class="mx-1" type="primary">{{ scope.row.studentcounts }}</el-text>
              人
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="250">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="deleteCourse(scope.row.id)">删除</el-button>
              <el-button link type="primary" size="small" @click="checkOutTheCourse(scope.row)">查看详情</el-button>
              <el-button link type="primary" size="small"
                @click="openSeeAddStudentsPopup(scope.row.id)">查看学生</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
  <!-- 添加课程弹窗 -->
  <el-dialog v-model="addCoursePopUps" title="添加课程" width="500">
    <el-form :model="addCourseForm">
      <el-form-item label="请输入课程名称" label-width="140px">
        <el-input v-model="addCourseForm.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addCoursePopUps = false">取消</el-button>
        <el-button type="primary" @click="addCourse">添加</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 查看课程详情 -->
  <div class="drawer">
    <el-drawer v-model="courseDetailsPopUps" direction="rtl" size="60%">
      <template #header>
        <div class="w-100px h-100% font-size-22px font-600">
          {{ courseDetails.name }}
          <el-button type="primary" class="m-l-20px" @click="addCourseTaskPopUps = true">
            <el-icon class="m-r-5px" size="16">
              <Plus />
            </el-icon>
            创建任务
          </el-button>
        </div>

      </template>
      <template #default>
        <el-descriptions v-for="item in courseTasksList" :key="item.id" class="m-b-30px" direction="vertical"
          :column="5" border>
          <template #title>
            <span>当前任务名称:{{ item.taskname }}</span>
          </template>
          <el-descriptions-item label="任务创建时间">{{ useTime(item.createtime) }}</el-descriptions-item>
          <el-descriptions-item label="完成人数" :span="2">已完成{{ item.wanchengsounts }}人</el-descriptions-item>
          <el-descriptions-item label="任务类型">
            <el-tag type="info">{{ item.type === 1 ? '作业' : '考试' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="操作">
            <!-- 删除 -->
            <el-tag type="danger" @click="deleteCourseTask(item.id)" class="cursor-pointer">删除</el-tag>
            <!-- 查看详情按钮 -->
            <el-popover m-l-10px placement="right" :width="800" trigger="click" @hide="hide" @show="show">
              <template #reference>
                <el-tag type="primary" class="m-l-10px cursor-pointer"
                  @click="viewCourseAssignmentDetails(item.id)">查看详情</el-tag>
              </template>
              <!-- 任务详情 -->
              <topic ref="topicRef" :courseTaskDetailsId="courseTaskDetailsId" />
            </el-popover>
            <!-- 是否发布 -->
            <el-tag m-l-10px type="success" cursor-pointer v-if="item.isfb === 1">已发布</el-tag>
            <el-tag type="warning" m-l-10px cursor-pointer v-else @click="publishCourseTask(item.id)">发布</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-drawer>
  </div>
  <!-- 添加课程任务弹窗 -->
  <el-dialog v-model="addCourseTaskPopUps" title="添加课程任务" width="500">
    <el-form :model="addCourseTaskForm">
      <el-form-item label="请输入课程任务名称" label-width="140px">
        <el-input v-model="addCourseTaskForm.taskname" autocomplete="off" />
      </el-form-item>
      <el-form-item label="任务类型" label-width="140px">
        <el-radio-group v-model="addCourseTaskForm.type">
          <el-radio :value="1">作业</el-radio>
          <el-radio :value="2">考试</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="addCourseTaskPopUps = false">取消</el-button>
        <el-button type="primary" @click="addCourseTask">添加</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 查看加入的学生 -->
  <el-dialog v-model="seeStudentsPopus" title="加入该课程的学生" width="500">
    <el-row class="form box-border overflow-scroll justify-end p-20px  max-h-30vh bg-#fff">
      <el-col :span="24" class="m-b-30px">
        <el-table :data="addStudentsList" border class="w-100%">
          <el-table-column prop="createtime" label="加入时间" min-width="180">
            <template #default="scope">
              {{ useTime(scope.row.createtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="学生名称" width="100" />
          <el-table-column prop="kcname" label="课程名称" width="100" />
        </el-table>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<style scoped lang="sass">
.myStudents
  box-sizing: border-box
  padding: 20px
  width: 100%
  height: 100%

.form::-webkit-scrollbar
  display: none


.flex-row
  display: flex
  align-items: center
  gap: 10px

.search-input
  width: 300px

:deep(.el-row)
  border-radius: 10px


:deep(.el-table__cell)
  .cell
    color: #000

:deep(.el-table__row)
  .cell
    color: #767575

.drawer:deep(.el-drawer__header)
  padding: 0 20px
  height: 50px
  line-height: 50px
  color: #000000
  margin-bottom: 0
  border-bottom: 1px solid #ccc

.drawer:deep(.el-drawer__body)
  padding: 10px 20px

:deep(.el-descriptions__header)
  box-sizing: border-box
  display: flex
  align-items: center
  margin-bottom: 10px
  padding: 0 10px
  height: 50px
  border-radius: 5px
  background-color: #eee
</style>
