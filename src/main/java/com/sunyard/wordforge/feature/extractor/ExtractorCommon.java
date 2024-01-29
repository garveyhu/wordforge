package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;

/**
 * 提取公共类
 *
 * @author Archer
 */
public class ExtractorCommon {

    /**
     * 提取样式
     *
     * @param run 源文本
     * @return 提取后的样式
     */
    public static JSONObject extractStyle(Run run) {
        JSONObject style = new JSONObject();
        style.put("bold", run.getFont().getBold());
        style.put("italic", run.getFont().getItalic());
        style.put("strike", run.getFont().getStrikeThrough());
        style.put("subscript", run.getFont().getSubscript());
        style.put("superscript", run.getFont().getSuperscript());
        style.put("underline", run.getFont().getUnderline());
        style.put("font-name", run.getFont().getName());
        style.put("font-size", run.getFont().getSize());
        style.put("highlight", run.getFont().getHighlightColor() != null);
        style.put(
            "highlight-color",
            run.getFont().getHighlightColor() != null ? run.getFont().getHighlightColor().getRGB() : -2
        );
        style.put(
            "hyperlink",
            run.getParentParagraph().getParagraphFormat().getStyleIdentifier() == StyleIdentifier.HYPERLINK
        );
        style.put(
            "color",
            new int[] {
                run.getFont().getColor().getRed(),
                run.getFont().getColor().getGreen(),
                run.getFont().getColor().getBlue()
            }
        );
        style.put("alignment", run.getParentParagraph().getParagraphFormat().getAlignment());

        // 处理列表编号（如果存在）
        if (run.getParentParagraph().isListItem()) {
            style.put("list-number", run.getParentParagraph().getListFormat().getListLevelNumber());
        } else {
            style.put("list-number", -1);
        }

        return style;
    }

    /**
     * 查找节点标题
     *
     * @param node 源节点
     * @return 节点标题
     */
    public static String findClosestHeading(Node node) {
        Node currentNode = node;

        while (currentNode != null && currentNode.getNodeType() != NodeType.DOCUMENT) {
            if (currentNode.getNodeType() == NodeType.PARAGRAPH) {
                Paragraph para = (Paragraph) currentNode;
                Style style = para.getParagraphFormat().getStyle();

                if (style != null) {
                    String styleName = style.getName();
                    // 检查英文和中文的标题样式
                    if (styleName.startsWith("Heading") || styleName.startsWith("标题")) {
                        return para.getText().trim();
                    }
                }
            }
            currentNode = currentNode.getPreviousSibling();
        }
        return "";
    }
}
