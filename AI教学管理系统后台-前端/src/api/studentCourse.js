import axios from '@/server/index';

const studentCourseApi = {
    // 获取学生加入的课程
    getCourseListByStudent(id) {
        return axios.get(`/student/getCourseListByStudent?studentid=${id}`);
    },
    // 课程搜索
    studentGetCourse(wordkey) {
        return axios.get(`/student/getCourse?wordkey=${wordkey}`);
    },
    // 学生加入课程
    studentJoinCourse(studentid, kcid) {
        return axios.get(`/student/addCourse?studentid=${studentid}&kcid=${kcid}`);
    },
    // 退出课程
    studentQuitCourse(studentid, kcid) {
        return axios.get(`/student/delCourse?studentid=${studentid}&kcid=${kcid}`);
    },
    // 获取课程任务
    getCourseTask(kcid, studentid) {
        return axios.get(`/student/getCourseTasks?kcid=${kcid}&studentid=${studentid}`);
    },
    // 回答题目
    studentAnswer(studentid, taskid) {
        return axios.get(`/student/Answer?studentid=${studentid}&taskid=${taskid}`);
    },
    // 获取任务进度详情
    getTaskProgress(tasklogid) {
        return axios.get(`/student/MissionDetails?tasklogid=${tasklogid}`);
    },
    // 更新答案
    updateAnswer(tasklogid, timuid, content) {
        return axios.get(`/student/updateAnswer?tasklogid=${tasklogid}&timuid=${timuid}&content=${content}`);
    },
    // 提交答案
    answerSubmission(tasklog) {
        return axios.get(`/student/AnswerSubmission?tasklog=${tasklog}`);
    }
};

export default studentCourseApi;
