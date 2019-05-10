package com.yy.common.util;

import cn.hutool.extra.qrcode.QrConfig;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * @Package: com.yy.common.util
 * @ClassName: ImgUtil
 * @Author: Created By Yy
 * @Date: 2019-05-10 17:10
 */
public class ImgUtil {
    /**
     * 画圆角图片
     * @param image
     * @param radius 圆角
     * @return
     */
    public static BufferedImage drawRadius(BufferedImage image, int radius){
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, radius,
                radius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    /**
     * 画边框
     * @return
     */
    public static BufferedImage drawBoder(BufferedImage image, int margin, int radius, Color color){
        int w = image.getWidth() + margin;
        int h = image.getHeight() + margin;
        BufferedImage bufferedImag=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImag.createGraphics();
        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(color);
        graphics.fill(new RoundRectangle2D.Double(0, 0, w, h, radius, radius) );
        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.drawImage(image,margin>>1,margin>>1,image.getWidth(),image.getHeight(),null);
        graphics.dispose();
        return bufferedImag;
    }

    /**
     * 画logo
     * @param qrImg
     * @param logo
     * @return
     */
    public static BufferedImage drawLogo(BufferedImage qrImg,BufferedImage logo){
        final int qrWidth = qrImg.getWidth();
        final int qrHeight = qrImg.getHeight();

        //logo 长宽
        int logoWidth = logo.getWidth()>qrWidth>>2?qrWidth>>2:logo.getWidth();
        int logoHeight =logo.getHeight()>qrHeight>>2?qrHeight>>2:logo.getHeight();

        int logoOffsetX = (qrWidth - logoWidth) >> 1;
        int logoOffsetY = (qrHeight - logoHeight) >> 1;
        //插入logo
        Graphics2D graphics = qrImg.createGraphics();
        graphics.drawImage(logo, logoOffsetX, logoOffsetY, logoWidth, logoHeight, null);
        graphics.dispose();
        return qrImg;
    }
}
