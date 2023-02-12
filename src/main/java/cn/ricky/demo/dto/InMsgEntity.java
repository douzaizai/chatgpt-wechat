package cn.ricky.demo.dto;

import lombok.Data;


@Data
public class InMsgEntity {
    // 开发者微信号
    private String FromUserName;
    // 发送方帐号（一个OpenID）
    private String ToUserName;
    // 消息创建时间
    private Long CreateTime;
    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     */
    private String MsgType;
    // 消息id
    private Long MsgId;
    // 文本内容
    private String Content;
    // 事件
    private String Event;

}