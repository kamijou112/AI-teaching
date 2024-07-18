<template>
  <div class="upload-interface">
    <div class="upload-area">
      <div v-if="newConversation && messages.length === 1" class="pre-inputs-overlay">
        <div class="pre-inputs">
          <div class="pre-inputs-header">
            <p class="pre-inputs-title">智能摘要助手</p>
            <p class="pre-inputs-description">
              嗨~我是摘要助手，上传文档，我可以对文档内容进行摘要！
            </p>
            <p class="pre-inputs-description">
              <strong>上传文档：</strong>点击下方图标即可选择上传文档，支持多种格式；
            </p>
            <p class="pre-inputs-description">
              <strong>助手切换：</strong>一键切换，多个助手帮助你解决多种问题；
            </p>
          </div>
        </div>
      </div>
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.sender]">
        <img v-if="message.sender === 'assistant'" src="@/assets/bot-icon2.png" class="icon" alt="Assistant Icon" />
        <div class="message-content">
          <span v-if="message.files && message.files.length" class="message-files">
            <div v-for="(file, index) in message.files" :key="index" class="message-file">
              <img src="@/assets/file-icon.png" alt="File Icon" class="file-icon" />
              <span class="file-name">{{ file.name }}</span>
            </div>
          </span>
          <span v-if="message.text" class="message-text">{{ message.text }}</span>
          <div v-if="message.loading" class="loading-spinner"></div>
        </div>
      </div>
      <div class="spacer" ref="spacer"></div>
    </div>
    <div class="dialogue-box-container">
      <div class="file-preview-container" v-if="selectedFiles.length">
        <div v-for="(file, index) in selectedFiles" :key="index" class="file-preview">
          <img src="@/assets/file-icon.png" alt="File Icon" class="file-icon" />
          <span class="file-name">{{ file.name }}</span>
          <button @click="removeFile(index)" class="remove-file-button">×</button>
        </div>
      </div>
      <div class="dialogue-box">
        <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" multiple />
        <div class="input-wrapper" @dragover.prevent @drop.prevent="handleDrop">
          <div class="upload-prompt" @click="selectFile">
            <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" fill="#000000" class="upload-icon">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path d="M851.770202 330.529637h-58.858048V318.759051c0-42.146192-34.274561-76.48321-76.48321-76.48321H515.335818c-19.509111 0-60.776809-63.280209-128.282592-63.280209H172.368023c-42.142096 0-76.480138 34.274561-76.480138 76.48321v511.897612c0 41.300462 32.972179 74.858304 73.879469 76.286624 0.585662 0 0.975763 0.263139 1.561425 0.263138h680.441423c42.205577 0 76.546691-34.341114 76.546691-76.549762V407.012847c0-42.145168-34.341114-76.48321-76.546691-76.48321z" fill="#27323A"></path>
                <path d="M188.433807 461.316653v306.775497c0 13.463068-5.26789 24.778025-21.723775 24.91113-14.371254-0.133105-20.874974-11.058985-20.874974-25.626826V255.477818c0-14.634393 11.900619-26.536036 26.531941-26.536035H387.052202c26.079383 0 48.385749 18.210824 68.091446 34.208008 18.340858 14.961012 35.609708 29.073224 60.190123 29.073224h201.093125c14.634393 0 26.536036 11.900619 26.536036 26.536036v11.770586h-176.896667c-40.061561 0-74.272641 54.302782-89.133313 54.302782H264.980498c-42.142096 0.001024-76.546691 34.274561-76.546691 76.484234z" fill="#79CCBF"></path>
                <path d="M557.025357 650.052506h-98.107668l182.363192-240.275169 182.229062 240.275169h-99.342474v142.557602H557.025357z" fill="#FFFFFF"></path>
                <path d="M238.382005 770.629338V461.316653c0-14.631321 11.9641-26.536036 26.599517-26.536036h211.95143c25.203961 0 42.112403-15.607084 58.435183-30.761611 7.805078-7.219415 24.064377-22.242885 31.217239-23.542195h58.858048c-4.618747 2.470635-8.650807 5.917033-11.708128 10.469227l0.912281-1.23583-182.198345 240.246501c-7.675044 9.429984-9.237493 22.763019-3.773018 34.208009 5.594509 11.641576 17.428576 19.186587 30.241477 19.186587h64.742317v110.627737h-289.247604c1.432416-7.676068 3.969603-14.438831 3.969603-23.349704zM878.368695 767.376454c0 14.697874-11.900619 26.602589-26.598493 26.602588h-94.304958V683.351305h66.077463c12.487305 0 23.412162-6.50372 29.136705-17.365095a34.806982 34.806982 0 0 0 3.903051-16.000256c0-6.762763-1.951525-13.463068-5.853553-19.120035l-183.01131-241.217144c-3.642984-4.098613-7.545011-6.959349-11.641577-9.169917h195.695203c14.697874 0 26.598493 11.900619 26.598493 26.536036v360.36156z" fill="#F4CE73"></path>
              </g>
            </svg>
            <p>点击上传，或拖动文档到这里</p>
            <p>支持 doc/docx、pdf、md、txt文件，文件大小不宜过大</p>
          </div>
        </div>
        <button @click="uploadFiles" :disabled="!selectedFiles.length" :class="{ active: selectedFiles.length }">
          上传文件
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FileAssistantContent',
  data() {
    return {
      messages: [
        { text: '您好！我是文件摘要助手，有什么我可以帮你的吗？', sender: 'assistant' }
      ],
      selectedFiles: [],
      loading: false,
      newConversation: true
    };
  },
  methods: {
    selectFile() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const files = Array.from(event.target.files);
      this.selectedFiles = this.selectedFiles.concat(files);
      this.$refs.fileInput.value = null; // 重置文件输入
    },
    handleDrop(event) {
      const files = Array.from(event.dataTransfer.files);
      this.selectedFiles = this.selectedFiles.concat(files);
      event.preventDefault(); // 阻止浏览器默认行为
    },
    removeFile(index) {
      this.selectedFiles.splice(index, 1);
    },
    async uploadFiles() {
      if (!this.selectedFiles.length) {
        return;
      }

      const userMessage = {
        sender: 'user',
        files: this.selectedFiles.map(file => ({ name: file.name, file: file }))
      };

      this.messages.push(userMessage);
      this.selectedFiles = [];
      this.newConversation = false;

      const assistantLoadingMessage = {
        text: '',
        sender: 'assistant',
        loading: true
      };

      this.messages.push(assistantLoadingMessage);

      this.scrollToBottom();

      this.loading = true;

      try {
        const formData = new FormData();
        userMessage.files.forEach(file => {
          formData.append('file', file.file);
        });

        console.log('Uploading files:', formData);

        const uploadResponse = await axios.post('http://localhost:9327/api/documents/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        const uploadData = uploadResponse.data;

        console.log('Upload response:', uploadData);

        if (uploadResponse.status !== 200 || !uploadData.flag) {
          throw new Error(uploadData.desc || '未知错误');
        }

        const fileId = uploadData.data.fileId;
        let summary = null;
        for (let i = 0; i < 10; i++) {
          await new Promise(resolve => setTimeout(resolve, 5000)); // 等待5秒钟

          const summaryResponse = await axios.get('http://localhost:9327/api/documents/summarize', {
            params: { fileId }
          });
          const summaryData = summaryResponse.data;

          console.log('Summary response:', summaryData);

          if (summaryData.data.summaryStatus === 'done') {
            summary = summaryData.data.summary;
            break;
          }
        }

        if (!summary) {
          throw new Error('摘要生成失败');
        }

        console.log('Summary generated:', summary);

        // 更新 assistantLoadingMessage 的内容
        const index = this.messages.indexOf(assistantLoadingMessage);
        if (index !== -1) {
          this.messages[index].loading = false;
          this.messages[index].text = `摘要: ${summary}`;
        }
      } catch (error) {
        assistantLoadingMessage.loading = false;
        assistantLoadingMessage.text = `文件上传或摘要失败: ${error.message}`;
      } finally {
        this.loading = false;
        this.scrollToBottom();
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const spacer = this.$refs.spacer;
        if (spacer) {
          spacer.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
        }
      });
    },
    resetConversation() {
      this.messages = [
        { text: '你好！我是文件摘要助手，有什么我可以帮你的吗？', sender: 'assistant' }
      ];
      this.newConversation = true;
    }
  },
  mounted() {
    this.scrollToBottom();
  }
};
</script>

