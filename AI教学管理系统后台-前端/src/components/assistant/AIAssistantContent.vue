<template>
  <div class="main-content">
    <div class="chat-area" ref="chatArea">
      <div v-for="(message, index) in messages" :key="index" :class="['message', message.sender]">
        <img v-if="message.sender === 'assistant'" src="../../assets/bot-icon1.png" class="icon" alt="Assistant Icon" />
        <div class="message-content">
          <span v-html="message.text" class="message-text"></span>
          <div v-if="message.loading" class="loading-spinner"></div>
        </div>
      </div>
      <div class="spacer" ref="spacer"></div>
    </div>
    <div class="dialogue-box-container">
      <div class="dialogue-box">
        <div class="input-wrapper">
          <svg fill="#000000" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" class="upload-icon">
            <path d="M4 4h16v2H4V4zm0 7h10v2H4v-2zm0 7h16v2H4v-2z" />
          </svg>
          <input v-model="userInput" @keyup.enter="sendMessage" placeholder="在此输入你的问题..." />
        </div>
        <button @click="sendMessage" :disabled="!userInput.trim()" :class="{ active: userInput.trim() }">
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

<script lang="ts">
import { ref, onMounted, nextTick, defineComponent, Ref, defineExpose } from 'vue';

export default defineComponent({
  name: 'AIAssistantContent',
  setup(_, { expose }) {
    const userInput = ref('');
    const messages = ref<{ text: string; sender: string; loading?: boolean }[]>(
      JSON.parse(localStorage.getItem('messages') || '[]') || [{ text: '你好！有什么我可以帮你的吗？', sender: 'assistant' }]
    );
    const socket: Ref<WebSocket | null> = ref(null);
    const currentAssistantMessage: Ref<{ text: string; sender: string; loading: boolean } | null> = ref(null);
    const loading = ref(false);

    const saveMessages = () => {
      localStorage.setItem('messages', JSON.stringify(messages.value));
    };

    const sendMessage = () => {
      if (userInput.value.trim() !== '') {
        const userMessage = { text: userInput.value, sender: 'user' };
        messages.value.push(userMessage);
        saveMessages();
        userInput.value = '';
        scrollToBottom();

        if (socket.value && socket.value.readyState === WebSocket.OPEN) {
          socket.value.send(JSON.stringify(userMessage));
          currentAssistantMessage.value = { text: '', sender: 'assistant', loading: true };
          messages.value.push(currentAssistantMessage.value);
          loading.value = true;
        } else {
          console.error('WebSocket未连接。');
        }
      }
    };

    const scrollToBottom = () => {
      nextTick(() => {
        const spacer = document.querySelector('.spacer');
        if (spacer) {
          spacer.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
        }
      });
    };

    const connectWebSocket = () => {
      socket.value = new WebSocket('ws://localhost:9327/ws/chat');

      socket.value.onopen = () => {
        console.log('WebSocket连接已建立。');
      };

      socket.value.onmessage = (event) => {
        const formattedData = event.data.replace(/\n/g, '<br/>');
        if (currentAssistantMessage.value) {
          currentAssistantMessage.value.text += formattedData;
          currentAssistantMessage.value.loading = false;
          loading.value = false;
        } else {
          currentAssistantMessage.value = { text: formattedData, sender: 'assistant', loading: false };
          messages.value.push(currentAssistantMessage.value);
          loading.value = false;
        }
        saveMessages();
        scrollToBottom();
      };

      socket.value.onerror = (error) => {
        console.error('WebSocket错误：', error);
      };

      socket.value.onclose = () => {
        console.log('WebSocket连接已关闭。');
      };
    };

    const resetConversation = () => {
      messages.value = [{ text: '你好！有什么我可以帮你的吗？', sender: 'assistant' }];
      saveMessages();
      scrollToBottom();
    };

    onMounted(() => {
      connectWebSocket();
      scrollToBottom();
    });

    expose({ resetConversation });

    return {
      userInput,
      messages,
      loading,
      sendMessage,
    };
  },
});
</script>

<style scoped>
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
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

.dialogue-box {
  display: flex;
  width: 100%;
  max-width: 800px;
  position: relative;
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
  position: absolute;
  left: 10px;
}

.dialogue-box input {
  flex: 1;
  padding: 10px;
  border: 1px solid #d3d3d3;
  border-radius: 30px;
  font-size: 16px;
  padding-left: 40px;
  padding-right: 40px;
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
