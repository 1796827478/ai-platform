package com.nariit.oms.ai.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nariit.oms.ai.common.api.Result;
import com.nariit.oms.ai.common.entity.ModelConfig;
import com.nariit.oms.ai.common.service.IModelConfigService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 大模型配置控制器
 */
@RestController
@RequestMapping("/api/model-config")
public class ModelConfigController {

    @Resource
    private IModelConfigService modelConfigService;

    /**
     * 分页查询模型配置列表
     * @param current 当前页
     * @param size 页大小
     * @param provider 模型提供商(如 qwen, openai)
     * @param modelName 配置名称关键词
     */
    @GetMapping("/page")
    public Result<Page<ModelConfig>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String modelName) {
            
        Page<ModelConfig> pageParam = new Page<>(current, size);
        LambdaQueryWrapper<ModelConfig> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(StringUtils.hasText(provider), ModelConfig::getProvider, provider)
               .like(StringUtils.hasText(modelName), ModelConfig::getModelName, modelName)
               .orderByDesc(ModelConfig::getUpdateTime);
               
        Page<ModelConfig> resultPage = modelConfigService.page(pageParam, wrapper);
        return Result.success(resultPage);
    }

    /**
     * 获取所有可用模型（下拉框字典用）
     */
    @GetMapping("/list-all")
    public Result<List<ModelConfig>> listAll() {
        List<ModelConfig> list = modelConfigService.lambdaQuery()
                .eq(ModelConfig::getStatus, 1) //只查启用状态
                .orderByDesc(ModelConfig::getCreateTime)
                .list();
        // 关键秘钥字段可选择性脱敏，内网管理端暂时明文或不查
        return Result.success(list);
    }

    /**
     * 根据主键查询模型详情
     */
    @GetMapping("/{id}")
    public Result<ModelConfig> getById(@PathVariable("id") Long id) {
        return Result.success(modelConfigService.getById(id));
    }

    /**
     * 新增模型配置
     */
    @PostMapping
    public Result<Boolean> save(@RequestBody ModelConfig modelConfig) {
        modelConfig.setCreateTime(LocalDateTime.now());
        modelConfig.setUpdateTime(LocalDateTime.now());
        return Result.success(modelConfigService.save(modelConfig));
    }

    /**
     * 修改模型配置
     */
    @PutMapping
    public Result<Boolean> update(@RequestBody ModelConfig modelConfig) {
        modelConfig.setUpdateTime(LocalDateTime.now());
        return Result.success(modelConfigService.updateById(modelConfig));
    }

    /**
     * 删除模型配置
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable("id") Long id) {
        return Result.success(modelConfigService.removeById(id));
    }
}
