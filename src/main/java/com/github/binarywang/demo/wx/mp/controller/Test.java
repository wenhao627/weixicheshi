package com.github.binarywang.demo.wx.mp.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.binarywang.demo.wx.mp.builder.TemplateMsgEntity;
import com.github.binarywang.demo.wx.mp.handler.ConfigurationService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@RestController
public class Test {
    //天气查询接口 https://user.ip138.com/login
    private final String WEATHERURL1 = "https://api.ip138.com/weather/?code=510184&type=1&token=";
    private final String WEATHERURL2 = "https://api.ip138.com/weather/?code=330303&type=1&token=";
    @Resource
    ConfigurationService configurationService;
    /**
     * 发送模板消息 510184  崇州  330303 温州
     * @return
     */
    @GetMapping( "/sedMsg")
    public JSONObject sedMsg(String info) throws ParseException {
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
        String day = "2022-8-22";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date targerDay = sdf.parse(day);
        long targetTime = targerDay.getTime();
        long todaytime = System.currentTimeMillis();
        long time =Math.abs(targetTime-todaytime);
        //93是到今天为止，恋爱多少天
        System.out.println(time/1000/60/60/24 + 93);
        messageVo.setTRemark(time/1000/60/60/24 + 93 + "");
        String url = HttpUtil.get(WEATHERURL1);
        com.alibaba.fastjson.JSONObject jsonObject;
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(url);
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"));
        String dayWeather = jsonObject.getString("dayWeather");
        String nightTemp = jsonObject.getString("nightTemp");
        String dayTemp = jsonObject.getString("dayTemp");
        messageVo.setTKeyword1("四川省达州达川区");
        messageVo.setTKeyword2(dayWeather);
        messageVo.setTKeyword3(dayTemp + "℃");
        messageVo.setTKeyword4(nightTemp + "℃");

        url = HttpUtil.get(WEATHERURL2);
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(url);
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"));
        dayWeather = jsonObject.getString("dayWeather");
        nightTemp = jsonObject.getString("nightTemp");
        dayTemp = jsonObject.getString("dayTemp");
        messageVo.setTKeyword5("四川省成都市成华区");
        messageVo.setTKeyword6(dayWeather);
        messageVo.setTKeyword7(dayTemp + "℃");
        messageVo.setTKeyword8(nightTemp + "℃");
        String flag = "";
        if (dayWeather.contains("雨")){
            flag = ("记得今日有雨哦，记得带伞呀!");
        }
        if (Integer.parseInt(dayTemp) > 30){
            flag = flag + "今日温度较高，记得防晒哟。";
        }
        if (Integer.parseInt(nightTemp) < 10){
            flag = flag + "夜间气温较低，别感冒了哟。";
        }
        if ("".equals(info) || info == null){
            messageVo.setTInfo("记得吃早饭呐  新的一天也要开心呐❤");
        }
        else {
            messageVo.setTInfo(info);
        }
        if ("".equals(flag)){
            messageVo.setTFlag("今日天气很好哦，没什么特别提醒的，就是提醒你想我哟。");
        }
        else {
            messageVo.setTFlag(flag);
        }
        for (Object openid:openids) {
            JSONObject resp = configurationService.sendMsg(messageVo,token,openid.toString(),false);
        }
        return null;
    }

    @GetMapping( "/send2")
    @Retryable(value= {Exception.class})
    public JSONObject send(String info) throws ParseException {
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
        System.out.println(getBirthDay(birthdayDateString));

        messageVo.setTRemark(caculate() + "");
        messageVo.setTBirth(getBirthDay(birthdayDateString) + "");

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
        if ("".equals(info) || info == null){
            messageVo.setTInfo("万事要全力以赴、包括开心哦❤❤");
        }
        else {
            messageVo.setTInfo(info);
        }
        if ("".equals(flag)){
            messageVo.setTFlag("今日天气很好哦，没什么特别提醒的，今天的你离富婆又进一步了。");
        }
        else {
            messageVo.setTFlag(flag);
        }
        if (caculate() % 100 == 0 || caculate() % 50 == 0){
            messageVo.setTInfo(messageVo.getTInfo() + "。今天也是特别的日子呢，你已经来到这个世界" + caculate() + "天了哟。");
        }
        if (getBirthDay("2000-4-29") < 30){
            messageVo.setTInfo(messageVo.getTInfo() + "。谢yuyu小可爱你的生日只有不到" + getBirthDay("2000-4-29") + "天了哦");
        }
        for (Object openid:openids) {
            JSONObject resp = configurationService.sendMsg2(messageVo,token,openid.toString(),test());
        }
        return null;
    }
    public int getBirthDay(String addtime) {
        int days = 0;
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String clidate = addtime;
            Calendar cToday = Calendar.getInstance(); // 存今天
            Calendar cBirth = Calendar.getInstance(); // 存生日
            cBirth.setTime(myFormatter.parse(clidate)); // 设置生日
            cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
            if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
                // 生日已经过了，要算明年的了
                days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
                days += cBirth.get(Calendar.DAY_OF_YEAR);
            } else {
                // 生日还没过
                days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public int caculate() throws ParseException {
        Date now = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date befor = dateFormat1.parse("2000-4-29");
        long day1 = System.currentTimeMillis()/1000/60/60/24;
        long day2 = befor.getTime()/1000/60/60/24;
        return (int) (day1 -day2);
    }

    public int test(){
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 6) {
            return 3;
        }
        if (a > 6 && a <= 12) {
            return 1;
        }
        if (a == 13) {
            return 2;
        }
        if (a > 13 && a <= 18) {
            return 2;
        }
        if (a > 18 && a <= 24) {
            return 3;
        }
        return 0;
    }
}
