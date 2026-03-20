package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * AI 问答统一对话流水记录表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_chat_message")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属会话ID
     */
    private Long sessionId;

    /**
     * 角色(user/assistant/tool)
     */
    private String role;

    /**
     * 完整聊天流水内容(对于大模型回答可能极长)
     */
    private String content;

    /**
     * 调用的MCP工具名及入参记录(JSON)
     */
    private String toolCalls;

    /**
     * 发送时间
     */
    private LocalDateTime createTime;
}
