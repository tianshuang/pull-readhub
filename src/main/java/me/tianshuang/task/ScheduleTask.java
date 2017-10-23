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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poison on 12/04/2017.
 */
@Service
public class ScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Value("${dingtalk.robot.url}")
    private String dingtalkRobotUrl;

    private Integer lastCursor;

    @Scheduled(cron = "0 0 9 * * *")
    void pullNewsFromReadhubAndPushToDingtalk() {
        List<Link> linkList = new ArrayList<>(80);
        LocalDateTime yesterday = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1);
        for (int i = 0; i < 4; i++) {
            pullNewsFromReadhubSince(linkList, yesterday);
        }
        lastCursor = null;
        sendNewsToDingTalk(linkList);
    }

    private void pullNewsFromReadhubSince(List<Link> linkList, LocalDateTime yesterday) {
        Request urlRequest = new Request.Builder()
                .url("https://api.readhub.me/topic?pageSize=20" + (lastCursor != null ? "&lastCursor=" + lastCursor : ""))
                .build();
        try (Response response = okHttpClient.newCall(urlRequest).execute()) {
            PageVO pageVO = JSON.parseObject(response.body().string(), PageVO.class);
            for (TopicDO topicDO : pageVO.getData()) {
                if (topicDO.getCreatedAt().isAfter(yesterday)) {
                    linkList.add(new Link(topicDO.getTitle(), topicDO.getNewsArray()[0].getUrl()));
                }
            }

            lastCursor = pageVO.getData().get(pageVO.getData().size() - 1).getOrder();
        } catch (IOException e) {
            logger.error("Pull exception: " + e.getMessage());
        }
    }

    private void sendNewsToDingTalk(List<Link> linkList) {
        if (linkList.isEmpty()) {
            return;
        }

        MarkdownDO markdownDO = new MarkdownDO();
        markdownDO.setTitle("Readhub 热门话题");
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
