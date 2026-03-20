package com.nariit.oms.ai.common.service.impl;

import com.nariit.oms.ai.common.entity.ChatSession;
import com.nariit.oms.ai.common.mapper.ChatSessionMapper;
import com.nariit.oms.ai.common.service.IChatSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * AI 问答统一会话上下文记录表 服务实现类
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements IChatSessionService {

}
