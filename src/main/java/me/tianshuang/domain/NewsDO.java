package me.tianshuang.domain;

import lombok.Data;

/**
 * Created by Poison on 12/04/2017.
 */
@Data
public class NewsDO {

    private String url;

    public String getUrl() {
        return url == null ? null : url.trim();
    }

}
