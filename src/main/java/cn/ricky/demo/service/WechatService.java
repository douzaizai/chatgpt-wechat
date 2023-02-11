package cn.ricky.demo.service;

import cn.ricky.demo.model.TextMessage;
import cn.ricky.demo.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class WechatService {

  @Autowired
  private ChatGPTService chatGPTService;

  /**
   * 处理微信发来的请求
   *
   * @param request
   * @return xml
   */
  public String processRequest(HttpServletRequest request) {
    // xml格式的消息数据
    String respXml = null;
    // 默认返回的文本消息内容
    String respContent = "未知的消息类型！";
    try {
      // 调用parseXml方法解析请求消息
      Map<String, String> requestMap = MessageUtil.xmlToMap(request);
      // 发送方帐号
      String fromUserName = requestMap.get("FromUserName");
      // 开发者微信号
      String toUserName = requestMap.get("ToUserName");
      // 消息类型
      String msgType = requestMap.get("MsgType");
      // 回复文本消息
      TextMessage textMessage = new TextMessage();
      textMessage.setToUserName(fromUserName);
      textMessage.setFromUserName(toUserName);
      textMessage.setCreateTime(new Date().getTime());
      textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
      // 文本消息
      if (msgType.equals(MessageUtil.MESSAGE_TEXT)) {
        //消息内容
        String content = requestMap.get("Content");
        respContent = chatGPTService.chat(content, fromUserName);
        respContent = "test";
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
        String eventType = requestMap.get("Event");
        // 关注
        if (eventType.equals(MessageUtil.EVENT_SUB)) {
          respContent = "欢迎关注Ricky的chatGPT公众号";
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

      // 设置文本消息的内容
      textMessage.setContent(respContent);
      // 将文本消息对象转换成xml
      respXml = MessageUtil.textMessageToXml(textMessage);
      log.info("respXml:{}", respXml);
    } catch (Exception e) {
      log.error("processRequest failed", e);
    }
    return respXml;
  }

}
