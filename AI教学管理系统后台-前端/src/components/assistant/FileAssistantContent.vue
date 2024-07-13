<template>
  <div class="chat-interface">
    <div class="chat-area" ref="chatArea">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.sender]">
        <img v-if="message.sender === 'assistant'" src="../../assets/bot-icon2.png" class="icon" alt="Assistant Icon" />
        <div class="message-content">
          <span v-if="message.files && message.files.length" class="message-files">
            <div v-for="(file, index) in message.files" :key="index" class="message-file">
              <img src="../../assets/file-icon2.png" alt="File Icon" class="file-icon" />
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
          <img src="../../assets/file-icon.png" alt="File Icon" class="file-icon" />
          <span class="file-name">{{ file.name }}</span>
          <button @click="removeFile(index)" class="remove-file-button">×</button>
        </div>
      </div>
      <div class="dialogue-box">
        <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" multiple />
        <div class="input-wrapper">
          <svg @click="selectFile" viewBox="-8 0 32 32" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:sketch="http://www.bohemiancoding.com/sketch/ns" fill="#000000" class="upload-icon">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <title>attachment</title>
              <desc>Created with Sketch Beta.</desc>
              <defs></defs>
              <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd" sketch:type="MSPage">
                <g id="Icon-Set" sketch:type="MSLayerGroup" transform="translate(-212.000000, -151.000000)" fill="#000000">
                  <path d="M226,155 L226,175 C226,178.313 223.313,181 220,181 C216.687,181 214,178.313 214,175 L214,157 C214,154.791 215.791,153 218,153 C220.209,153 222,154.791 222,157 L222,175 C222,176.104 221.104,177 220,177 C218.896,177 218,176.104 218,175 L218,159 L216,159 L216,175 C216,177.209 217.791,179 220,179 C222.209,179 224,177.209 224,175 L224,157 C224,153.687 221.313,151 218,151 C214.687,151 212,153.687 212,157 L212,176 C212.493,179.945 215.921,183 220,183 C224.079,183 227.507,179.945 228,176 L228,155 L226,155" id="attachment" sketch:type="MSShapeGroup"></path>
                </g>
              </g>
            </g>
          </svg>
          <input v-model="userInput" @keyup.enter="sendMessage" placeholder="在此输入你的问题..." />
        </div>
        <button @click="sendMessage" :disabled="!userInput.trim() && !selectedFiles.length" :class="{ active: userInput.trim() || selectedFiles.length }">
          <svg v-if="loading" fill="#000000" viewBox="0 0 56 56" xmlns="http://www.w3.org/2000/svg">
            <path d="M 27.9999 51.9063 C 41.0546 51.9063 51.9063 41.0781 51.9063 28 C 51.9063 14.9453 41.0312 4.0937 27.9765 4.0937 C 14.8983 4.0937 4.0937 14.9453 4.0937 28 C 4.0937 41.0781 14.9218 51.9063 27.9999 51.9063 Z M 21.8593 36.4609 C 20.7812 36.4609 20.3124 35.8984 20.3124 35.0312 L 20.3124 20.9922 C 20.3124 20.1484 20.7812 19.5625 21.8593 19.5625 L 23.9921 19.5625 C 25.0702 19.5625 25.5390 20.1484 25.5390 20.9922 L 25.5390 35.0312 C 25.5390 35.8984 25.0702 36.4609 23.9921 36.4609 Z M 31.9843 36.4609 C 30.9296 36.4609 30.4140 35.8984 30.4140 35.0312 L 30.4140 20.9922 C 30.4140 20.1484 30.9296 19.5625 31.9843 19.5625 L 34.1171 19.5625 C 35.1718 19.5625 35.6640 20.1484 35.6640 20.9922 L 35.6640 35.0312 C 35.6640 35.8984 35.1718 36.4609 34.1171 36.4609 Z"></path>
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 6V18M12 6L7 11M12 6L17 11" stroke="#fcfcfc" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FileAssistantContent',
  data() {
    return {
      userInput: '',
      messages: [
        { text: '你好！我是图片助手，有什么我可以帮你的吗？', sender: 'assistant' }
      ],
      loading: false,
      selectedFiles: []
    };
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim() && !this.selectedFiles.length) {
        return;
      }

      const userMessage = {
        text: this.userInput,
        sender: 'user',
        files: this.selectedFiles.map(file => ({ name: file.name, file: file }))
      };

      this.messages.push(userMessage);
      this.userInput = '';
      this.selectedFiles = [];

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
          formData.append('file', file.file); // Correctly append the file object
        });
        formData.append('question', userMessage.text);

        const response = await fetch('http://localhost:9327/api/image/analyze', {
          method: 'POST',
          body: formData
        });

        const data = await response.json();

        if (response.status !== 200 || data.status === 'error') {
          throw new Error(data.message || 'Unknown error');
        }

        assistantLoadingMessage.loading = false;
        assistantLoadingMessage.text = `${data.answer}`;
      } catch (error) {
        assistantLoadingMessage.loading = false;
        assistantLoadingMessage.text = `Error: ${error.message}`;
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
    selectFile() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const files = Array.from(event.target.files);
      this.selectedFiles = this.selectedFiles.concat(files);
      this.$refs.fileInput.value = null; // Reset file input
    },
    removeFile(index) {
      this.selectedFiles.splice(index, 1);
    },
    resetConversation() {
      this.userInput = '';
      this.selectedFiles = [];
      this.messages = [
        { text: '你好！我是图片助手，有什么我可以帮你的吗？', sender: 'assistant' }
      ];
    }
  },
  mounted() {
    this.scrollToBottom();
  }
};
</script>

<style scoped>
.chat-interface {
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
  overflow: hidden;
  height: 100%;
  width: 100%;
}

.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  overflow-y: auto;
  max-height: calc(100vh - 140px);
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
}

.dialogue-box input[type="file"] {
  display: none;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  flex: 1;
}

.upload-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
  cursor: pointer;
  position: absolute;
  left: 10px;
}

.dialogue-box input {
  flex: 1;
  padding: 10px;
  border: 1px solid #d3d3d3;
  border-radius: 30px;
  font-size: 16px;
  padding-left: 40px; /* 为SVG图标预留空间 */
  padding-right: 40px; /* 为按钮预留空间 */
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
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background-color: #d3d3d3;
  color: #fff;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialogue-box button.active {
  background-color: #000;
}

.dialogue-box button:disabled {
  cursor: not-allowed;
}
</style>
