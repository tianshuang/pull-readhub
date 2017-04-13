package me.tianshuang.vo;

import lombok.Data;
import me.tianshuang.domain.TopicDO;

import java.util.List;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class PageVO {

    private List<TopicDO> data;

    private int pageSize;

    private int totalItems;

    private int totalPages;

}
