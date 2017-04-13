package me.tianshuang.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Poison on 12/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleTaskTest {

    @Autowired
    private ScheduleTask scheduleTask;

    @Test
    public void pullNewsFromReadhubTest() {
        scheduleTask.pullNewsFromReadhub();
    }

}