package me.tianshuang.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class TopicDO {

    private int id;

    private int order;

    private String title;

    private NewsDO[] newsArray;

    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

}
