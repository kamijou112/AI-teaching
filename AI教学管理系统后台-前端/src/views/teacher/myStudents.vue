<script setup>
  import studentApi from '@/api/studentApi';
  import useTime from '@/composables/useTime';
  import useStudentsResult from "@/composables/useStudentsResult"
  import { MdPreview } from 'md-editor-v3';
  import 'md-editor-v3/lib/style.css';

  const { studentsResultPopup, studentsResultData, openResultPopup } = useStudentsResult();

  const student = ref([]);
  const search = ref('');
  const getStudents = async () => {
    const { data } = await studentApi.getAllStudent();
    student.value = data.data;
  };
  const searchStudent = () => {
    if (search.value === '') {
      ElMessage({
        message: '请输入学生名称',
        type: 'warning',
      })
    } else {
      student.value = student.value.filter(item => item.name.includes(search.value))
      ElMessage({
        message: '搜索成功',
        type: 'success',
      })
    }
  }

  const edit = async (data) => {
    const title = data.status === 0 ? '封禁学生' : '解封学生';
    const status = data.status === 0 ? 1 : 0;
    try {
      await ElMessageBox.confirm(`确定要${title}${data.name}吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        top: '10vh',
        type: "warning"
      })
      try {
        await studentApi.updateStudent(data.id, status);
        ElMessage({
          message: '解封成功',
          type: 'success',
        })
        await getStudents();
      } catch (err) {
        ElMessage('解封失败')
      }
    } catch (err) {
      ElMessage('已取消')
    }
  }

  onMounted(getStudents);
</script>

<template>
  <div class="myStudents">
    <!-- 搜索 -->
    <el-row class="box-border p-20px m-b-30px bg-#fff flex-row">
      <el-col :span="24" class="flex-row">
        <el-input v-model="search" placeholder="请输入学生姓名" class="search-input" />
        <el-button type="primary" class="m-l-10px" @click="searchStudent">
          <el-icon class="m-r-5px" size="16">
            <Search />
          </el-icon>
          查询学生
        </el-button>
        <el-button type="primary" class="m-l-10px" @click="getStudents">
          <el-icon class="m-r-5px" size="16">
            <Refresh />
          </el-icon>
          重置
        </el-button>
      </el-col>
    </el-row>
    <!-- 表单 -->
    <el-row class="form box-border overflow-scroll justify-end p-20px h-75vh bg-#fff">
      <el-col :span="24" class="m-b-30px">
        <el-table :data="student" border class="w-100%">
          <el-table-column prop="createtime" label="创建时间" min-width="180">
            <template #default="scope">
              {{ useTime(scope.row.createtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="学生姓名" min-width="180" />
          <el-table-column prop="username" label="学生账号" min-width="180" />
          <el-table-column prop="password" label="学生密码" min-width="180" />
          <el-table-column prop="sex" label="性别" width="180">
            <template #default="scope">
              {{ scope.row.sex === 1 ? '男' : '女' }}
            </template>
          </el-table-column>
          <el-table-column prop="studyresult" label="学生近期学习成果" min-width="180">
            <template #default="empty">
              <el-empty v-if="empty.row.studyresult === null" description="暂无学习成果" :image-size="50"
                        style="width: 160px;height: 80px;" />
              <div v-else>
                <el-tag type="primary" @click="openResultPopup(empty.row.studyresult)">查看学习成果</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="180">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 0" type="success">正常</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="edit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>

  <!-- 学习成果 -->
  <el-dialog v-model="studentsResultPopup" title="学生近期学习成果" width="700">
    <MdPreview editorId="preview-only" :modelValue="studentsResultData" />
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

  :deep(.el-empty__description)
    margin-top: 0 !important

  #preview-only
    max-height: 600px
</style>
