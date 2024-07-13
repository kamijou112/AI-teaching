import studentCourse from "../views/student/studentCourse.vue"
import studentCourse1 from "../views/student/studentCourse1.vue"
import courseDetails from "../views/student/courseDetails.vue"
import task from "../views/student/task.vue"
import StudyResources from "../views/student/StudyResources.vue" // 添加这一行导入StudyResources组件

// 学生路由
const student = [
    {
        path: "student/studentCourse1",
        name: "studentCourse1",
        meta: {
            title: "我的课程",
            aside: true
        },
        component: studentCourse1,
    },
    {
        path: "student/courseDetails/:kcid",
        name: "courseDetails",
        meta: {
            title: "课程详情",
            aside: false
        },
        component: courseDetails
    },
    {
        path: "student/task/:id",
        name: "task",
        meta: {
            title: "作业详情",
            aside: false
        },
        component: task
    },
    {
        path: 'student/study-resources', // 更新路径，使其在学生目录下
        name: 'StudyResources',
        meta: {
            title: "学习资源推荐",
            aside: true // 确保该项在侧边栏显示
        },
        component: StudyResources,
    }
]

export default student

