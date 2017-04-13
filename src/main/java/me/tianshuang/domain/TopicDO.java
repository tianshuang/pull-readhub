package me.tianshuang.domain;

import lombok.Data;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class TopicDO {

    private int id;

    private String title;

    private NewsDO[] newsArray;

}
