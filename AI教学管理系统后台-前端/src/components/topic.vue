<script setup>
import topicApi from '@/api/topicApi';
import useTime from '@/composables/useTime';
import { id } from 'element-plus/es/locales.mjs';
import { watch, ref, reactive, defineExpose } from 'vue';

const generateAIQuestions = async (requirements) => {
    try {
        const response = await fetch('http://localhost:9327/api/ai/generateQuestions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ requirements }),
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return await response.json();
    } catch (error) {
        console.error('Failed to generate AI questions:', error);
        return [];
    }
};

const kcId = ref();
const search = ref();
const topicList = ref([]);
const addTopicPopUps = ref(false);
const aiPopUps = ref(false);
const editTopicPopUps = ref(false);
const topicForm = reactive({
    taskid: "",
    type: "",
    tigan: "",
    xuanxiang: "",
    daan: ""
});
const editTopicForm = reactive({
    taskid: "",
    type: "",
    tigan: "",
    xuanxiang: "",
    daan: "",
    id
});

const aiRequirement = ref('');
const chatMessages = ref([
    { type: 'assistant', text: '我是一个出题助手，请输入你想生成题目的需求，例如题目类型和难度。' }
]);

const props = defineProps({
    courseTaskDetailsId: {
        type: String,
        required: true
    },
});

const numberToLetter = {
    1: 'A',
    2: 'B',
    3: 'C',
    4: 'D',
};

const convertNumberToLetter = (number) => {
    return numberToLetter[number];
};

const getTopic = async (id) => {
    const { data } = await topicApi.getTopicDetile(id);
    topicList.value = data.data;
}

const searchCourse = () => {
    if (search.value === '') {
        ElMessage({
            message: '请输入题目名称',
            type: 'warning',
        })
    } else {
        topicList.value = topicList.value.filter(item => item.tigan.includes(search.value));
        ElMessage({
            message: '搜索成功',
            type: 'success',
        })
    }
}

const deleteTopic = async (id) => {
    try {
        await ElMessageBox.confirm(`确定要删除该题目吗?`, "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "error"
        })

        try {
            await topicApi.delTopic(id);
            ElMessage({
                message: '删除成功',
                type: 'success',
            })
            await getTopic(kcId.value);
        } catch (e) {
            ElMessage({
                message: '删除失败',
                type: 'error',
            })
        }
    } catch (err) {
        ElMessage('已取消')
    }
}

const addTopic = async () => {
    if (topicForm.tigan === '') {
        ElMessage({
            message: '请输入题目名称',
            type: 'warning',
        })
        return
    }

    if (topicForm.type === "") {
        ElMessage({
            message: '请输入题目类型',
            type: 'warning',
        })
        return
    }

    topicForm.taskid = kcId.value;
    try {
        await topicApi.AddTopic(topicForm);
        ElMessage({
            message: '添加成功',
            type: 'success',
        })
        await getTopic(kcId.value);
        topicForm.type = "";
        topicForm.tigan = "";
        topicForm.xuanxiang = "";
        topicForm.daan = "";
        addTopicPopUps.value = false;

    } catch (e) {
        ElMessage({
            message: '添加失败',
            type: 'error',
        })
    }
}

// AI 生成题目并添加
const addAIQuestions = async () => {
    try {
        const requirements = aiRequirement.value; // 获取用户输入的AI需求
        const generatedQuestions = await generateAIQuestions(requirements);
        for (const question of generatedQuestions) {
            question.taskid = kcId.value;
            await topicApi.AddTopic(question);
        }
        chatMessages.value.push({ type: 'assistant', text: '创建题目成功，还有需要请输入新的出题需求需求。' });
        ElMessage({
            message: 'AI 生成题目并添加成功',
            type: 'success',
        });
        await getTopic(kcId.value);
    } catch (e) {
        chatMessages.value.push({ type: 'assistant', text: '生成失败，请重试。' });
        ElMessage({
            message: 'AI 生成题目失败',
            type: 'error',
        });
    }
}

const sendMessage = () => {
    if (aiRequirement.value.trim() !== '') {
        chatMessages.value.push({ type: 'user', text: aiRequirement.value });
        addAIQuestions();
        aiRequirement.value = '';
    }
}

const resetData = () => topicList.value = [];

const editTopic = ({ taskid, type, tigan, xuanxiang, daan, id }) => {
    editTopicForm.taskid = taskid;
    editTopicForm.type = type;
    editTopicForm.tigan = tigan;
    editTopicForm.xuanxiang = xuanxiang;
    editTopicForm.daan = daan;
    editTopicForm.id = id;
    editTopicPopUps.value = true;
}

const preservation = async () => {
    try {
        await topicApi.UpdateTopic(editTopicForm);
        ElMessage({
            message: '修改成功',
            type: 'success',
        })
        await getTopic(kcId.value);
        editTopicPopUps.value = false;
    } catch (e) {
        console.log(e);
        ElMessage({
            message: '修改失败',
            type: 'error',
        })
    }
}

