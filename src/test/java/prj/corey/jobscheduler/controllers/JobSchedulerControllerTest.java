package prj.corey.jobscheduler.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prj.corey.jobscheduler.models.User;
import prj.corey.jobscheduler.services.jobSchedulerService.JobSchedulerService;
import prj.corey.jobscheduler.services.userService.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class JobSchedulerControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JobSchedulerService jobSchedulerService;

    private JobSchedulerController jobSchedulerController = new JobSchedulerController(jobSchedulerService, userService);

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password1");
    }

    @Test
    public void testGetJobsReturnsOkResponseWhenUserLoggedIn() throws SchedulerException {
        // given
        doReturn(Optional.of(testUser)).when(userService).getCurrentUsername();
        doReturn(Collections.emptyList()).when(jobSchedulerService).getAllJobs(testUser.getUsername());

        // when
        ResponseEntity response = jobSchedulerController.getJobs();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
