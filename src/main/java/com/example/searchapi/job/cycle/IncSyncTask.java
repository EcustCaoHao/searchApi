package com.example.searchapi.job.cycle;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Springboot定时任务
 */
@Component
public class IncSyncTask {

    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void execute(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        System.out.println("时间：" + df.format(new Date()));
    }

}
