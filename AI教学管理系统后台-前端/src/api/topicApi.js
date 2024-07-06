
import axios from '@/server/index';

const topicApi = {
  // 获取题目列表及详情
  getTopicDetile(id) {
    return axios.get(`/getTopicDetile?taskid=${id}`);
  },

  // 删除题目
  delTopic(id) {
    return axios.get(`/delTopic?timuid=${id}`);
  },
  // 创建题目
  AddTopic(data) {
    return axios.post('/AddTopic', data);
  },
  // 编辑题目
  UpdateTopic(data) {
    return axios.post('/UpdateTopic', data);
  },

};

export default topicApi;
