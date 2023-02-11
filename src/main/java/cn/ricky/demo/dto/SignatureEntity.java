package cn.ricky.demo.dto;

import lombok.Data;

@Data
public class SignatureEntity {
  private String signature;
  private String timestamp;
  private String nonce;
  private String echostr;
}
