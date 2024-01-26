package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.sunyard.wordforge.complex.function.ThrowableFunction;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解析提取
 *
 * @author Archer
 */
public class ExtractorDoc {

    /**
     * 提取文档内容
     *
     * @param file 源文档
     * @return 提取后的文档内容
     */
    public static JSONObject extract0(MultipartFile file) throws Exception {
        return extractContent(file, inputStream -> extractWithTitle(inputStream, false, false, true, true, true));
    }

    /**
     * 提取文档内容
     *
     * @param file 源文档
     * @return 提取后的文档内容
     */
    public static JSONObject extract1(MultipartFile file) throws Exception {
        return extractContent(file, inputStream -> extractStructuredContent(inputStream, false, false, true, false, true));
    }

    /**
     * 提取文档内容
     *
     * @param file 源文档
     * @return 提取后的文档内容
     */
    public static JSONObject extract2(MultipartFile file) throws Exception {
        return extractContent(file, inputStream -> extractStructuredContent(inputStream, false, true, false, false, true));
    }

    /**
     * 提取文档内容的通用方法
     *
     * @param file          源文档
     * @param dataExtractor 数据提取函数
     * @return 提取后的文档内容
     */
    private static JSONObject extractContent(
            MultipartFile file,
            ThrowableFunction<InputStream, JSONArray> dataExtractor
    )
            throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            JSONArray data = dataExtractor.apply(inputStream);
            JSONObject result = new JSONObject();
            result.put("data", data);
            result.put("file_name", fileName);

