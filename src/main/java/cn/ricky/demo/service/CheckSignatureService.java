package cn.ricky.demo.service;

import cn.ricky.demo.configuration.MyConfig;
import cn.ricky.demo.dto.SignatureEntity;
import cn.ricky.demo.util.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@Service
public class CheckSignatureService {

  @Autowired
  private MyConfig myConfig;

  /**
   * 实现对回传参数的hash，然后同回传参数signature比对
   *
   * @param signatureEntity
   * @return
   */
  public boolean checkSignature(SignatureEntity signatureEntity) {
    return this.checkSignature(signatureEntity.getSignature(), signatureEntity.getTimestamp(), signatureEntity.getNonce());
  }

  /**
   * 实现对回传参数的hash，然后同回传参数signature比对
   *
   * @param signature
   * @param timestamp
   * @param nonce
   * @return
   */
  public boolean checkSignature(String signature, String timestamp, String nonce) {
    ArrayList<String> list = new ArrayList<String>();
    //定义微信接口配置的token，同微信账号页面中配置的token值保持一致
    list.add(myConfig.getWechatToken());
    list.add(timestamp);
    list.add(nonce);
    //对参数进行升序排列
    Collections.sort(list);
    StringBuilder content = new StringBuilder();
    for (String str : list) {
      content.append(str);
    }
    //调用hash算法，对相关参数hash
    return signature.equals(HashUtil.hash(content.toString(), "SHA1"));
  }
}