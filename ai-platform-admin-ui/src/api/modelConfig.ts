import request from '../utils/request';

// 定义类型
export interface ModelConfig {
  id?: string;
  provider: string;
  modelName: string;
  apiKey: string;
  baseUrl?: string;
  status?: number;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface PageParams {
  current: number;
  size: number;
  provider?: string;
  modelName?: string;
}

// 分页查询
export const getModelConfigPage = (params: PageParams) => {
  return request({
    url: '/api/model-config/page',
    method: 'get',
    params
  });
};

// 获取所有可用字典
export const getAllModelConfigList = () => {
  return request({
    url: '/api/model-config/list-all',
    method: 'get'
  });
};

// 新增
export const addModelConfig = (data: ModelConfig) => {
  return request({
    url: '/api/model-config',
    method: 'post',
    data
  });
};

// 修改
export const updateModelConfig = (data: ModelConfig) => {
  return request({
    url: '/api/model-config',
    method: 'put',
    data
  });
};

// 删除
export const deleteModelConfig = (id: string) => {
  return request({
    url: `/api/model-config/${id}`,
    method: 'delete'
  });
};
