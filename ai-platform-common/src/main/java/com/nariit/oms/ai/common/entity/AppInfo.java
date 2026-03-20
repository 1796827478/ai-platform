package com.nariit.oms.ai.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 外部接入业务系统信息表
 * </p>
 *
 * @author AI-Copilot
 * @since 2026-03-20
 */
@Getter
@Setter
@TableName("ai_app_info")
public class AppInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID(雪花算法)
     */
    private Long id;

    /**
     * 业务系统唯一编码(如: dccs-oms, erp)
     */
    private String appCode;

    /**
     * 业务系统名称
     */
    private String appName;

    /**
     * 调用密钥(建议Bcrypt加密或简单的UUID Token)
     */
    private String appSecret;

    /**
     * 状态: 1-正常 0-停用
     */
    private Byte status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
