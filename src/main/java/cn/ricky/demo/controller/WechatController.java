package cn.ricky.demo.controller;

import cn.ricky.demo.dto.InMsgEntity;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/wechat")
public class WechatController {
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
  public OutMsgEntity handleMessage(@RequestBody InMsgEntity msg) {
    return wechatService.processRequest(msg);
  }

  @GetMapping(value = "/callback")
  @ResponseBody
  public String checkSignature(@RequestBody SignatureEntity signatureEntity) {
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
