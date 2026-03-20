package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * AI 问答统一会话上下文记录表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_chat_session")
public class ChatSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键也是真实的 Session ID
     */
    private Long id;

    /**
     * 这是哪个业务系统建立的会话
     */
    private Long appId;

    /**
     * 业务系统传过来的原始用户工号/ID
     */
    private String userId;

    /**
     * 本次会话指定绑定的大模型资源ID
     */
    private Long modelConfigId;

    /**
     * 由大模型自动抽取的聊天下拉框标题
     */
    private String sessionTitle;

    /**
     * 逻辑删除
     */
    private Byte isDeleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
