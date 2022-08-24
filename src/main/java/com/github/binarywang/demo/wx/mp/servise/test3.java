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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@EnableScheduling
@Service
public class test3 {

    private final String WEATHERURL1 = "https://api.ip138.com/weather/?code=330603&type=1&token=7ebec8dc8bc6a0b78d7c304b66df2aaa";
    @Resource
    ConfigurationService configurationService;
    @Retryable(value= {Exception.class})
    @Scheduled(cron = "0 30 8 * * ?")//设置定时执行任务
    public void clear() throws ParseException {
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
        String birthdayDateString = "2023-6-30";
        System.out.println(getBirthDay(birthdayDateString));

        messageVo.setTRemark(caculate() + "");
        messageVo.setTBirth(getBirthDay(birthdayDateString) + "");

        String url = HttpUtil.get(WEATHERURL1);
        com.alibaba.fastjson.JSONObject jsonObject;
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(url);
        String diqu = jsonObject.getString("province")+jsonObject.getString("city")+jsonObject.getString("area");
        jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonObject.getString("data"));
        String dayWeather = jsonObject.getString("dayWeather");
        String nightTemp = jsonObject.getString("nightTemp");
        String dayTemp = jsonObject.getString("dayTemp");
        messageVo.setTKeyword1(diqu);
        messageVo.setTKeyword2(dayWeather);
        messageVo.setTKeyword3(dayTemp + "℃");
        messageVo.setTKeyword4(nightTemp + "℃");

        String flag = "";
        if (dayWeather.contains("雨")){
            flag = ("今的记得带伞，不带伞莫后悔!");
        }
        if (Integer.parseInt(dayTemp) > 36){
            flag = flag + "今日温度高的很，做点防晒，要不然晚上打球人都要找不到了！";
        }
        if (Integer.parseInt(nightTemp) < 10){
            flag = flag + "温度较低，小心冻成憨憨哟！";
        }
        messageVo.setTInfo("满怀期待，就会所向披靡！");
        if ("".equals(flag)){
            messageVo.setTFlag("今日天气好的很，快出去觅食喽。");
        }
        else {
            messageVo.setTFlag(flag);
        }

        if (caculate() % 100 == 0 || caculate() % 50 == 0){
            messageVo.setTInfo(messageVo.getTInfo() +caculate() );
        }
        for (Object openid:openids) {
            JSONObject resp = configurationService.sendMsg2(messageVo,token,openid.toString(),3);
        }
    }
    //距离生日还有几天
    public int getBirthDay(String addtime) {
        int days = 0;
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cToday = Calendar.getInstance(); // 存今天
            Calendar cBirth = Calendar.getInstance(); // 存毕业日
            cBirth.setTime(myFormatter.parse(addtime)); // 设置毕业日

            cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
            if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
                // 生日已经过了，要算明年的了
                days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
                days += cBirth.get(Calendar.DAY_OF_YEAR);
            } else {
                // 生日还没过        //生日是今年的第多少天  -        //今年的第多少天
                days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }


    //计算出生到现在的时间
    public int caculate() throws ParseException {
        Date now = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date befor = dateFormat1.parse("2002-6-27");
        long day1 = System.currentTimeMillis()/1000/60/60/24;
        System.out.println(day1);
        long day2 = befor.getTime()/1000/60/60/24;
        return (int) (day1 -day2);
    }

}
