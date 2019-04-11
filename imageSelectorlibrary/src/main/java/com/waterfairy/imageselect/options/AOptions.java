package com.waterfairy.imageselect.options;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/11 17:15
 * @info:
 */
public abstract class AOptions implements Options {
    protected String tag;

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
