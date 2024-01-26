package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.InputStream;
import java.util.*;
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
    public static JSONObject extractFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        String fileName = file.getOriginalFilename();
        JSONArray data = extractWithTitle(inputStream, false);
        JSONObject result = new JSONObject();
        result.put("data", data);
        result.put("file_name", fileName);

        return result;
    }

    /**
     * 提取文档内容
     *
     * @param inputStream             源文档输入流
     * @param includeHeadingInContent 是否提取标题内容
     * @return 提取后的文档内容
     */
    public static JSONArray extractWithTitle(InputStream inputStream, boolean includeHeadingInContent) throws Exception {
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
                JSONObject paragraphInfo = extractParagraph(para, includeHeadingInContent);
                if (paragraphInfo != null) {
                    data.add(paragraphInfo);
                }
            }

            // 提取所有表格
            NodeCollection tables = section.getBody().getChildNodes(NodeType.TABLE, false);
            for (Table table : (Iterable<Table>) tables) {
                JSONObject tableInfo = extractTable(table);
                if (tableInfo != null) {
                    data.add(tableInfo);
                }
            }
        }

        inputStream.close();

        return data;
    }


    /**
     * 提取段落内容
     *
     * @param paragraph               源段落
     * @param includeHeadingInContent 是否提取标题内容
     * @return 提取后的段落内容
     */
    private static JSONObject extractParagraph(Paragraph paragraph, boolean includeHeadingInContent) {
        if (paragraph.getChildNodes(NodeType.RUN, true).getCount() == 0) {
            return null;
        }

        String title = findClosestHeading(paragraph);
        JSONObject paragraphInfo = new JSONObject();
        paragraphInfo.put("title", title);
        JSONArray content = new JSONArray();
        if (!includeHeadingInContent && paragraph.getParagraphFormat().isHeading()) {
            // 如果不包含标题内容并且当前段落是标题，则跳过内容提取
            paragraphInfo.put("content", content);
            return paragraphInfo;
        }

        // 提取段落中的文本和样式
        for (Run run : (Iterable<Run>) paragraph.getChildNodes(NodeType.RUN, true)) {
            JSONObject textInfo = new JSONObject();
            JSONObject style = new JSONObject();
            style.put("bold", run.getFont().getBold());
            style.put("italic", run.getFont().getItalic());
            style.put("color", new int[]{
                    run.getFont().getColor().getRed(),
                    run.getFont().getColor().getGreen(),
                    run.getFont().getColor().getBlue()
            });

            textInfo.put("style", style);
            textInfo.put("text", Collections.singletonList(run.getText()));

            content.add(textInfo);
        }

        paragraphInfo.put("content", content);
        return paragraphInfo;
    }

    /**
     * 提取表格内容
     *
     * @param table 源表格
     * @return 提取后的表格内容
     */
    private static JSONObject extractTable(Table table) {
        JSONObject tableInfo = new JSONObject();
        String title = findClosestHeading(table);
        tableInfo.put("title", title);

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
                    for (Run run : (Iterable<Run>) para.getChildNodes(NodeType.RUN, true)) {
                        JSONObject style = new JSONObject();
                        style.put("bold", run.getFont().getBold());
                        style.put("italic", run.getFont().getItalic());
                        style.put("color", new int[]{
                                run.getFont().getColor().getRed(),
                                run.getFont().getColor().getGreen(),
                                run.getFont().getColor().getBlue()
                        });

                        cellStyleArray.add(style);
                        cellTextArray.add(run.getText());
                    }
                }

                rowStyleData.add(cellStyleArray);
                rowTextData.add(cellTextArray);
            }

            styleArray.add(rowStyleData);
            tableArray.add(rowTextData);
        }

        tableContent.put("style", styleArray);
        tableContent.put("table", tableArray);
        contentArray.add(tableContent);
        tableInfo.put("content", contentArray);

        return tableInfo;
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
