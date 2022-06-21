package me.tianshuang.task;

import net.steppschuh.markdowngenerator.MarkdownSerializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

/**
 * Created by Poison on 12/04/2017.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:pull-readhub.properties")
public class ScheduleTaskTest {

    @Autowired
    private ScheduleTask scheduleTask;

    @Test
    public void pullNewsFromReadhubTest() throws MarkdownSerializationException, IOException {
        scheduleTask.pullNewsFromReadhubAndPushToDingtalk();
    }

}