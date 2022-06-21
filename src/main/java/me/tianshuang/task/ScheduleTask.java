package me.tianshuang.task;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.tianshuang.domain.MarkdownDO;
import me.tianshuang.domain.TopicDO;
import me.tianshuang.dto.DingTalkDTO;
import me.tianshuang.vo.PageVO;
import net.steppschuh.markdowngenerator.MarkdownSerializationException;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Poison on 12/04/2017.
 */
@Slf4j
@Service
public class ScheduleTask {

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Value("${dingtalk.robot.url}")
    private String dingtalkRobotUrl;

    private Integer lastCursor;

    @Scheduled(cron = "${cron}")
    void pullNewsFromReadhubAndPushToDingtalk() throws IOException, MarkdownSerializationException {
        List<Link> linkList = new ArrayList<>(80);
        LocalDateTime yesterday = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1);
        for (int i = 0; i < 4; i++) {
            pullNewsFromReadhubSince(linkList, yesterday);
        }
        lastCursor = null;
        sendNewsToDingTalk(linkList);
    }

    private void pullNewsFromReadhubSince(List<Link> linkList, LocalDateTime yesterday) throws IOException {
        Request urlRequest = new Request.Builder()
                .url("https://api.readhub.cn/topic?pageSize=20" + (lastCursor != null ? "&lastCursor=" + lastCursor : ""))
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(urlRequest).execute()) {
            PageVO pageVO = OBJECT_MAPPER.readValue(Objects.requireNonNull(response.body()).string(), PageVO.class);
            for (TopicDO topicDO : pageVO.getData()) {
                if (topicDO.getCreatedAt().isAfter(yesterday)) {
                    linkList.add(new Link(topicDO.getTitle(), "https://readhub.cn/topic/" + topicDO.getId()));
                }
            }

            lastCursor = pageVO.getData().get(pageVO.getData().size() - 1).getOrder();
        }
    }

    private void sendNewsToDingTalk(List<Link> linkList) throws MarkdownSerializationException, IOException {
        if (linkList.isEmpty()) {
            return;
        }

        MarkdownDO markdownDO = new MarkdownDO();
        markdownDO.setTitle("Readhub 热门话题");
        markdownDO.setText(new UnorderedList<>(linkList).serialize());

        DingTalkDTO dingTalkMessage = new DingTalkDTO();
        dingTalkMessage.setMarkdown(markdownDO);

        RequestBody requestBody = RequestBody.create(OBJECT_MAPPER.writeValueAsString(dingTalkMessage), MediaType.parse("application/json; charset=utf-8"));
        Request urlRequest = new Request.Builder()
                .url(dingtalkRobotUrl)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(urlRequest).execute()) {
            log.info("Dingtalk HTTP response: " + Objects.requireNonNull(response.body()).string());
        }
    }

}
