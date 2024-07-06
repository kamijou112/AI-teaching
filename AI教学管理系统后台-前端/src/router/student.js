import studentCourse from "../views/student/studentCourse.vue"
import studentCourse1 from "../views/student/studentCourse1.vue"
import courseDetails from "../views/student/courseDetails.vue"
import task from "../views/student/task.vue"

// 教师路由
const student = [
    // {
    //     path: "student/studentCourse",
    //     name: "studentCourse",
    //     meta: {
    //         title: "我的课程",
    //         aside: true
    //     },
    //     component: studentCourse,
    // },
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
    }
]

export default student
