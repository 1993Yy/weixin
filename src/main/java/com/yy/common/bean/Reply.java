package com.yy.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Package: com.yy.entity
 * @ClassName: Reply
 * @Author: Created By Yy
 * @Date: 2019-05-06 10:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    private Text text;
    private Image image;
    private Voice voice;
    private Video video;
    private Music music;
    private List<News> news;

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public class Text {
        /** 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示） **/
        private String content;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public class Image {
        /** 通过素材管理中的接口上传多媒体文件，得到的id **/
        private String mediaId;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public  class Voice {
        /** 通过素材管理中的接口上传多媒体文件，得到的id **/
        private String mediaId;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public  class Video {
        /** 通过素材管理中的接口上传多媒体文件，得到的id **/
        private String mediaId;
        /** 视频消息的标题 **/
        private String title;
        /** 视频消息的描述 **/
        private String description;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public  class Music {
        /** 音乐标题 **/
        private String title;
        /** 音乐描述 **/
        private String description;
        /** 音乐链接 **/
        private String MusicUrl;
        /** 高质量音乐链接，WIFI环境优先使用该链接播放音乐 **/
        private String hqMusicUrl;
        /** 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id **/
        private String thumbMediaId;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public  class News { //多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
        /** 图文消息标题 **/
        private String title;
        /** 图文消息描述 **/
        private String description;
        /** 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200 **/
        private String picUrl;
        /** 点击图文消息跳转链接 **/
        private String url;
    }

}
