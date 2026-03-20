package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 外部应用系统关联模型与MCP工具授权表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_app_resource_rel")
public class AppResourceRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务系统ID (关联 ai_app_info)
     */
    private Long appId;

    /**
     * 资源类型(MODEL 或 MCP_SERVER)
     */
    private String resourceType;

    /**
     * 关联的 ai_model_config.id 或 ai_mcp_server.id
     */
    private Long resourceId;

    private LocalDateTime createTime;
}
