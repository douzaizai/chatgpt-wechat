package cn.ricky.demo.controller;

import cn.ricky.demo.service.ChatGPTService;
import cn.ricky.demo.service.CheckSignatureService;
import cn.ricky.demo.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


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

  @PostMapping(value = "/callback")
  public void callback(HttpServletRequest request, HttpServletResponse response) {
    try {
      String result = wechatService.processRequest(request);
      response.setContentType("text/xml");
      PrintWriter out = response.getWriter();
      out.write(result);
      out.flush();
    } catch (Exception e) {
      log.error("callback error", e);
    }
  }


  /**
   * 验证微信回调请求
   *
   * @param req
   * @param resp
   */
  @GetMapping(value = "/callback")
  public void callbackCheck(HttpServletRequest req, HttpServletResponse resp) {
    try {
      String signature = req.getParameter("signature");
      String timestamp = req.getParameter("timestamp");
      String nonce = req.getParameter("nonce");
      String echoStr = req.getParameter("echostr");
      log.info("signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echoStr);
      PrintWriter out = resp.getWriter();
      if (checkSignatureService.checkSignature(signature, timestamp, nonce)) {
        log.info("callback check passed");
        out.print(echoStr);
        out.flush();
      }
    } catch (Exception e) {
      log.error("callbackCheck failed", e);
    }
  }

  /**
   * 测试chatGPT接口
   *
   * @param request
   * @param response
   */
  @GetMapping(value = "/chatGPTTest")
  public void chatGPTTest(HttpServletRequest request, HttpServletResponse response) {
    try {
      response.setContentType("text/html");
      // 接收查询参数
      String model = request.getParameter("model");
      String prompt = request.getParameter("prompt");
      String user = request.getParameter("user");
      // 返回前端值
      String result = chatGPTService.chat(model, prompt, user);
      PrintWriter out = response.getWriter();
      out.print(result);
      out.flush();
    } catch (Exception e) {
      log.error("chatGPTTest failed", e);
      try {
        PrintWriter out = response.getWriter();
        out.print(e);
        out.flush();
      } catch (Exception e2) {
      }
    }
  }


}
