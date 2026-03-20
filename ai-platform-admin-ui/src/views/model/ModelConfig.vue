<template>
  <div class="model-config-container">
    <el-card shadow="never">
      <!-- 搜索查询区 -->
      <div class="search-wrap">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
          <el-form-item label="服务商类型">
            <el-select v-model="queryParams.provider" placeholder="请选择服务商" clearable style="width: 200px">
              <el-option label="OpenAI" value="openai" />
              <el-option label="通义千问 (Qwen)" value="qwen" />
              <el-option label="智谱 (Zhipu)" value="zhipu" />
              <el-option label="深度求索 (DeepSeek)" value="deepseek" />
            </el-select>
          </el-form-item>
          <el-form-item label="模型名称">
            <el-input v-model="queryParams.modelName" placeholder="模糊搜索模型名" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery" :icon="Search">搜索查询</el-button>
            <el-button @click="resetQuery" :icon="Refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作工具栏 -->
      <div class="toolbar">
        <el-button type="success" :icon="Plus" @click="handleAdd">新增模型网关配置</el-button>
      </div>

      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" style="width: 100%" border stripe>
        <el-table-column prop="id" label="主键标识" width="180" align="center" show-overflow-tooltip/>
        <el-table-column prop="modelName" label="模型名称" width="150" align="center" show-overflow-tooltip />
        <el-table-column prop="provider" label="服务渠道" width="120" align="center">
          <template #default="scope">
            <el-tag :type="getProviderTag(scope.row.provider)">
              {{ scope.row.provider.toUpperCase() }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="baseUrl" label="API BaseUrl" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip />
        <el-table-column prop="createTime" label="录入时间" width="170" align="center" />
        
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" link :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-popconfirm title="确定要永久删除该项配置吗？" @confirm="handleDelete(scope.row)">
              <template #reference>
                <el-button size="small" type="danger" link :icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页底栏 -->
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 新增 / 编辑 弹出层 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="例如：平台通用千问主力模型" />
        </el-form-item>
        <el-form-item label="服务商类型" prop="provider">
          <el-select v-model="form.provider" placeholder="选择服务商平台" style="width: 100%">
            <el-option label="OpenAI" value="openai" />
            <el-option label="通义千问 (Qwen)" value="qwen" />
            <el-option label="智谱 (Zhipu)" value="zhipu" />
            <el-option label="深度求索 (DeepSeek)" value="deepseek" />
          </el-select>
        </el-form-item>
        <el-form-item label="API Key 秘钥" prop="apiKey">
          <el-input v-model="form.apiKey" placeholder="输入从服务商申请的 API Key" show-password type="password" />
        </el-form-item>
        <el-form-item label="基础代理网关" prop="baseUrl">
          <el-input v-model="form.baseUrl" placeholder="默认官方域，如国内代理域可填入 (形如 https://api.xxx.com)" />
        </el-form-item>
        <el-form-item label="启用状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">对外开放</el-radio>
            <el-radio :value="0">挂起熔断</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="填写一下这把钥匙的关联配额情况..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确定保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import {
  getModelConfigPage,
  addModelConfig,
  updateModelConfig,
  deleteModelConfig,
  type ModelConfig
} from '../../api/modelConfig'

// -------- 响应式状态 --------
const loading = ref(false)
const tableData = ref<ModelConfig[]>([])
const total = ref(0)
const queryParams = reactive({
  current: 1,
  size: 10,
  provider: '',
  modelName: ''
})

// 表单抽屉状态
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

const initForm = (): ModelConfig => ({
  id: undefined,
  modelName: '',
  provider: '',
  apiKey: '',
  baseUrl: '',
  status: 1,
  remark: ''
})
const form = ref<ModelConfig>(initForm())

const rules: FormRules = {
  modelName: [{ required: true, message: '配置名称不可为空', trigger: 'blur' }],
  provider: [{ required: true, message: '网络服务商不能为空', trigger: 'change' }],
  apiKey: [{ required: true, message: '千万别忘了填核心网关秘钥', trigger: 'blur' }]
}

// -------- 方法逻辑 --------
onMounted(() => {
  getList()
})

const getList = async () => {
  loading.value = true
  try {
    const res: any = await getModelConfigPage(queryParams)
    tableData.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

const resetQuery = () => {
  queryParams.provider = ''
  queryParams.modelName = ''
  handleQuery()
}

const handleAdd = () => {
  form.value = initForm()
  dialogTitle.value = '➕ 登记全新的大语言模型 (LLM)'
  dialogVisible.value = true
}

const handleEdit = (row: ModelConfig) => {
  form.value = { ...row }
  dialogTitle.value = '🪄 修改网关模型属性'
  dialogVisible.value = true
}

// 行内修改状态
const handleStatusChange = async (row: ModelConfig) => {
  const opStr = row.status === 1 ? '启用' : '熔断(禁用)'
  try {
    await updateModelConfig({ id: row.id, status: row.status, provider: row.provider, modelName: row.modelName, apiKey: row.apiKey })
    ElMessage.success(`${opStr} ${row.modelName} 成功！`)
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1 // 还原
  }
}

const handleDelete = async (row: ModelConfig) => {
  if (!row.id) return
  await deleteModelConfig(row.id)
  ElMessage.success('配置清理成功')
  getList()
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.value.id) {
          await updateModelConfig(form.value)
          ElMessage.success('更新成功！大模型马上就会用新的配置上线啦')
        } else {
          await addModelConfig(form.value)
          ElMessage.success('接轨成功！一个新的宇宙模型已经并入了我们的母星')
        }
        dialogVisible.value = false
        getList()
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const getProviderTag = (provider: string) => {
  const map: Record<string, string> = {
    openai: 'success',
    qwen: 'primary',
    zhipu: 'warning',
    deepseek: 'danger'
  }
  return map[provider] || 'info'
}
</script>

<style scoped>
.model-config-container {
  padding: 20px;
}
.search-wrap {
  margin-bottom: 10px;
}
.toolbar {
  margin-bottom: 20px;
}
.pagination-wrap {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
