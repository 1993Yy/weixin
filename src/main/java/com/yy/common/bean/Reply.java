package com.yy.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Package: com.yy.common.bean
 * @ClassName: Reply
 * @Author: Created By Yy
 * @Date: 2019-05-30 15:37
 */
@Data
@Accessors(chain = true)
public class Reply {
    /**
     * 回复文本消息
     */
    private Content content;
    /**
     * 图片
     */
    private Image image;

    private Voice voice;

    private Video video;

    private Music music;

    private List<News> news;

    @Data
    @Accessors(chain = true)
    public static class News{

        private String title;

        private String description;

        private String picUrl;

        private String url;
    }

    @Data
    @Accessors(chain = true)
    public static class Music{
        private String thumbMediaId;
        /**
         * 不是必须
         */
        private String title;
        /**
         * 不是必须
         */
        private String description;
        /**
         * 不是必须
         */
        private String musicURL;
        /**
         * 不是必须
         */
        private String HQMusicUrl;
    }

    @Data
    @Accessors(chain = true)
    public static class Video{
        private String mediaId;
        /**
         * 不是必须
         */
        private String title;
        /**
         * 不是必须
         */
        private String description;
    }

    @Data
    @Accessors(chain = true)
    public static class Voice{
        private String mediaId;
    }

    @Data
    @Accessors(chain = true)
    public static class Image{
        private String mediaId;
    }

    @Data
    @Accessors(chain = true)
    public static class Content{
        private String content;
    }

    /**
     * 消息类型
     */
    public enum MsgType{
        text,
        image,
        voice,
        video,
        music,
        news;
    }
}
