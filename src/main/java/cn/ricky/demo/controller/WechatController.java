package cn.ricky.demo.controller;

import cn.ricky.demo.dto.InMsgEntity;
import cn.ricky.demo.dto.InMsgEntity4Xml;
import cn.ricky.demo.dto.OutMsgEntity;
import cn.ricky.demo.dto.SignatureEntity;
import cn.ricky.demo.service.ChatGPTService;
import cn.ricky.demo.service.CheckSignatureService;
import cn.ricky.demo.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {
  private static final Set<String> REQUEST_SET = Collections.synchronizedSet(new HashSet<>());

  @Autowired
  private WechatService wechatService;

  @Autowired
  private ChatGPTService chatGPTService;

  @Autowired
  private CheckSignatureService checkSignatureService;

  @PostMapping(value = "/callback",
          produces = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE},
          consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE}
  )
  @ResponseBody
  public OutMsgEntity handleMessage(@RequestBody InMsgEntity4Xml msg) {
    String key = new StringBuilder().append(msg.getFromUserName()).append(msg.getToUserName()).append(msg.getMsgType()).append(msg.getContent()).toString();
    if (REQUEST_SET.contains(key)) {
      String content = "别着急，我正在思考如何回答你，20s后再问我相同的问题吧";
      OutMsgEntity outMsg = new OutMsgEntity(msg.getToUserName(), msg.getFromUserName(), msg.getMsgType(), content);
      return outMsg;
    }

    try {
      REQUEST_SET.add(key);
      OutMsgEntity outMsgEntity = wechatService.processRequest(msg);
      return outMsgEntity;
    } catch (Throwable e) {
      throw e;
    } finally {
      REQUEST_SET.remove(key);
    }
  }

  @PostMapping(value = "/callback4Json")
  @ResponseBody
  public OutMsgEntity handleMessage4Json(@RequestBody InMsgEntity msg) {
    return wechatService.processRequest(msg);
  }

  @GetMapping(value = "/callback")
  @ResponseBody
  public String checkSignature(SignatureEntity signatureEntity) {
    if (checkSignatureService.checkSignature(signatureEntity)) {
      return signatureEntity.getEchostr();
    }
    return null;
  }

  @GetMapping(value = "/test")
  @ResponseBody
  public String test(@RequestParam String model, @RequestParam String prompt, @RequestParam String user) {
    return chatGPTService.chat(model, prompt, user);
  }

}