            return result;
        }
    }

    /**
     * 提取文档内容，保持内容的整体性
     *
     * @param inputStream             源文档输入流
     * @param includeHeadingInContent 是否提取标题内容
     * @return 提取后的文档内容
     */
    public static JSONArray extractStructuredContent(
            InputStream inputStream,
            boolean includeHeadingInContent,
            boolean extractAsWholeParagraph,
            boolean extractStyle,
            boolean extractTitle,
            boolean filterEmptyContent
    )
            throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();

        for (Section section : doc.getSections()) {
            NodeCollection nodes = section.getBody().getChildNodes(NodeType.ANY, false);
            for (Node node : (Iterable<Node>) nodes) {
                if (node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph para = (Paragraph) node;
                    if (!para.isInCell()) { // 如果段落不属于表格
                        JSONObject paragraphInfo = extractParagraph(
                                para,
                                includeHeadingInContent,
                                extractAsWholeParagraph,
                                extractStyle,
                                extractTitle,
                                filterEmptyContent
                        );
                        if (paragraphInfo != null) {
                            data.add(paragraphInfo);
                        }
                    }
                } else if (node.getNodeType() == NodeType.TABLE) {
                    Table table = (Table) node;
                    JSONObject tableInfo = extractTable(
                            table,
                            extractAsWholeParagraph,
                            extractStyle,
                            extractTitle,
                            false
                    );
                    data.add(tableInfo);
                }
            }
        }

        inputStream.close();

        return data;
    }

    /**
     * 提取文档内容
     *
     * @param inputStream             源文档输入流
     * @param includeHeadingInContent 是否提取标题内容
     * @return 提取后的文档内容
     */
    public static JSONArray extractWithTitle(
            InputStream inputStream,
            boolean includeHeadingInContent,
            boolean extractAsWholeParagraph,
            boolean extractStyle,
            boolean extractTitle,
            boolean filterEmptyContent
    )
            throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();

        // 遍历文档的所有段落和表格
        for (Section section : doc.getSections()) {
            // 提取所有段落
            NodeCollection paragraphs = section.getBody().getChildNodes(NodeType.PARAGRAPH, false);
            for (Paragraph para : (Iterable<Paragraph>) paragraphs) {
                if (para.isInCell()) {
                    continue; // 如果段落属于表格，则跳过
                }

                // 提取段落内容和标题
                JSONObject paragraphInfo = extractParagraph(
                        para,
                        includeHeadingInContent,
                        extractAsWholeParagraph,
                        extractStyle,
                        extractTitle,
                        filterEmptyContent
                );
                if (paragraphInfo != null) {
                    data.add(paragraphInfo);
                }
            }

            // 提取所有表格
            NodeCollection tables = section.getBody().getChildNodes(NodeType.TABLE, false);
            for (Table table : (Iterable<Table>) tables) {
                JSONObject tableInfo = extractTable(
                        table,
                        extractAsWholeParagraph,
                        extractStyle,
                        extractTitle,
                        false
                );
                data.add(tableInfo);
            }
        }

        inputStream.close();

        return data;
    }

    /**
     * 提取段落内容
     *
     * @param paragraph               源段落
     * @param includeHeadingInContent 是否包含标题内容
     * @param extractAsWholeParagraph 是否作为整个段落提取
     * @param extractStyle            是否提取样式
     * @param extractTitle            是否提取标题
     * @param filterEmptyContent      是否过滤空内容
     * @return 提取后的段落内容
     */
    private static JSONObject extractParagraph(
            Paragraph paragraph,
            boolean includeHeadingInContent,
            boolean extractAsWholeParagraph,
            boolean extractStyle,
            boolean extractTitle,
            boolean filterEmptyContent
    ) {
        // 提取整个段落的内容
        String paragraphText = paragraph.getText().trim();

        // 如果设置了过滤空内容并且段落内容为空，则返回null
        if (filterEmptyContent && paragraphText.isEmpty()) {
            return null;
        }

        // 如果不包含标题内容并且当前段落是标题，则返回空的内容
        if (!includeHeadingInContent && paragraph.getParagraphFormat().isHeading()) {
            return null;
        }

        JSONObject paragraphInfo = new JSONObject();
        if (extractTitle) {
            String title = findClosestHeading(paragraph);
            paragraphInfo.put("title", title);
        }


        if (extractAsWholeParagraph) {
            // 提取整个段落的内容
            JSONObject content = new JSONObject();
            if (extractStyle) {
                Run firstRun = (Run) paragraph.getChildNodes(NodeType.RUN, true).get(0);
                JSONObject style = extractStyle(firstRun);
                content.put("style", style);
            }
            content.put("text", paragraphText);
            paragraphInfo.put("content", content);
        } else {
            // 分段提取内容
            JSONArray content = new JSONArray();
            for (Run run : (Iterable<Run>) paragraph.getChildNodes(NodeType.RUN, true)) {
                String runText = run.getText().trim();
                if (filterEmptyContent && runText.isEmpty()) {
                    continue; // 跳过空内容
                }

                JSONObject textInfo = new JSONObject();
                if (extractStyle) {
                    JSONObject style = extractStyle(run);
                    textInfo.put("style", style);
                }
                textInfo.put("text", runText);
                content.add(textInfo);
            }
            paragraphInfo.put("content", content);
        }

        return paragraphInfo;
    }

    /**
     * 提取表格内容
     *
     * @param table                   源表格
     * @param extractAsWholeParagraph 是否作为整个段落提取
     * @param extractStyle            是否提取样式
     * @param extractTitle            是否提取标题
     * @param filterEmptyContent      是否过滤空内容
     * @return 提取后的表格内容
     */
    private static JSONObject extractTable(
            Table table,
            boolean extractAsWholeParagraph,
            boolean extractStyle,
            boolean extractTitle,
            boolean filterEmptyContent
    ) {
        JSONObject tableInfo = new JSONObject();

        if (extractTitle) {
            String title = findClosestHeading(table);
            tableInfo.put("title", title);
        }

        JSONArray contentArray = new JSONArray();
        JSONObject tableContent = new JSONObject();
        JSONArray styleArray = new JSONArray();
        JSONArray tableArray = new JSONArray();

        for (Row row : table.getRows()) {
            JSONArray rowStyleData = new JSONArray();
            JSONArray rowTextData = new JSONArray();

            for (Cell cell : row.getCells()) {
                JSONArray cellTextArray = new JSONArray();
                JSONArray cellStyleArray = new JSONArray();

                for (Paragraph para : cell.getParagraphs()) {
                    StringBuilder cellTextBuilder = new StringBuilder();

                    for (Run run : (Iterable<Run>) para.getChildNodes(NodeType.RUN, true)) {
                        if (extractStyle) {
                            JSONObject style = extractStyle(run);
                            cellStyleArray.add(style);
                        }
                        cellTextBuilder.append(run.getText());
                    }

                    cellTextArray.add(cellTextBuilder.toString().trim());
                    if (!extractAsWholeParagraph) {
                        rowStyleData.add(cellStyleArray);
                    }
                }

                if (filterEmptyContent) {
                    List<String> cellTextList = new ArrayList<>();
                    for (Object text : cellTextArray) {
                        if (text instanceof String) {
                            cellTextList.add((String) text);
                        }
                    }
                    if (cellTextList.isEmpty() || cellTextList.stream().allMatch(String::isEmpty)) {
                        continue;
                    }
                }

                if (extractAsWholeParagraph) {
                    rowTextData.add(cellTextArray);
                } else {
                    rowStyleData.add(cellStyleArray);
                }
            }

            if (extractStyle && !extractAsWholeParagraph) {
                styleArray.add(rowStyleData);
            }
            tableArray.add(rowTextData);
        }

        if (extractStyle && !extractAsWholeParagraph) {
            tableContent.put("style", styleArray);
        }
        tableContent.put("table", tableArray);
        contentArray.add(tableContent);
        tableInfo.put("content", contentArray);

        return tableInfo;
    }

    /**
     * 提取样式
     *
     * @param run 源文本
     * @return 提取后的样式
     */
    private static JSONObject extractStyle(Run run) {
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
        style.put("highlight-color", run.getFont().getHighlightColor() != null ? run.getFont().getHighlightColor().getRGB() : -2);
        style.put("hyperlink", run.getParentParagraph().getParagraphFormat().getStyleIdentifier() == StyleIdentifier.HYPERLINK);
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
