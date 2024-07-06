import myStudents from "../views/teacher/myStudents.vue"
import myCourse from "../views/teacher/myCourse.vue"

// 教师路由
const teacher = [
    {
        path: "teacher/myStudents",
        name: "myStudents",
        meta: {
            title: "我的学生",
            aside: true
        },
        component: myStudents,
    },
    {
        path: "teacher/myCourse",
        name: "myCourse",
        meta: {
            title: "我的课程",
            aside: true
        },
        component: myCourse,
        children: []
    },

]


export default teacher
