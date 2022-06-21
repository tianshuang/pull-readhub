package me.tianshuang.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class TopicDO {

    private String id;

    private int order;

    private String title;

    private NewsDO[] newsArray;

    private LocalDateTime createdAt;

    public String getTitle() {
        return title == null ? null : title.trim();
    }

}
