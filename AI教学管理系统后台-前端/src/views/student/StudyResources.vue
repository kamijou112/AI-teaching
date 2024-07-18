<template>
  <div class="resource-container">
    <div class="study-result">
      <p><strong>近期学习成果评价</strong></p>
      <div v-if="studyResult" v-html="formattedStudyResult"></div>
      <div v-else class="empty-icon">
        <svg viewBox="0 0 64 64" xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="#000000">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path d="M32 0C14.327 0 0 14.327 0 32s14.327 32 32 32 32-14.327 32-32S49.673 0 32 0zm0 60C15.458 60 4 48.542 4 32S15.458 4 32 4s28 11.458 28 28-11.458 28-28 28zm12-36c0-2.209-1.791-4-4-4H24c-2.209 0-4 1.791-4 4v20c0 2.209 1.791 4 4 4h16c2.209 0 4-1.791 4-4V24zm-4 20H24V24h16v20z"></path>
          </g>
        </svg>
      </div>
    </div>
    <div class="resource-card" @click="showPopup('books')">
      <div class="icon">
        <svg version="1.0" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 64 64" enable-background="new 0 0 64 64" xml:space="preserve" fill="#000000">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <g>
              <path fill="#F9EBB2" d="M56,62H10c-2.209,0-4-1.791-4-4s1.791-4,4-4h46V62z"></path>
              <g>
                <path fill="#45AAB8" d="M6,4v49.537C7.062,52.584,8.461,52,10,52h2V2H8C6.896,2,6,2.896,6,4z"></path>
                <path fill="#45AAB8" d="M56,2H14v50h42h2v-2V4C58,2.896,57.104,2,56,2z"></path>
              </g>
              <g>
                <path fill="#394240" d="M60,52V4c0-2.211-1.789-4-4-4H8C5.789,0,4,1.789,4,4v54c0,3.313,2.687,6,6,6h49c0.553,0,1-0.447,1-1 s-0.447-1-1-1h-1v-8C59.104,54,60,53.104,60,52z M6,4c0-1.104,0.896-2,2-2h4v50h-2c-1.539,0-2.938,0.584-4,1.537V4z M56,62H10 c-2.209,0-4-1.791-4-4s1.791-4,4-4h46V62z M56,52H14V2h42c1.104,0,2,0.896,2,2v46v2H56z"></path>
                <path fill="#394240" d="M43,26H23c-0.553,0-1,0.447-1,1s0.447,1,1,1h20c0.553,0,1-0.447,1-1S43.553,26,43,26z"></path>
                <path fill="#394240" d="M49,20H23c-0.553,0-1,0.447-1,1s0.447,1,1,1h26c0.553,0,1-0.447,1-1S49.553,20,49,20z"></path>
                <path fill="#394240" d="M23,16h12c0.553,0,1-0.447,1-1s-0.447-1-1-1H23c-0.553,0-1,0.447-1,1S22.447,16,23,16z"></path>
              </g>
              <path opacity="0.2" fill="#231F20" d="M6,4v49.537C7.062,52.584,8.461,52,10,52h2V2H8C6.896,2,6,2.896,6,4z"></path>
            </g>
          </g>
        </svg>
      </div>
      <p>书籍推荐</p>
    </div>
    <div class="resource-card" @click="showPopup('websites')">
      <div class="icon">
        <svg viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" fill="#000000">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <title>file_type_source</title>
            <path d="M14.194,27a1.2,1.2,0,0,1-.276-.033,1.388,1.388,0,0,1-.968-1.618L16.562,6.105a1.383,1.383,0,0,1,.592-.91,1.192,1.192,0,0,1,.928-.163,1.39,1.39,0,0,1,.969,1.617L15.436,25.9a1.378,1.378,0,0,1-.59.908A1.2,1.2,0,0,1,14.194,27Z" fill="#06cc14"></path>
            <path d="M21.437,24.273l-.091,0a1.242,1.242,0,0,1-.891-.5,1.461,1.461,0,0,1,.136-1.893L26.8,15.807l-6.185-5.652a1.462,1.462,0,0,1-.187-1.888,1.25,1.25,0,0,1,.881-.533,1.2,1.2,0,0,1,.945.316l7.294,6.668a1.463,1.463,0,0,1,.191,1.889,1.415,1.415,0,0,1-.189.218l-7.265,7.1A1.222,1.222,0,0,1,21.437,24.273Z" fill="#06cc14"></path>
            <path d="M10.563,24.277a1.219,1.219,0,0,1-.852-.355l-7.271-7.1a1.2,1.2,0,0,1-.182-.21,1.459,1.459,0,0,1,.188-1.886l7.3-6.67a1.175,1.175,0,0,1,.938-.317,1.254,1.254,0,0,1,.887.53,1.462,1.462,0,0,1-.187,1.89L5.2,15.809l6.212,6.069a1.457,1.457,0,0,1,.133,1.893,1.235,1.235,0,0,1-.893.5C10.622,24.275,10.593,24.277,10.563,24.277Z" fill="#06cc14"></path>
          </g>
        </svg>
      </div>
      <p>网址推荐</p>
    </div>
    <div class="resource-card" @click="showPopup('courses')">
      <div class="icon">
        <svg viewBox="0 0 1024 1024" class="icon" version="1.1" xmlns="http://www.w3.org/2000/svg" fill="#000000">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <path d="M759.3 352h84.3c15.5 0 28.1 9.2 28.1 20.5v277.3c0 11.3-12.6 20.5-28.1 20.5h-70.3" fill="#8CAAFF"></path>
            <path d="M843.5 695h-70.2v-49.5h70.2c1.4 0 2.5-0.2 3.3-0.4v-268c-0.9-0.2-2-0.4-3.3-0.4h-84.2v-49.5h84.3c29.6 0 52.8 19.9 52.8 45.3v277.3c0 25.4-23.3 45.2-52.9 45.2z" fill="#333333"></path>
            <path d="M203.5 258h531c13.7 0 24.7 11.1 24.7 24.7v457.8c0 13.7-11.1 24.7-24.7 24.7h-531c-13.7 0-24.7-11.1-24.7-24.7V282.7c-0.1-13.6 11-24.7 24.7-24.7z" fill="#FFFFFF"></path>
            <path d="M734.5 790h-531c-27.3 0-49.5-22.2-49.5-49.5V282.7c0-27.3 22.2-49.5 49.5-49.5h531c27.3 0 49.5 22.2 49.5 49.5v457.8c0 27.3-22.2 49.5-49.5 49.5z m-531-507.3v457.8h531V282.7h-531z" fill="#333333"></path>
            <path d="M595.8 521.2L407 656c-5.6 4-13.3 2.7-17.3-2.9-1.5-2.1-2.3-4.6-2.3-7.2V376.3c0-6.8 5.5-12.4 12.4-12.4 2.6 0 5.1 0.8 7.2 2.3L595.8 501c5.6 4 6.9 11.7 2.9 17.3-0.8 1.1-1.8 2.1-2.9 2.9z" fill="#8CAAFF"></path>
            <path d="M399.9 683c-2.1 0-4.1-0.2-6.2-0.5-9.8-1.6-18.4-7-24.1-15.1-4.5-6.3-6.9-13.8-6.9-21.6V376.3c0-20.5 16.6-37.1 37.1-37.1 7.8 0 15.2 2.4 21.6 6.9l188.8 134.8c8.1 5.8 13.4 14.3 15 24.1 1.6 9.8-0.6 19.6-6.4 27.7-2.4 3.4-5.3 6.3-8.7 8.7L421.3 676.2c-6.3 4.5-13.8 6.8-21.4 6.8z m12.2-282.6v221.5l155.1-110.7-155.1-110.8zM581.4 501s0 0.1 0 0z" fill="#333333"></path>
          </g>
        </svg>
      </div>
      <p>课程推荐</p>
    </div>
    <div class="resource-card" @click="showPopup('papers')">
      <div class="icon">
        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" fill="#000000">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
          <g id="SVGRepo_iconCarrier">
            <g id="Icons">
              <path fill="#f19b5f" d="M21,10.66V20a3,3,0,0,1-3,3H6a3,3,0,0,1-3-3V4A3,3,0,0,1,6,1h5.34a4,4,0,0,1,2.83,1.17l5.66,5.66A4,4,0,0,1,21,10.66Z"></path>
              <path fill="#ffce69" d="M21,10.66V15a3,3,0,0,1-3,3H6a3,3,0,0,1-3-3V4A3,3,0,0,1,6,1h5.34a4,4,0,0,1,2.83,1.17l5.66,5.66A4,4,0,0,1,21,10.66Z"></path>
              <path fill="#ffe5b6" d="M20.64,9H16a3,3,0,0,1-3-3V1.36a4.089,4.089,0,0,1,1.17.81l5.66,5.66A4.089,4.089,0,0,1,20.64,9Z"></path>
            </g>
            <g data-name="Layer 4" id="Layer_4">
              <path fill="#6c2e7c" d="M6,24H18a4,4,0,0,0,4-4V10.657a4.968,4.968,0,0,0-.433-2.016.85.85,0,0,0-.055-.1,4.976,4.976,0,0,0-.977-1.416L14.879,1.464A4.981,4.981,0,0,0,13.462.487c-.035-.018-.066-.04-.1-.055A4.984,4.984,0,0,0,11.343,0H6A4,4,0,0,0,2,4V20A4,4,0,0,0,6,24ZM18.586,8H16a2,2,0,0,1-2-2V3.414ZM4,4A2,2,0,0,1,6,2h5.343A3,3,0,0,1,12,2.078V6a4,4,0,0,0,4,4h3.92a2.953,2.953,0,0,1,.08.657V20a2,2,0,0,1-2,2H6a2,2,0,0,1-2-2Z"></path>
              <path fill="#6c2e7c" d="M7,20H17a1,1,0,0,0,0-2H7a1,1,0,0,0,0,2Z"></path>
              <path fill="#6c2e7c" d="M7,16h4a1,1,0,0,0,0-2H7a1,1,0,0,0,0,2Z"></path>
            </g>
          </g>
        </svg>
      </div>
      <p>论文推荐</p>
    </div>

    <Popup v-if="showPopupFlag" :type="popupType" :studentResult="studyResult" @close="showPopupFlag = false" />
  </div>
