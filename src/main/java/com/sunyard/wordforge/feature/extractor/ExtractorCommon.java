package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import java.util.Optional;

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

        if (run == null) {
            // 如果 Run 对象为 null，则直接返回空的样式对象
            return style;
        }

        Font font = run.getFont();
        if (font != null) {
            // 安全地检查并设置样式属性
            style.put("bold", Optional.of(font.getBold()).orElse(false));
            style.put("italic", Optional.of(font.getItalic()).orElse(false));
            style.put("strike", Optional.of(font.getStrikeThrough()).orElse(false));
            style.put("subscript", Optional.of(font.getSubscript()).orElse(false));
            style.put("superscript", Optional.of(font.getSuperscript()).orElse(false));
            style.put("underline", Optional.of(font.getUnderline()).orElse(com.aspose.words.Underline.NONE));
            style.put("font-name", Optional.of(font.getName()).orElse(""));
            style.put("font-size", Optional.of(font.getSize()).orElse(12.0));
            style.put("highlight", font.getHighlightColor() != null);
            style.put("highlight-color", font.getHighlightColor() != null ? font.getHighlightColor().getRGB() : -2);
            style.put("hyperlink", run.getParentParagraph().getParagraphFormat().getStyleIdentifier() == StyleIdentifier.HYPERLINK);
            style.put("color", new int[]{
                    font.getColor().getRed(),
                    font.getColor().getGreen(),
                    font.getColor().getBlue()
            });
        }

        // 对于对齐方式和列表编号的处理，需考虑到Paragraph的存在性
        Paragraph paragraph = run.getParentParagraph();
        if (paragraph != null) {
            style.put("alignment", paragraph.getParagraphFormat().getAlignment());
            if (paragraph.isListItem()) {
                style.put("list-number", paragraph.getListFormat().getListLevelNumber());
            } else {
                style.put("list-number", -1);
            }
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
