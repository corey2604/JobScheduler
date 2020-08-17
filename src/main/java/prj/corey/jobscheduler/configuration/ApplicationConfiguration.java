package prj.corey.jobscheduler.configuration;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import prj.corey.jobscheduler.services.schedulerService.SchedulerService;
import prj.corey.jobscheduler.services.schedulerService.SchedulerServiceImpl;
import prj.corey.jobscheduler.services.userService.UserService;
import prj.corey.jobscheduler.services.userService.UserServiceImpl;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Scheduler getScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public SchedulerService getSchedulerService() {
        return SchedulerServiceImpl.getInstance();
    }

    @Bean
    public UserService getUserService() {
        return UserServiceImpl.getInstance();
    }
}
