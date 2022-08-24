package com.github.binarywang.demo.wx.mp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class WechatConfig {
    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.appkey}")
    private String appkey;
}
