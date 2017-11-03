package me.tianshuang.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Poison on 12/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:pull-readhubme.properties")
public class ScheduleTaskTest {

    @Autowired
    private ScheduleTask scheduleTask;

    @Test
    public void pullNewsFromReadhubTest() {
        scheduleTask.pullNewsFromReadhubAndPushToDingtalk();
    }

}