</template>

<script>
import Popup from './Popup.vue';
import axios from 'axios';

export default {
  name: 'StudyResources',
  components: {
    Popup,
  },
  data() {
    return {
      showPopupFlag: false,
      popupType: '',
      studyResult: '',
    };
  },
  created() {
    const userData = JSON.parse(localStorage.getItem('user'));
    if (userData && userData.data && userData.data.id) {
      const userId = userData.data.id;
      this.fetchStudyResult(userId);
    }
  },
  computed: {
    formattedStudyResult() {
      return this.studyResult.replace(/\n/g, '<br>'); // 替换换行符为 <br>
    },
  },
  methods: {
    async fetchStudyResult(userId) {
      try {
        const response = await axios.get(`http://localhost:9327/api/student/getStudyResult`, {
          params: { studentid: userId }
        });
        this.studyResult = response.data.data || '';
      } catch (error) {
        console.error('获取学习结果时出错:', error);
        this.studyResult = ''; // 如果出错，将其设置为空
      }
    },
    showPopup(type) {
      this.popupType = type;
      this.showPopupFlag = true;
    },
  },
};
</script>

<style scoped>
.resource-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 使父容器占满整个视口高度 */
  gap: 20px; /* 增加卡片之间的间距 */
  padding: 20px;
  flex-wrap: wrap; /* 允许子元素换行 */
}

.study-result {
  position: absolute;
  top: 80px;
  width: 80%;
  max-width: 800px;
  height: 45vh; /* 固定高度 */
  overflow-y: auto; /* 超出内容出现滚动条 */
  padding: 20px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  z-index: 1;
  word-wrap: break-word;
}

.empty-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.resource-card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  width: 200px;
  text-align: center;
  position: relative;
  cursor: pointer; /* 添加鼠标指针样式 */
  z-index: 0;
  margin-top: 200px;
}

.icon {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 24px;
  height: 24px;
}

.resource-card p {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  margin-top: 40px; /* 为了避免文字与图标重叠 */
}

.resource-card ul {
  list-style-type: none;
  padding: 0;
}

.resource-card ul li {
  margin: 5px 0;
}
</style>
