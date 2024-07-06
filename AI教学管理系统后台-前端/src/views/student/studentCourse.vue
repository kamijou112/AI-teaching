<script setup>
import useTime from '@/composables/useTime';

// 课程
const courseList = ref([]);
const search = ref('');

const getCourse = async () => {
  const info = JSON.parse(localStorage.getItem("user"));
  console.log(info)
  // const {data} = await studentCourseApi.getStudentCourse();
  // courseList.value = data.data;
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

onMounted(getCourse);
</script>

<template>
  <div class="myStudents">
    <el-row class="box-border p-20px m-b-30px bg-#fff ">
      <!-- 搜索 -->
      <el-col :span="6" class="flex">
        <el-input v-model="search" placeholder="请输入课程名称"/>
        <el-button type="primary" class="m-l-10px" @click="searchCourse">
          <el-icon class="m-r-5px" size="16">
            <Search/>
          </el-icon>
          查询课程
        </el-button>
      </el-col>
      <!-- 重置 -->
      <el-col :span="1.5" class="flex">
        <el-button type="primary" class="m-l-10px" @click="getCourse">
          <el-icon class="m-r-5px" size="16">
            <Refresh/>
          </el-icon>
          重置
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
          <el-table-column prop="kcname" label="课程名称" min-width="180"/>
          <el-table-column prop="studentcounts" label="加入学生人数" min-width="180">
            <template #default="scope">
              已加入
              <el-text class="mx-1" type="primary">{{ scope.row.studentcounts }}</el-text>
              人
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template #default="scope">
<!--              <el-button link type="primary" size="small" @click="deleteCourse(scope.row.id)">删除</el-button>-->
              <el-button link type="primary" size="small">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>

</template>

<style scoped lang="sass">
.myStudents
  box-sizing: border-box
  padding: 20px
  width: 100%
  height: 100%

.form::-webkit-scrollbar
  display: none


:deep(.el-row)
  border-radius: 10px


:deep(.el-table__cell)
  .cell
    color: #000

:deep(.el-table__row)
  .cell
    color: #767575
</style>