<style scoped>
.upload-interface {
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
  overflow: hidden;
  height: 100%;
  width: 100%;
}

.upload-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  overflow-y: auto;
  max-height: calc(100vh - 140px);
  position: relative;
}

.pre-inputs-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  z-index: 10;
}

.pre-inputs {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
}

.pre-inputs-header {
  text-align: left;
  margin-bottom: 20px;
}

.pre-inputs-title {
  font-size: 28px;
  font-weight: bold;
  color: #3c7bff;
  margin-bottom: 10px;
}

.pre-inputs-description {
  font-size: 16px;
  color: #000;
}

.message {
  max-width: 60%;
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  display: flex;
  align-items: flex-start;
}

.message.user {
  justify-content: flex-end;
  text-align: right;
}

.message.assistant {
  justify-content: flex-start;
  text-align: left;
}

.message .icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.message-content {
  display: flex;
  flex-direction: column;
  margin-top: 7px;
}

.message-text {
  white-space: pre-wrap;
  word-wrap: break-word; /* Ensure long texts break correctly */
}

.message-files {
  display: flex;
  flex-direction: column;
}

.message-file {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.file-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.file-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 150px;
}

.loading-spinner {
  margin-left: 10px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  width: 16px;
  height: 16px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.dialogue-box-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px 20px;
  margin-bottom: 50px;
  width: 100%;
}

.file-preview-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 800px;
  margin-bottom: 10px;
}

.dialogue-box {
  display: flex;
  width: 100%;
  max-width: 800px;
  position: relative;
  flex-direction: column;
  align-items: center;
}

.input-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: center;
}

.upload-icon {
  width: 48px;
  height: 48px;
  margin-right: 10px;
  cursor: pointer;
}

.upload-prompt {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  border: 2px dashed #ccc;
  border-radius: 10px;
  padding: 40px;
  width: 100%;
  text-align: center;
}

.upload-prompt:hover {
  background-color: #f0f0f0;
}

.file-preview {
  display: flex;
  align-items: center;
  background-color: #f3f3f3;
  padding: 5px 10px;
  border-radius: 30px;
  margin-bottom: 5px;
  position: relative;
}

.remove-file-button {
  background: none;
  border: none;
  color: #ff0000;
  font-size: 16px;
  cursor: pointer;
  position: absolute;
  top: 0;
  right: 0;
  padding: 5px;
}

.dialogue-box button {
  margin-top: 20px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}

.dialogue-box button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.dialogue-box button.active {
  background-color: #0056b3;
}
</style>
