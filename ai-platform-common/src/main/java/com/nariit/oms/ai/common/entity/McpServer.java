package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * MCP 工具提供方注册汇总表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_mcp_server")
public class McpServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 服务唯一标识(如: weather_mcp, db_mcp)
     */
    private String serverCode;

    /**
     * 服务人类可读名称(如: 天气工具服务)
     */
    private String serverName;

    /**
     * 传输协议(SSE 或 STDIO)
     */
    private String transportType;

    /**
     * SSE端点连接地址
     */
    private String serverUrl;

    /**
     * 心跳检查地址(可选)
     */
    private String healthCheckUrl;

    /**
     * 挂载状态: 1-上线 0-下线
     */
    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
