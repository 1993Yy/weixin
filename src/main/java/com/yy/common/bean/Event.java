package com.yy.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Package: com.yy.entity
 * @ClassName: Event
 * @Author: Created By Yy
 * @Date: 2019-05-06 10:16
 */
@Data
@Accessors(chain = true)
public class Event {

    /**
     * 开发者微信号--------toUserName
     */
    private String wxID;
    /**
     * 发送方帐号（一个OpenID）-----fromUserName
     */
    private String openID;

    /**
     * 普通文本
     */
    private Content content;
    /**
     * 关注
     */
    private Subscribe subscribe;
    /**
     * 取关
     */
    private Unsubscribe unsubscribe;
    /**
     * 扫描(已关注)
     */
    private Scan scan;
    /**
     * 扫描关注
     */
    private ScanSubscribe scanSubscribe;
    /**
     * 上报地理位置
     */
    private Location location;
    /**
     * 点击菜单拉取消息 (注意：点击菜单弹出子菜单,不会产生上报)
     */
    private Click click;
    /**
     * 点击菜单跳转链接 (注意：点击菜单弹出子菜单,不会产生上报)
     */
    private View view;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {//普通文本
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        /**
         * 上报文本
         */
        private String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Subscribe {//关注
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "subscribe";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Unsubscribe {//取关
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "unsubscribe";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Scan {//扫描(已关注)
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "SCAN";

        /**
         * 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
         */
        private String eventKey;
        /**
         * 二维码的ticket，可用来换取二维码图片
         */
        private String ticket;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScanSubscribe {//扫描关注
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "subscribe";

        /**
         * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
         */
        private String eventKey;
        /**
         * 二维码的ticket，可用来换取二维码图片
         */
        private String ticket;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {//上报地理位置
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "LOCATION";

        /**
         * 地理位置纬度
         */
        private String latitude;
        /**
         * 地理位置经度
         */
        private String longitude;
        /**
         * 地理位置精度
         */
        private String precision;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Click {//点击菜单拉取消息 (注意：点击菜单弹出子菜单,不会产生上报)
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "VIEW";

        /**
         * 事件KEY值，与自定义菜单接口中KEY值对应
         */
        private String eventKey;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class View {//点击菜单跳转链接 (注意：点击菜单弹出子菜单,不会产生上报)
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         * 发送方帐号（一个OpenID）
         */
        private String fromUserName;
        /**
         * 消息创建时间 （整型）
         */
        private String createTime;
        private String msgType = "event";
        private String event = "VIEW";

        /**
         * 事件KEY值，设置的跳转URL
         */
        private String eventKey;
    }
}
