package me.tianshuang.task;

import com.alibaba.fastjson.JSON;
import me.tianshuang.domain.MarkdownDO;
import me.tianshuang.domain.TopicDO;
import me.tianshuang.dto.DingTalkDTO;
import me.tianshuang.vo.PageVO;
import net.steppschuh.markdowngenerator.MarkdownSerializationException;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Poison on 12/04/2017.
 */
@Service
public class ScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    private static final String READHUB_ME_URL = "https://api.readhub.me/topic?pageSize=12";

    @Value("${dingtalk.robot.url}")
    private String dingtalkRobotUrl;

    private OkHttpClient okHttpClient = new OkHttpClient();

    private Set<Integer> lastIdSet = new HashSet<>();

    @Scheduled(cron = "0 0 9,18 * * *")
    void pullNewsFromReadhub() {
        Request urlRequest = new Request.Builder()
                .url(READHUB_ME_URL)
                .build();
        try (Response response = okHttpClient.newCall(urlRequest).execute()) {
            PageVO pageVO = JSON.parseObject(response.body().string(), PageVO.class);
            List<Link> linkList = new ArrayList<>(12);

            for (TopicDO topicDO : pageVO.getData()) {
                if (lastIdSet.contains(topicDO.getId())) {
                    lastIdSet.remove(topicDO.getId());
                } else {
                    linkList.add(new Link(topicDO.getTitle(), topicDO.getNewsArray()[0].getUrl()));
                    lastIdSet.add(topicDO.getId());
                }
            }

            sendNewsToDingTalk(linkList);

        } catch (IOException e) {
            logger.error("Pull exception: " + e.getMessage());
        }
    }

    private void sendNewsToDingTalk(List<Link> linkList) {
        if (linkList.isEmpty()) {
            return;
        }

        MarkdownDO markdownDO = new MarkdownDO();
        markdownDO.setTitle("Readhub 最新消息");
        try {
            markdownDO.setText(new UnorderedList<>(linkList).serialize());
        } catch (MarkdownSerializationException e) {
            logger.error("UnorderedList serialize exception: " + e.getMessage());
            return;
        }
        DingTalkDTO dingTalkMessage = new DingTalkDTO();
        dingTalkMessage.setMarkdown(markdownDO);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(dingTalkMessage));
        Request urlRequest = new Request.Builder()
                .url(dingtalkRobotUrl)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(urlRequest).execute()) {
            logger.info("Dingtalk HTTP response: " + response.body().string());
        } catch (IOException e) {
            logger.error("Send message to dingtalk exception: " + e.getMessage());
        }
    }

}
