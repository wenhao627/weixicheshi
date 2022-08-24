package com.github.binarywang.demo.wx.mp.handler;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.demo.wx.mp.builder.TemplateMsgEntity;
import com.github.binarywang.demo.wx.mp.config.WechatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigurationService {
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";

    /**
     * 读取配置文件中自定义配置的微信接口信息
     */
    @Autowired
    private WechatConfig wechatConfig;
    @Value("${wechat.messageId}")
    private String templateId;
    @Value("${wechat.messageId2}")
    private String templateId2;
    @Value("${wechat.messageId3}")
    private String templateId3;
    public JSONObject getAccessToken() {
        String requestUrl = accessTokenUrl + "appid=" + wechatConfig.getAppid() + "&secret=" + wechatConfig.getAppkey();//读取微信接口的id和密码
        String resp = HttpUtil.get(requestUrl);
        JSONObject result = JSONUtil.parseObj(resp);
        System.out.println("获取access_token:" + resp);

        return result;
    }
    /**
     * 获取用户列表
     * @param accessToken
     * @return
     */

    public JSONObject getUserList(String accessToken) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=";
        String resp = HttpUtil.get(requestUrl);
        JSONObject result = JSONUtil.parseObj(resp);
        System.out.println("用户列表:" + resp);
        return result;
    }
    public JSONObject sendMsg(TemplateMsgEntity messageVo, String token, String openId,boolean flag) throws ParseException {
        String requestUrl = " https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="  + token;
        Map<String,Object> content=new HashMap<>();
        JSONObject data = JSONUtil.createObj();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTTitle());
        jsonObject.put("color","#173177");
        data.put("first",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword1());
        jsonObject.put("color","#FFA500");
        data.put("keyword1",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword2());
        jsonObject.put("color","#87CEFA");
        data.put("keyword2",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword3());
        jsonObject.put("color","#00FF7F");
        data.put("keyword3",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword4());
        jsonObject.put("color","#D3D3D3");
        data.put("keyword4",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword5());
        jsonObject.put("color","#FFA500");
        data.put("keyword5",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword6());
        jsonObject.put("color","#87CEFA");
        data.put("keyword6",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword7());
        jsonObject.put("color","#00FF7F");
        data.put("keyword7",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword8());
        jsonObject.put("color","#D3D3D3");
        data.put("keyword8",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTRemark());
        jsonObject.put("color","#FF6347");
        data.put("remark",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTInfo());
        jsonObject.put("color","#7CFC00");
        data.put("info",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTFlag());
        jsonObject.put("color","#FF69B4");
        data.put("flag",jsonObject);
        content.put("touser",openId);
        content.put("url",messageVo.getTUrl());
        if (flag){
            content.put("template_id",templateId);
        }
        else {
            content.put("template_id",templateId3);
        }
        content.put("data",data);
        String resp = HttpUtil.post(requestUrl,JSONUtil.parseFromMap(content).toString());
        JSONObject result = JSONUtil.parseObj(resp);
        System.out.println("发送消息:" + resp);
        return result;
    }
    public JSONObject sendMsg2(TemplateMsgEntity messageVo, String token, String openId,int flag) throws ParseException {
        String requestUrl = " https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="  + token;
        Map<String,Object> content=new HashMap<>();
        JSONObject data = JSONUtil.createObj();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTTitle());
        jsonObject.put("color","#173177");
        data.put("first",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword1());
        jsonObject.put("color","#0006B5");
        data.put("keyword1",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword2());
        jsonObject.put("color","#87CEFA");
        data.put("keyword2",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword3());
        jsonObject.put("color","#FF4633");
        data.put("keyword3",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTKeyword4());
        jsonObject.put("color","#7CFB8D");
        data.put("keyword4",jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTRemark());
        jsonObject.put("color","#FF6A5B");
        data.put("remark",jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTBirth());
        jsonObject.put("color","#FF6A5B");
        data.put("birth",jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTInfo());
        jsonObject.put("color","#FFD33E");
        data.put("info",jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("value",messageVo.getTFlag());
        jsonObject.put("color","#FF0B7C");
        data.put("flag",jsonObject);
        content.put("touser",openId);
        content.put("url",messageVo.getTUrl());
        if (flag == 1){
            content.put("template_id",templateId);
        }
        else if(flag == 2) {
            content.put("template_id",templateId2);
        }
        else {
            content.put("template_id",templateId3);
        }
        content.put("data",data);
        String resp = HttpUtil.post(requestUrl,JSONUtil.parseFromMap(content).toString());
        JSONObject result = JSONUtil.parseObj(resp);
        System.out.println("发送消息:" + resp);
        return result;
    }

}

