package com.mk.timer;

import com.mk.report.JJResults;
import com.mk.tool.stock.GlobalContext;
import com.mk.tool.stock.StragetyZTBottom;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SpringScheduledBottomZTDemo {
//    @Scheduled(cron = "0 0/5 9-17 * * MON-FRI")
//    @Scheduled(cron = "0 31 11 * * *")
@Scheduled(cron = "0/10 * * * * *")
    public void testScheduled(){
        System.out.println("springScheduled run:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        try {
            JJResults.cleaar();
            GlobalContext.clear();
            GlobalContext.setFinish(false);
            StragetyZTBottom.main(null);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
