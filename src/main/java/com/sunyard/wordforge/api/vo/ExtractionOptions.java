package com.sunyard.wordforge.api.vo;

import lombok.Data;

/**
 * 提取选项
 *
 * @author Archer
 */
@Data
public class ExtractionOptions {
    /**
     * 是否提取标题作为内容
     */
    private Boolean includeHeadingInContent;

    /**
     * 是否作为整个段落提取
     */
    private Boolean extractAsWholeParagraph;

    /**
     * 是否提取样式
     */
    private Boolean extractStyle;

    /**
     * 是否提取标题
     */
    private Boolean extractTitle;

    /**
     * 是否过滤空内容
     */
    private Boolean filterEmptyContent;

    /**
     * 填充默认值
     */
    public void fillDefaultValues() {
        this.includeHeadingInContent = this.includeHeadingInContent != null ? this.includeHeadingInContent : false;
        this.extractAsWholeParagraph = this.extractAsWholeParagraph != null ? this.extractAsWholeParagraph : true;
        this.extractStyle = this.extractStyle != null ? this.extractStyle : false;
        this.extractTitle = this.extractTitle != null ? this.extractTitle : false;
        this.filterEmptyContent = this.filterEmptyContent != null ? this.filterEmptyContent : true;
    }
}
