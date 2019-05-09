package com.yy.schedule;

import com.yy.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Package: com.yy.schedule
 * @ClassName: ScheduleTask
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:52
 */
@Component
public class ScheduleTask {

    @Autowired
    private WeiXinService weiXinService;

    public void scheduleUpdateToken(){

    }
}
