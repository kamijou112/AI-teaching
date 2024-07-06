
import axios from '@/server/index';

const signInApi = {
  // 老师登录
  login(data) {
    return axios.post('/login', data);
  },
  // 学生注册
  studentRegister(data) {
    return axios.post('/student/enroll', data);
  },
  // 学生登录
  studentLogin(data) {
    return axios.post('/student/login', data);
  },
  // 修改学生信息
  updateStudent(data) {
    return axios.post('/student/updateStudent', data);
  },
  // 修改老师信息
  updateTeacher(data) {
    return axios.post('/UpdateInfo', data);
  }
};

export default signInApi;