watch(() => props.courseTaskDetailsId, (newVal, oldVal) => {
    kcId.value = newVal;
});

watch(aiPopUps, (newVal, oldVal) => {
    if (!newVal) {
        chatMessages.value = [
            { type: 'assistant', text: '我是一个出题助手，请输入你想生成题目的需求，例如题目类型和数量。' }
        ];
    }
});

defineExpose({
    resetData,
    getTopic
})
</script>

<template>
    <div class="topic overflow-scroll max-h-600px">
        <!-- 操作 -->
        <el-row class="m-b-10px">
            <!-- 搜索 -->
            <el-col :span="14" class="flex">
                <el-input v-model="search" placeholder="请输入课程名称" />
                <el-button type="primary" class="m-l-10px" @click="searchCourse">
                    <el-icon class="m-r-5px" size="16">
                        <Search />
                    </el-icon>
                    查询题目
                </el-button>
            </el-col>
            <!-- 重置 -->
            <el-col :span="1.5" class="flex">
                <el-button type="primary" class="m-l-10px" @click="getTopic(kcId)">
                    <el-icon class="m-r-5px" size="16">
                        <Refresh />
                    </el-icon>
                    重置
                </el-button>
            </el-col>
            <!-- 创建题目 -->
            <el-col :span="1.5">
                <el-button type="primary" class="m-l-10px" @click="addTopicPopUps = true">
                    <el-icon class="m-r-5px" size="16">
                        <Plus />
                    </el-icon>
                    创建题目
                </el-button>
            </el-col>
            <!-- AI 生成题目 -->
            <el-col :span="1.5">
                <el-button type="primary" class="m-l-10px" @click="aiPopUps = true">
                    <el-icon class="m-r-5px" size="16">
                        <Cpu />
                    </el-icon>
                    AI出题
                </el-button>
            </el-col>
        </el-row>
        <!-- 表单 -->
        <el-table :data="topicList" border style="width: 100%;">
            <!-- 创建时间 -->
            <el-table-column prop="createtime" label="创建时间" max-width="180">
                <template #default="scope">
                    {{ useTime(scope.row.createtime) }}
                </template>
            </el-table-column>
            <!-- 题干 -->
            <el-table-column prop="tigan" label="题干" width="120">
                <template #default="scope">
                    <el-popover placement="top-start" title="题干详情" :width="200" trigger="hover"
                        :content="scope.row.tigan">
                        <template #reference>
                            <div class="w-90% overflow-hidden text-ellipsis whitespace-nowrap">{{ scope.row.tigan }}
                            </div>
                        </template>
                    </el-popover>
                </template>
            </el-table-column>
            <!-- 题目类型 -->
            <el-table-column prop="type" label="题目类型" width="100">
                <template #default="scope">
                    <template v-if="scope.row.type === 1"><el-tag type="primary">填空</el-tag></template>
                    <template v-if="scope.row.type === 2"> <el-tag type="success">单选</el-tag></template>
                    <template v-if="scope.row.type === 3"> <el-tag type="info">判断</el-tag></template>
                </template>
            </el-table-column>
            <!-- 答案 -->
            <el-table-column prop="daan" label="答案" width="80">
                <template #default="scope">
                    <!-- 填空题 -->
                    <template v-if="scope.row.type === 1">
                        <el-popover placement="top-start" title="答案详情" :width="200" trigger="hover"
                            :content="scope.row.daan">
                            <template #reference>
                                <div class="w-90% overflow-hidden text-ellipsis whitespace-nowrap">{{ scope.row.daan }}
                                </div>
                            </template>
                        </el-popover>
                    </template>
                    <!-- 单选题 -->
                    <template v-if="scope.row.type === 2">
                        <el-tag size="large">{{ convertNumberToLetter(Number(scope.row.daan)) }}</el-tag>
                    </template>
                    <!-- 判断题 -->
                    <template v-if="scope.row.type === 3">
                        <template v-if="scope.row.daan === '1'"><el-tag type="success">正确</el-tag></template>
                        <template v-if="scope.row.daan === '2'"><el-tag type="danger">错误</el-tag></template>
                    </template>

                </template>
            </el-table-column>
            <!-- 操作 -->
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="editTopic(scope.row)">编辑</el-button>
                    <el-button link type="primary" size="small" @click="deleteTopic(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>

    <!-- 添加课程弹窗 -->
    <el-dialog v-model="addTopicPopUps" title="添加题目" width="500">
        <el-form :model="topicForm">
            <el-form-item label="请输入题目名称" label-width="140px">
                <el-input v-model="topicForm.tigan" autocomplete="off" />
            </el-form-item>
            <el-form-item label="题目类型" label-width="140px">
                <el-radio-group v-model="topicForm.type">
                    <el-radio :value="1">填空</el-radio>
                    <el-radio :value="2">单选</el-radio>
                    <el-radio :value="3">判断</el-radio>
                </el-radio-group>
            </el-form-item>
            <!-- 填空 -->
            <template v-if="topicForm.type === 1">
                <el-form-item label="请输入答案" label-width="140px">
                    <el-input v-model="topicForm.daan" autocomplete="off" />
                </el-form-item>
            </template>
            <!-- 单选 -->
            <template v-else-if="topicForm.type === 2">
                <el-form-item label="请输入选项" label-width="140px">
                    <el-input v-model="topicForm.xuanxiang" autocomplete="off"
                        placeholder="按顺序用|隔开，例如1986|1988,代表二个选项" />
                </el-form-item>
                <el-form-item label="请选择答案" label-width="140px">
                    <el-radio-group v-model="topicForm.daan">
                        <el-radio v-for="(item, index) in topicForm.xuanxiang.split('|')" :key="index"
                            :label="1 + index">{{ item }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </template>
            <!-- 判断 -->
            <template v-else-if="topicForm.type === 3">
                <el-form-item label="请输入答案" label-width="140px">
                    <el-radio-group v-model="topicForm.daan">
                        <el-radio :label="1">正确</el-radio>
                        <el-radio :label="2">错误</el-radio>
                    </el-radio-group>
                </el-form-item>
            </template>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addTopicPopUps = false">取消</el-button>
                <el-button type="primary" @click="addTopic">添加</el-button>
            </div>
        </template>
    </el-dialog>

    <!-- AI 需求弹窗 -->
    <el-dialog v-model="aiPopUps" title="AI 出题小助手" width="600" :style="{top: '10vh'}">
        <el-form>
            <div class="chat-container" style="max-height: 400px; overflow-y: auto; border: 1px solid #ccc; padding: 10px;">
                <div v-for="message in chatMessages" :key="message.text" class="chat-message">
                    <div :class="{'user-message': message.type === 'user', 'assistant-message': message.type === 'assistant'}">
                        {{ message.text }}
                    </div>
                </div>
            </div>
        </el-form>
        <div class="input-container" style="display: flex; margin-top: 10px;">
            <el-input v-model="aiRequirement" placeholder="请输入生成题目的需求，例如题目类型和数量" @keyup.enter="sendMessage" style="flex: 1; margin-right: 10px;" />
            <el-button type="primary" @click="sendMessage">发送</el-button>
        </div>
    </el-dialog>

    <!-- 编辑课程弹窗 edit -->
    <el-dialog v-model="editTopicPopUps" title="编辑题目" width="500">
        <el-form :model="editTopicForm">
            <el-form-item label="请输入题目名称" label-width="140px">
                <el-input v-model="editTopicForm.tigan" autocomplete="off" />
            </el-form-item>
            <el-form-item label="题目类型" label-width="140px">
                <el-radio-group v-model="editTopicForm.type">
                    <el-radio :value="1">填空</el-radio>
                    <el-radio :value="2">单选</el-radio>
                    <el-radio :value="3">判断</el-radio>
                </el-radio-group>
            </el-form-item>
            <!-- 填空 -->
            <template v-if="editTopicForm.type === 1">
                <el-form-item label="请输入答案" label-width="140px">
                    <el-input v-model="editTopicForm.daan" autocomplete="off" />
                </el-form-item>
            </template>
            <!-- 单选 -->
            <template v-else-if="editTopicForm.type === 2">
                <el-form-item label="请输入选项" label-width="140px">
                    <el-input v-model="editTopicForm.xuanxiang" autocomplete="off"
                        placeholder="按顺序用|隔开，例如1986|1988,代表二个选项" />
                </el-form-item>
                <el-form-item label="请选择答案" label-width="140px">
                    <el-radio-group v-model="editTopicForm.daan">
                        <el-radio v-for="(item, index) in editTopicForm.xuanxiang.split('|')" :key="index"
                            :label="1 + index">{{ item }}</el-radio>
                    </el-radio-group>
                </el-form-item>
            </template>
            <!-- 判断 -->
            <template v-else-if="editTopicForm.type === 3">
                <el-form-item label="请输入答案" label-width="140px">
                    <el-radio-group v-model="editTopicForm.daan">
                        <el-radio :label="1">正确</el-radio>
                        <el-radio :label="2">错误</el-radio>
                    </el-radio-group>
                </el-form-item>
            </template>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="editTopicPopUps = false">取消</el-button>
                <el-button type="primary" @click="preservation">保存</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>
.topic::-webkit-scrollbar {
    display: none;
}

.user-message {
    text-align: right;
    background-color: #f0f0f0;
    padding: 5px 10px;
    border-radius: 10px;
    margin: 5px 0;
}

.assistant-message {
    text-align: left;
    background-color: #e0e0e0;
    padding: 5px 10px;
    border-radius: 10px;
    margin: 5px 0;
}
</style>
