package com.example.searchapi.job.once;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Springboot一次性任务
 */
@Component
public class FullSyncTask {

    @Scheduled(initialDelay = 1000,fixedRate = Long.MAX_VALUE)
    public void execute(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        System.out.println("时间：Full" + df.format(new Date()));
    }
}
