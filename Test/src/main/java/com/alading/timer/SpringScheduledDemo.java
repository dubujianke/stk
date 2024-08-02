//package com.alading.timer;
//
//import com.alading.report.JJResults;
//import com.alading.tool.stock.GlobalContext;
//import com.alading.tool.stock.StragetyZTKN;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Component
//public class SpringScheduledDemo {
////    @Scheduled(cron = "0 0/5 9-17 * * MON-FRI")
////    @Scheduled(cron = "0 31 11 * * *")
//@Scheduled(cron = "0/10 * * * * *")
//    public void testScheduled(){
//        System.out.println("springScheduled run:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
//        try {
//            JJResults.cleaar();
//            GlobalContext.clear();
//            GlobalContext.setFinish(false);
//            StragetyZTKN.main(null);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
