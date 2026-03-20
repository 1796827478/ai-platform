package com.nariit.oms.ai.common.service.impl;

import com.nariit.oms.ai.common.entity.ChatMessage;
import com.nariit.oms.ai.common.mapper.ChatMessageMapper;
import com.nariit.oms.ai.common.service.IChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * AI 问答统一对话流水记录表 服务实现类
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageService {

}
