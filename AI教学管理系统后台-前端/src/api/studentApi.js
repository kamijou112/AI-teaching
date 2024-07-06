
import axios from '@/server/index';

const studentApi = {
  getAllStudent() {
    return axios.get('/getStudentList');
  },
  updateStudent(id,status) {
    return axios.get(`/updateStudent?id=${id}&status=${status}`);
  },
};

export default studentApi;
