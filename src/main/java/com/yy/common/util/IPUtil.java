package com.yy.common.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @Package: com.yy.common.util
 * @ClassName: IPUtil
 * @Author: Created By Yy
 * @Date: 2019-05-30 14:53
 */
public class IPUtil {

    public static String getIp(HttpServletRequest request) {
        String forwards = request.getHeader("x-forwarded-for");
        if (StrUtil.isBlank(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getRemoteAddr();
        }
        if (StrUtil.isBlank(forwards) || "unknown".equalsIgnoreCase(forwards)) {
            forwards = request.getHeader("X-Real-IP");
        }
        if (forwards != null && forwards.trim().length() > 0) {
            int index = forwards.indexOf(',');
            return (index != -1) ? forwards.substring(0, index) : forwards;
        }
        return forwards;
    }
}
