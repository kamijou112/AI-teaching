<template>
  <div class="chat-interface">
    <div class="sidebar">
      <el-dropdown @command="handleCommand" class="dropdown-wrapper">
        <span class="el-dropdown-link">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg" class="icon">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <circle cx="13.7146" cy="13.7143" r="6.85714" fill="#2F88FF" stroke="#000000" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"></circle>
              <circle cx="34.2859" cy="34.2857" r="6.85714" fill="#2F88FF" stroke="#000000" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"></circle>
              <path d="M24.001 44.0002C12.9553 44.0002 4.00098 35.0459 4.00098 24.0002L10.6676 27.3335" stroke="#000000" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"></path>
              <path d="M24.001 4.00021C35.0467 4.00021 44.001 12.9545 44.001 24.0002L37.3343 20.6669" stroke="#000000" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"></path>
            </g>
          </svg>
          <strong>更换助手</strong>
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="AI助手">问答助手</el-dropdown-item>
            <el-dropdown-item command="文件助手">图片助手</el-dropdown-item>
            <el-dropdown-item command="摘要助手">摘要助手</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <div class="new-conversation-icon-wrapper" @mouseover="showTooltip" @mouseout="hideTooltip">
        <svg fill="#000000" viewBox="0 0 22 22" xmlns="http://www.w3.org/2000/svg" id="memory-plus-circle-fill" class="new-conversation-icon" @click="newConversation">
          <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
          <g id="SVGRepo_tracerCarrier"></g>
          <g id="SVGRepo_iconCarrier">
            <path d="M15,21H7V20H5V19H4V18H3V17H2V15H1V7H2V5H3V4H4V3H5V2H7V1H15V2H17V3H18V4H19V5H20V7H21V15H20V17H19V18H18V19H17V20H15ZM12,16V12H16V10H12V6H10V10H6V12H10V16Z"></path>
          </g>
        </svg>
        <div class="tooltip" v-if="tooltipVisible || true">创建新会话</div>
      </div>
    </div>
    <div class="main-content">
      <component :is="currentAssistantComponent" ref="currentAssistantRef" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineAsyncComponent, nextTick } from 'vue';
import { ArrowDown } from '@element-plus/icons-vue';

const tooltipVisible = ref(false);
const currentAssistant = ref('AI助手');

const AIAssistant = defineAsyncComponent(() => import('./AIAssistantContent.vue'));
const FileAssistant = defineAsyncComponent(() => import('./FileAssistantContent.vue'));
const SummaryAssistant = defineAsyncComponent(() => import('./SummaryAssistantContent.vue'));

const assistants = {
  'AI助手': AIAssistant,
  '文件助手': FileAssistant,
  '摘要助手': SummaryAssistant
};

type AssistantComponentInstance = {
  resetConversation: () => void;
};

const currentAssistantComponent = ref(assistants['AI助手']);
const currentAssistantRef = ref<InstanceType<typeof AIAssistant | typeof FileAssistant | typeof SummaryAssistant> & AssistantComponentInstance | null>(null);

const handleCommand = (command: string) => {
  currentAssistant.value = command;
  currentAssistantComponent.value = assistants[command];
  nextTick(() => {
    (currentAssistantRef.value as unknown as AssistantComponentInstance)?.resetConversation();
  });
};

const showTooltip = () => {
  tooltipVisible.value = true;
};

const hideTooltip = () => {
  tooltipVisible.value = false;
};

const newConversation = () => {
  (currentAssistantRef.value as unknown as AssistantComponentInstance)?.resetConversation();
};
</script>

<style scoped>
.chat-interface {
  display: flex;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.sidebar {
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  padding-top: 30px;
  position: relative;
  background-color: #f9f9f9;
}

.dropdown-wrapper {
  margin-bottom: 20px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #000;
  display: flex;
  align-items: center;
  font-size: 22px;
  margin-left: 40px; /* Add margin to move it to the right */
}

.icon {
  width: 24px;
  height: 24px;
  margin-right: 5px;
}

.new-conversation-icon-wrapper {
  display: flex;
  align-items: center;
  position: relative;
  margin-top: auto;
  margin-bottom: auto;
  margin-left: 40px; /* Add margin to align with the dropdown */
}

.new-conversation-icon {
  width: 40px;
  height: 40px;
  cursor: pointer;
  margin: 20px 0;
}

.tooltip {
  position: absolute;
  top: 50%;
  left: 45px;
  transform: translateY(-50%);
  background-color: #333;
  color: #fff;
  padding: 5px;
  border-radius: 5px;
  white-space: nowrap;
  font-size: 14px;
  z-index: 10;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f9f9f9;
}
</style>
