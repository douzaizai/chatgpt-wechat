package cn.ricky.demo.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutMsgEntity {
    // 发送方的账号
    private String FromUserName;
    // 接收方的账号(OpenID)
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
     * news 图文消息
     */
    private String MsgType;
    // 文本内容
    private String Content;
}