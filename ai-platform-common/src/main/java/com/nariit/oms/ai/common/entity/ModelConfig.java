package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 大模型底层通道配置表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_model_config")
public class ModelConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模型服务商(如: Qwen, OpenAI, Zhipu)
     */
    private String provider;

    /**
     * 具体模型版本(如: qwen-max, gpt-4o)
     */
    private String modelName;

    /**
     * 代理地址/内网部署地址
     */
    private String baseUrl;

    /**
     * 调用秘钥(建议脱敏)
     */
    private String apiKey;

    /**
     * 状态: 1-启用 0-停用
     */
    private Byte status;

    /**
     * 负载优先级(数字越大越优先)
     */
    private Integer priority;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
