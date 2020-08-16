package prj.corey.jobscheduler.configuration;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import prj.corey.jobscheduler.services.SchedulerService;
import prj.corey.jobscheduler.services.UserService;

@Configuration
public class SchedulerConfiguration {

    @Bean
    public Scheduler getScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public SchedulerService getSchedulerService() {
        return SchedulerService.getInstance();
    }

    @Bean
    public UserService getUserService() {
        return UserService.getInstance();
    }
}
