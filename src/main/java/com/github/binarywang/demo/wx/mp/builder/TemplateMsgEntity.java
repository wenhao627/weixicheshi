package com.github.binarywang.demo.wx.mp.builder;

import lombok.Data;

import java.io.Serializable;

@Data

public class TemplateMsgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Integer id;
    /**
     * 标题
     */
    private String tTitle;
    /**
     * 第一行
     */
    private String tKeyword1;
    /**
     * 第二行
     */
    private String tKeyword2;
    /**
     * 第三行
     */
    private String tKeyword3;
    /**
     * 第四行
     */
    private String tKeyword4;
    /**
     * 第四行
     */
    private String tKeyword5;
    /**
     * 第四行
     */
    private String tKeyword6;
    /**
     * 第四行
     */
    private String tKeyword7;
    /**
     * 第四行
     */
    private String tKeyword8;
    /**
     * 备注
     */
    private String tRemark;
    /**
     * 提示
     */
    private String tInfo;
    /**
     * 提示
     */
    private String tFlag;
    /**
     * 提示
     */
    private String tBirth;
    /**
     * 跳转连接
     */
    private String tUrl;
    /**
     * 模板编码
     */
    private String tCode;
    /**
     * 状态
     */
    private int tStatus;
}
