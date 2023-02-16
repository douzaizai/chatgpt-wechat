package cn.ricky.demo.service;

import cn.ricky.demo.dto.InMsgEntity;
import cn.ricky.demo.dto.OutMsgEntity;
import cn.ricky.demo.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class WechatService {

  @Autowired
  private ChatGPTService chatGPTService;

  public OutMsgEntity processRequest(InMsgEntity msg) {
    log.info("inMsg:{}", msg);
    //创建消息响应对象
    OutMsgEntity out = new OutMsgEntity();
    //把原来的发送方设置为接收方
    out.setToUserName(msg.getFromUserName());
    //把原来的接收方设置为发送方
    out.setFromUserName(msg.getToUserName());
    //获取接收的消息类型
    String msgType = msg.getMsgType();
    //设置消息的响应类型
    out.setMsgType(msgType);
    //设置消息创建时间
    out.setCreateTime(new Date().getTime());
    String respContent = "";
    //根据类型设置不同的消息数据
    // 文本消息
    if (msgType.equals(MessageUtil.MESSAGE_TEXT)) {
      //消息内容
      respContent = chatGPTService.chat(msg.getContent(), msg.getFromUserName());
    }
    // 图片消息
    else if (msgType.equals(MessageUtil.MESSAGE_IMAGE)) {
      respContent = "您发送的图片我没见过！";
    }
    // 语音消息
    else if (msgType.equals(MessageUtil.MESSAGE_VOICE)) {
      respContent = "我听不见您说的什么，还是打字吧！";
    }
    // 视频消息
    else if (msgType.equals(MessageUtil.MESSAGE_VIDEO)) {
      respContent = "您发送视频我没看过！";
    }
    // 视频消息
    else if (msgType.equals(MessageUtil.MESSAGE_SHORT_VIDEO)) {
      respContent = "您发送的是小视频消息！";
    }
    // 地理位置消息
    else if (msgType.equals(MessageUtil.MESSAGE_LOCATION)) {
      respContent = "这个地方我没去过！";
    }
    // 链接消息
    else if (msgType.equals(MessageUtil.MESSAGE_LINK)) {
      respContent = "您发送的是链接消息！但是我不认识！";
    }
    // 事件推送
    else if (msgType.equals(MessageUtil.MESSAGE_EVENT)) {
      // 事件类型
      String eventType = msg.getEvent();
      // 关注
      if (eventType.equals(MessageUtil.EVENT_SUB)) {
        respContent = "欢迎关注Ricky的chatGPT公众号，个人微信18516698996";
      }
      // 取消关注
      else if (eventType.equals(MessageUtil.EVENT_UNSUB)) {
        //取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
      }
      // 扫描带参数二维码
      else if (eventType.equals(MessageUtil.EVENT_SCAN)) {
        // 处理扫描带参数二维码事件
      }
      // 上报地理位置
      else if (eventType.equals(MessageUtil.EVENT_LOCATION)) {
        //处理上报地理位置事件
      }
      // 自定义菜单
      else if (eventType.equals(MessageUtil.EVENT_CLICK)) {
        //处理菜单点击事件
      }
    }
    out.setContent(respContent);
    log.info("outMsg:{}", out);
    return out;
  }

}
