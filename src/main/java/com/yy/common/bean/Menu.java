package com.yy.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Package: com.yy.common.bean
 * @ClassName: Menu
 * @Author: Created By Yy
 * @Date: 2019-05-11 11:13
 */
@Data
@Accessors(chain = true)
public class Menu {
    /**
     * 是	一级菜单数组，个数应为1~3个
     */
    private String name;
    /**
     * 是	菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     */
    private String type;
    /**
     * click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节
     */
    private String key;
    /**
     * view、miniprogram类型必须	网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，
     * 不支持小程序的老版本客户端将打开本url。
     */
    private String url;
    /**
     * media_id类型和view_limited类型必须	调用新增永久素材接口返回的合法media_id
     */
    private String mediaID;
    /**
     * miniprogram类型必须	小程序的appid（仅认证公众号可配置）
     */
    private String appID;
    /**
     * miniprogram类型必须	小程序的页面路径
     */
    private String pagePath;
    /**
     * 否	二级菜单数组，个数应为1~5个
     */
    private List<Menu> sub_button;

    public enum MenuType{
        /**
         * 点击推事件用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，
         * 开发者可以通过自定义的key值与用户进行交互；
         */
        click,
        /**
         * 跳转URL用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，
         * 可与网页授权获取用户基本信息接口结合，获得用户基本信息。
         */
        view,
        /**
         * 扫码推事件用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），
         * 且会将扫码的结果传给开发者，开发者可以下发消息。
         */
        scancode_push,
        /**
         * 弹出地理位置选择器用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，
         * 同时收起位置选择工具，随后可能会收到开发者下发的消息。
         */
        location_select;
    }

}
