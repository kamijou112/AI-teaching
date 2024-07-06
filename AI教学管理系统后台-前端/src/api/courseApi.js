
import axios from '@/server/index';

const courseApi = {
    // 获取课程列表
    courseApi() {
        return axios.get('/getCourseList');
    },
    // 添加课程
    addCourseApi(name) {
        return axios.get(`/addCourse?kcname=${name}`);
    },
    // 删除课程
    deleteCourseApi(id) {
        return axios.get(`/delCourse?id=${id}`);
    },
    // 查询课程
    findCourseApi(id) {
        return axios.get(`/getCourse?kcid=${id}`);
    },
    // 查询课程任务
    findCourseTask(id) {
        return axios.get(`/getCourseTask?kcid=${id}`);
    },
    // 删除课程任务
    deleteCourseTask(id) {
        return axios.get(`/delCourseTask?taskid=${id}`);
    },
    // 添加课程任务
    addCourseTask(data) {
        return axios.post('/addCourseTask', data);
    },
    // 发布任务
    publishTask(id) {
        return axios.get(`/updateCourseTask?id=${id}`);
    },
    // 获取加入该课程的学生
    getCourseStudent(id) {
        return axios.get(`/getCourseStudent?courseid=${id}`);
    }
};

export default courseApi;
