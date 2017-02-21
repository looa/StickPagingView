package org.looa.ndkencode;

/**
 * 第二页顶部tab的信息，包括tab的名称和对应的url，以及其他必要的信息
 * <p>
 * Created by ranxiangwei on 2017/2/20.
 */

public class StickyPageInfo {
    private String name;
    private String url;

    public StickyPageInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
