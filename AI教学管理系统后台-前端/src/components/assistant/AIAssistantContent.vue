<template>
  <div class="main-content">
    <div class="chat-area" ref="chatArea">
      <div v-if="newConversation && messages.length === 0" class="pre-inputs-overlay">
        <div class="pre-inputs">
          <div class="pre-inputs-header">
            <p class="pre-inputs-title">智能问答助手</p>
            <p class="pre-inputs-description">
              嗨~我是问答助手，有什么不懂的，都来问我吧！
            </p>
            <p class="pre-inputs-description">
              <strong>推荐问题：</strong>已为您预置了多个常见的热门问题，只需轻轻一点，即可快速获取详细解答，帮助您更好地了解相关知识
            </p>
            <p class="pre-inputs-description">
              <strong>助手切换：</strong>一键切换，多个助手帮助你解决多种问题；
            </p>
          </div>
          <div class="pre-input" v-for="(input, index) in preInputs" :key="index" @click="sendPreInput(input)">
            <svg viewBox="0 0 1024 1024" class="icon" version="1.1" xmlns="http://www.w3.org/2000/svg" fill="#000000">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path d="M622.4 682.453333l60.330667-60.309333 256.405333 256.405333-60.330667 60.309334z" fill="#616161"></path>
                <path d="M426.666667 426.666667m-341.333334 0a341.333333 341.333333 0 1 0 682.666667 0 341.333333 341.333333 0 1 0-682.666667 0Z" fill="#616161"></path>
                <path d="M692.266667 753.92l60.309333-60.330667 185.514667 185.514667-60.330667 60.330667z" fill="#37474F"></path>
                <path d="M426.666667 426.666667m-277.333334 0a277.333333 277.333333 0 1 0 554.666667 0 277.333333 277.333333 0 1 0-554.666667 0Z" fill="#64B5F6"></path>
                <path d="M573.866667 302.933333c-36.266667-42.666667-89.6-68.266667-147.2-68.266666s-110.933333 25.6-147.2 68.266666c-8.533333 8.533333-6.4 23.466667 2.133333 29.866667 8.533333 8.533333 23.466667 6.4 29.866667-2.133333C341.333333 296.533333 381.866667 277.333333 426.666667 277.333333s85.333333 19.2 115.2 53.333334c4.266667 4.266667 10.666667 8.533333 17.066666 8.533333 4.266667 0 10.666667-2.133333 12.8-4.266667 8.533333-8.533333 8.533333-23.466667 2.133334-32z" fill="#BBDEFB"></path>
              </g>
            </svg>
            {{ input }}
          </div>
        </div>
      </div>
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
          <button @click="toggleSpeechRecognition" :class="['speech-button', { active: recognizing }]">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 14C13.6569 14 15 12.6569 15 11V5C15 3.34315 13.6569 2 12 2C10.3431 2 9 3.34315 9 5V11C9 12.6569 10.3431 14 12 14Z" stroke="#fcfcfc" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
              <path d="M5 11V12C5 15.3137 7.68629 18 11 18H13C16.3137 18 19 15.3137 19 12V11" stroke="#fcfcfc" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
              <path d="M12 18V22M8 22H16" stroke="#fcfcfc" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
            </svg>
          </button>
          <input v-model="userInput" @keyup.enter="sendMessage" placeholder="在此输入你的问题..." />
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
  </div>
</template>

<script lang="ts">
  import { ref, onMounted, nextTick, defineComponent, Ref, defineExpose } from 'vue';

  export default defineComponent({
    name: 'AIAssistantContent',
    setup(_, { expose }) {
      const userInput = ref('');
      const messages = ref<{ text: string; sender: string; loading?: boolean }[]>([]);
      const preInputs = ref(['什么是物联网（IoT）？', '物联网中的边缘计算和云计算有什么区别？', '常用的物联网开发工具和框架有哪些？','物联网未来的发展方向和挑战有哪些？']);
      const newConversation = ref(true);
      const socket: Ref<WebSocket | null> = ref(null);
      const currentAssistantMessage: Ref<{ text: string; sender: string; loading: boolean } | null> = ref(null);
      const loading = ref(false);
      const recognizing = ref(false);
      let recognition: any = null;

      if (!('SpeechRecognition' in window)) {
        window.SpeechRecognition = window.webkitSpeechRecognition;
      }

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
          newConversation.value = false;
        }
      };

      const sendPreInput = (input: string) => {
        userInput.value = input;
        sendMessage();
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
        messages.value = [{ text: '您好！我是AI问答助手，有什么我可以帮你的吗？', sender: 'assistant' }];
        saveMessages();
        scrollToBottom();
        newConversation.value = true;
      };

      const startSpeechRecognition = () => {
        recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
        recognition.lang = 'zh-CN';
        recognition.interimResults = true;
        recognition.continuous = true;

        recognition.onstart = () => {
          recognizing.value = true;
          console.log('语音识别启动');
        };

        recognition.onresult = (event: any) => {
          let interimTranscript = '';
          for (let i = event.resultIndex; i < event.results.length; ++i) {
            if (event.results[i].isFinal) {
              userInput.value += event.results[i][0].transcript;
            } else {
              interimTranscript += event.results[i][0].transcript;
            }
          }
          console.log('中间结果: ', interimTranscript);
        };

        recognition.onerror = (event: any) => {
          console.error('语音识别错误: ', event.error);
        };

        recognition.onend = () => {
          recognizing.value = false;
          console.log('语音识别结束');
        };

        recognition.start();
      };

      const stopSpeechRecognition = () => {
        if (recognition) {
          recognition.stop();
        }
      };

      const toggleSpeechRecognition = () => {
        if (recognizing.value) {
          stopSpeechRecognition();
        } else {
          startSpeechRecognition();
        }
      };

      onMounted(() => {
        connectWebSocket();
        scrollToBottom();
      });

      expose({ resetConversation });

      return {
        userInput,
        messages,
        preInputs,
        newConversation,
        loading,
        recognizing,
        sendMessage,
        sendPreInput,
        toggleSpeechRecognition,
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

  .pre-inputs-link {
    color: #3c7bff;
    text-decoration: none;
  }

  .pre-inputs-link:hover {
    text-decoration: underline;
  }

  .pre-input {
    padding: 10px 20px;
    margin: 10px 0;
    background-color: #ced1d4;
    color: #030303;
    border-radius: 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    display: flex;
    align-items: center;
  }

  .pre-input svg {
    margin-right: 10px;
    width: 20px;
    height: 20px;
  }

  .pre-input:hover {
    background-color: #ffffff;
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
    width: 100%;
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
    left: 40px;
    cursor: pointer;
  }

  .dialogue-box input {
    flex: 1;
    padding: 10px;
    border: 1px solid #d3d3d3;
    border-radius: 30px;
    font-size: 16px;
    padding-left: 70px;
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

  .speech-button {
    position: absolute;
    left: 10px;
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

  .speech-button.active {
    background-color: #000;
  }
</style>
