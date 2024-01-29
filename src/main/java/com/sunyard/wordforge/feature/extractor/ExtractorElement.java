package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.sunyard.wordforge.api.vo.ExtractionOptions;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * 提取元素
 *
 * @author Archer
 */
public class ExtractorElement {

    /**
     * 提取段落内容
     *
     * @param paragraph 源段落
     * @param options   提取选项
     * @return 提取后的段落内容
     */
    public static JSONObject extractParagraph(Paragraph paragraph, ExtractionOptions options) {
        // 提取整个段落的内容
        String paragraphText = paragraph.getText().trim();

        // 如果设置了过滤空内容并且段落内容为空，则返回null
        if (options.getFilterEmptyContent() && StringUtils.isBlank(paragraphText)) {
            return null;
        }

        // 如果不包含标题内容并且当前段落是标题，则返回空的内容
        if (!options.getIncludeHeadingInContent() && paragraph.getParagraphFormat().isHeading()) {
            return null;
        }

        JSONObject paragraphInfo = new JSONObject();
        if (options.getExtractTitle()) {
            String title = ExtractorCommon.findClosestHeading(paragraph);
            paragraphInfo.put("title", title);
        }

        if (options.getExtractAsWholeParagraph()) {
            // 提取整个段落的内容
            JSONObject content = new JSONObject();
            if (options.getExtractStyle()) {
                Run firstRun = (Run) paragraph.getChildNodes(NodeType.RUN, true).get(0);
                JSONObject style = ExtractorCommon.extractStyle(firstRun);
                content.put("style", style);
            }
            content.put("text", paragraphText);
            paragraphInfo.put("content", content);
        } else {
            // 分段提取内容
            JSONArray content = new JSONArray();
            for (Run run : (Iterable<Run>) paragraph.getChildNodes(NodeType.RUN, true)) {
                String runText = run.getText().trim();
                if (options.getFilterEmptyContent() && runText.isEmpty()) {
                    continue; // 跳过空内容
                }

                JSONObject textInfo = new JSONObject();
                if (options.getExtractStyle()) {
                    JSONObject style = ExtractorCommon.extractStyle(run);
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
     * @param table   源表格
     * @param options 提取选项
     * @return 提取后的表格内容
     */
    public static JSONObject extractTable(Table table, ExtractionOptions options) {
        options.setFilterEmptyContent(false);
        JSONObject tableInfo = new JSONObject();

        if (options.getExtractTitle()) {
            String title = ExtractorCommon.findClosestHeading(table);
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
                        if (options.getExtractStyle()) {
                            JSONObject style = ExtractorCommon.extractStyle(run);
                            cellStyleArray.add(style);
                        }
                        cellTextBuilder.append(run.getText());
                    }

                    cellTextArray.add(cellTextBuilder.toString().trim());
                    if (!options.getExtractAsWholeParagraph()) {
                        rowStyleData.add(cellStyleArray);
                    }
                }

                if (options.getFilterEmptyContent()) {
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

                if (options.getExtractAsWholeParagraph()) {
                    rowTextData.add(cellTextArray);
                } else {
                    rowStyleData.add(cellStyleArray);
                }
            }

            if (options.getExtractStyle() && !options.getExtractAsWholeParagraph()) {
                styleArray.add(rowStyleData);
            }
            tableArray.add(rowTextData);
        }

        if (options.getExtractStyle() && !options.getExtractAsWholeParagraph()) {
            tableContent.put("style", styleArray);
        }
        tableContent.put("table", tableArray);
        contentArray.add(tableContent);
        tableInfo.put("content", contentArray);

        return tableInfo;
    }
}
