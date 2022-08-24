package com.github.binarywang.demo.wx.mp.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * restTemplate 远程访问工具类
 *
 * @author gbx
 * @since 2021-02-26 14:36
 */
public class RestClientUtils {


    /**
     * 使用restTemplate发送post请求
     *
     * @param restTemplate 请求工具
     * @param uri          请求路径
     * @param token        访问令牌
     * @param jsonObject   请求参数
     * @param message      请求功能详情说明
     * @return 响应结果
     */
    public static String sendPostString(RestTemplate restTemplate, String uri, String token, JSONObject jsonObject, String message) {
        String body;
        try {
            HttpEntity<String> formEntity = getFormEntity(jsonObject, token);
            body = restTemplate.postForEntity(uri, formEntity, String.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("调用上游服务器失败：" + message, e);
        }
        return body;
    }

    /**
     * 获取POST请求的请求体对象
     *
     * @param jsonObject 封装请求参数信息的实体
     * @param token      接口访问令牌
     * @return 处理结果
     */
    public static HttpEntity<String> getFormEntity(JSONObject jsonObject, String token) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if (StringUtils.isNotBlank(token)) {
            headers.add("token", token);
        }

        HttpEntity<String> formEntity;
        if (jsonObject != null) {
            formEntity = new HttpEntity<>(jsonObject.toString(), headers);
        } else {
            formEntity = new HttpEntity<>(headers);
        }
        return formEntity;
    }
}
