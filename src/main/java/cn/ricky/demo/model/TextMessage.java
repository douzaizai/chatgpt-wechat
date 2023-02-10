package cn.ricky.demo.model;

import lombok.Data;
@Data
public class TextMessage extends BaseMessage{
    /**
     * 消息类型
     * @ApiModelProperty(value = "消息类型", hidden = true)
     */
    private String MsgType = "text";

    /**
     * 消息内容
     */
    private String Content;
}

