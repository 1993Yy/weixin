package com.yy.schedule;

import com.yy.service.WeiXinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Package: com.yy.schedule
 * @ClassName: ScheduleTask
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:52
 */
@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private WeiXinService weiXinService;

    @Scheduled(fixedRate = 5*60*1000)
    public void scheduleUpdateToken(){
        log.info("更新token");
        weiXinService.scheduleUpdateToken();
    }
}
