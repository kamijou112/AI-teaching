<template>
  <div class="mistake-book">
    <div v-if="mistakes.length">
      <div v-for="(mistake, index) in mistakes" :key="index" class="mistake-item">
        <h2>题目 {{ index + 1 }}</h2>
        <p><strong>题目内容：</strong>{{ mistake.question }}</p>
        <p><strong>你的作答：</strong>{{ mistake.studentAnswer }}</p>
        <p><strong>正确答案：</strong>{{ mistake.correctAnswer }}</p>
        <p><strong>AI解析：</strong>{{ mistake.analysis }}</p>
      </div>
    </div>
    <div v-else>
      <p>没有错题记录。</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MistakeBook',
  data() {
    return {
      mistakes: [],
      studentId: null // 初始化学生ID为null
    };
  },
  created() {
    this.loadUser();
    this.fetchMistakes();
  },
  methods: {
    loadUser() {
      // 从localStorage获取用户信息
      const user = JSON.parse(localStorage.getItem('user'));
      if (user && user.data && user.data.id) {
        this.studentId = user.data.id;
      } else {
        console.error('无法获取用户ID');
      }
    },
    async fetchMistakes() {
      if (!this.studentId) {
        console.error('学生ID未设置');
        return;
      }
      try {
        const response = await axios.get(`http://localhost:9327/questions/wrongQuestions/${this.studentId}`);
        this.mistakes = response.data;
      } catch (error) {
        console.error('Error fetching mistakes:', error);
      }
    }
  }
};
</script>

<style scoped>
.mistake-book {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.mistake-item {
  padding: 10px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.mistake-item h2 {
  margin-bottom: 10px;
  color: #333;
}

.mistake-item p {
  margin: 5px 0;
  line-height: 1.5;
}

.mistake-item p strong {
  color: #555;
}
</style>
