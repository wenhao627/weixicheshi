package com.github.binarywang.demo.wx.mp.servise;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.binarywang.demo.wx.mp.builder.TemplateMsgEntity;
import com.github.binarywang.demo.wx.mp.controller.Test;
import com.github.binarywang.demo.wx.mp.handler.ConfigurationService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@EnableScheduling
@Service
public class test1 {

    private final String WEATHERURL1 = "https://api.ip138.com/weather/?code=510184&type=1&token=";
    @Resource
    ConfigurationService configurationService;
    @Retryable(value= {Exception.class})
    //(cron = "0 0 7 * * ?")
    public void clear() throws ParseException {
        Test test = new Test();
        JSONObject accessToken = configurationService.getAccessToken();
        String token=accessToken.getStr("access_token");
        //获取用户列表
        JSONObject userList = configurationService.getUserList(token);
        JSONArray openids = userList.getJSONObject("data").getJSONArray("openid");
        System.out.println(Arrays.toString(openids.toArray()));
        TemplateMsgEntity messageVo=new TemplateMsgEntity();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now3 = df.format(System.currentTimeMillis());
        messageVo.setTTitle("现在的时间是：" + now3);
        String birthdayDateString = "2000-4-29";
        System.out.println(test.getBirthDay(birthdayDateString));

        messageVo.setTRemark(test.caculate() + "");
        messageVo.setTBirth(test.getBirthDay(birthdayDateString) + "");

        String url = HttpUtil.get(WEATHERURL1);
        com.alibaba.fastjson.JSONObject jsonObject;
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(url);
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"));
        String dayWeather = jsonObject.getString("dayWeather");
        String nightTemp = jsonObject.getString("nightTemp");
        String dayTemp = jsonObject.getString("dayTemp");
        messageVo.setTKeyword1("四川省成都市崇州区");
        messageVo.setTKeyword2(dayWeather);
        messageVo.setTKeyword3(dayTemp + "℃");
        messageVo.setTKeyword4(nightTemp + "℃");

        String flag = "";
        if (dayWeather.contains("雨")){
            flag = ("记得今日有雨哦，记得带伞呀!");
        }
        if (Integer.parseInt(dayTemp) > 30){
            flag = flag + "今日温度较高，记得防晒，多喝点水哟。";
        }
        if (Integer.parseInt(nightTemp) < 10){
            flag = flag + "夜间气温较低，别感冒了哟。";
        }
        messageVo.setTInfo("万事要全力以赴、包括开心哦❤❤");
        if ("".equals(flag)){
            messageVo.setTFlag("今日天气很好哦，没什么特别提醒的，今天的你离富婆又进一步了。");
        }
        else {
            messageVo.setTFlag(flag);
        }
        if (test.caculate() % 100 == 0 || test.caculate() % 50 == 0){
            messageVo.setTInfo(messageVo.getTInfo() + "。今天也是特别的日子呢，你已经来到这个世界" + test.caculate() + "天了哟。");
        }
        if (test.getBirthDay("2000-4-29") < 30){
            messageVo.setTInfo(messageVo.getTInfo() + "。谢yuyu小可爱你的生日只有不到" + test.getBirthDay("2000-4-29") + "天了哦");
        }
        for (Object openid:openids) {
            JSONObject resp = configurationService.sendMsg2(messageVo,token,openid.toString(),test.test());
        }
    }

}
