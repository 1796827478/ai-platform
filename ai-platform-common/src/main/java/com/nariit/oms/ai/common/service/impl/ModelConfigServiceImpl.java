package com.nariit.oms.ai.common.service.impl;

import com.nariit.oms.ai.common.entity.ModelConfig;
import com.nariit.oms.ai.common.mapper.ModelConfigMapper;
import com.nariit.oms.ai.common.service.IModelConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 大模型底层通道配置表 服务实现类
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Service
public class ModelConfigServiceImpl extends ServiceImpl<ModelConfigMapper, ModelConfig> implements IModelConfigService {

}
