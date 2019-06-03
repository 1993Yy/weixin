package com.yy.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Package: com.yy.common.bean
 * @ClassName: Event
 * @Author: Created By Yy
 * @Date: 2019-05-30 14:32
 */
@Data
@Accessors(chain = true)
public class Event {
    /**
     * 开发者微信号
     */
    private String wxid;
    /**
     * 用户openID
     */
    private String userOpenid;

    private String msgType;
    /**
     * 用户ip
     */
    private String userIP;
    /**
     * 关注
     */
    private Subscribe subscribe;
    /**
     * 取消关注
     */
    private Unsubscribe unsubscribe;
    /**
     * 扫码关注
     */
    private Scansubscribe scanSubscribe;
    /**
     * 扫码已经关注
     */
    private Scan scan;
    /**
     * 上报地理位置
     */
    private Location location;
    /**
     * 菜单点击
     */
    private Click click;
    /**
     * 普通文本信息
     */
    private Text text;
    /**
     * 普通图片信息
     */
    private Image image;
    /**
     * 普通语音信息
     */
    private Voice voice;
    /**
     * 普通视频信息
     */
    private Video video;
    @Data
    @AllArgsConstructor
    public static class Video{//普通视频信息
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="video";
        /**
         * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
         */
        private String thumbMediaId;
        /**
         * 语音消息媒体id，可以调用获取临时素材接口拉取数据。
         */
        private String mediaId;
        /**
         * 消息id，64位整型
         */
        private String msgId;
    }
    @Data
    @AllArgsConstructor
    public static class Voice{//普通语音信息
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="voice";
        /**
         * 语音格式，如amr，speex等
         */
        private String format;
        /**
         * 语音消息媒体id，可以调用获取临时素材接口拉取数据。
         */
        private String mediaId;
        /**
         * 消息id，64位整型
         */
        private String msgId;
    }
    @Data
    @AllArgsConstructor
    public static class Image{//普通图片信息
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="image";
        /**
         * 文本消息内容
         */
        private String picUrl;
        /**
         * 图片消息媒体id，可以调用获取临时素材接口拉取数据。
         */
        private String mediaId;
        /**
         * 消息id，64位整型
         */
        private String msgId;
    }
    @Data
    @AllArgsConstructor
    public static class Text{//普通文本信息
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="text";
        /**
         * 文本消息内容
         */
        private String content;
        /**
         * 消息id，64位整型
         */
        private String msgId;
    }
    @Data
    @AllArgsConstructor
    public static class Click{//菜单点击
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="CLICK";
        /**
         * 事件KEY值，与自定义菜单接口中KEY值对应
         */
        private String eventKey;
    }

    @Data
    @AllArgsConstructor
    public static class Location{//上报地理位置
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="LOCATION";
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
    public static class Scan{//扫码
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="SCAN";
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
    public static class Scansubscribe{//扫码关注
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="subscribe";
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
    public static class Unsubscribe{//取消关注
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="unsubscribe";
    }
    @Data
    @AllArgsConstructor
    public static class Subscribe{//关注
        /**
         * 开发者微信号
         */
        private String toUserName;
        /**
         *用户openID
         */
        private String fromUserName;
        /**
         * 创建时间
         */
        private Long createTime;
        /**
         * 消息类型
         */
        private String msgType="event";
        /**
         * 事件类型
         */
        private String event="subscribe";
    }
}
