package me.tianshuang.dto;

import lombok.Data;
import me.tianshuang.domain.MarkdownDO;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class DingTalkDTO {

    private String msgtype = "markdown";

    private MarkdownDO markdown;

}
