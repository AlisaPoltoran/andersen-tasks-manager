package report_sender.listener.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;

public class SchedulerListener extends GenericServlet {

    @Override
    public void init() throws ServletException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = JobBuilder.newJob(SendJob.class)
                    .withIdentity("reportDailySenderJob")
                    .build();

            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("reportDailySender")
                    .startNow()
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0 0 23 ? * MON-FRI")
                            .inTimeZone(TimeZone.getTimeZone("Europe/Minsk")))
                    .build();

//            Trigger trigger = newTrigger()
//                    .withIdentity("trigger1", "group1")
//                    .withSchedule(dailyAtHourAndMinute(23, 00))
//                    .forJob(job)
//                    .build();

            SchedulerFactory schFactory = new StdSchedulerFactory();
            Scheduler sch = schFactory.getScheduler();
            sch.start();
            sch.scheduleJob(job, trigger);
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    }
}
