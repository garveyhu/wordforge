package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.sunyard.wordforge.api.vo.ExtractionOptions;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.InputStream;

/**
 * 提取粒度
 *
 * @author Archer
 */
public class ExtractorGranularity {

    /**
     * 提取文档内容
     * 0级粒度
     * 以段落和表格为单位提取
     *
     * @param inputStream 源文档输入流
     * @param options     提取选项
     * @return 提取后的文档内容
     */
    public static JSONArray granularity0(InputStream inputStream, ExtractionOptions options) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();

        for (Section section : doc.getSections()) {
            NodeCollection nodes = section.getBody().getChildNodes(NodeType.ANY, false);
            for (Node node : (Iterable<Node>) nodes) {
                if (node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph para = (Paragraph) node;
                    if (!para.isInCell()) { // 如果段落不属于表格
                        JSONObject paragraphInfo = ExtractorElement.extractParagraph(para, options);
                        if (paragraphInfo != null) {
                            data.add(paragraphInfo);
                        }
                    }
                } else if (node.getNodeType() == NodeType.TABLE) {
                    Table table = (Table) node;
                    JSONObject tableInfo = ExtractorElement.extractTable(table, options);
                    data.add(tableInfo);
                }
            }
        }

        inputStream.close();
        return data;
    }

    /**
     * 提取文档内容
     * 1级粒度
     * 以标题为单位提取
     *
     * @param inputStream 源文档输入流
     * @param options     提取选项
     * @return 提取后的文档内容
     */
    public static JSONArray granularity1(InputStream inputStream, ExtractionOptions options) throws Exception {
        options.setExtractAsWholeParagraph(true);
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();
        JSONObject currentSection = new JSONObject();
        JSONArray currentContent = new JSONArray();
        String currentTitle = "";

        // 定义提取逻辑
        for (Section section : doc.getSections()) {
            NodeCollection nodes = section.getBody().getChildNodes(NodeType.ANY, false);
            for (Node node : (Iterable<Node>) nodes) {
                if (node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph para = (Paragraph) node;
                    String title = ExtractorCommon.findClosestHeading(para);
                    if (!title.isEmpty() && !title.equals(currentTitle)) {
                        if (!currentContent.isEmpty()) {
                            currentSection.put("title", currentTitle);
                            currentSection.put("content", currentContent);
                            data.add(currentSection);
                            currentSection = new JSONObject();
                            currentContent = new JSONArray();
                        }
                        currentTitle = title;
                    }

                    if (!para.isInCell()) { // 如果段落不属于表格
                        JSONObject paragraphInfo = ExtractorElement.extractParagraph(para, options);
                        if (paragraphInfo != null) {
                            currentContent.add(paragraphInfo.getJSONObject("content"));
                        }
                    }
                } else if (node.getNodeType() == NodeType.TABLE) {
                    Table table = (Table) node;
                    JSONObject tableInfo = ExtractorElement.extractTable(table, options);
                    currentContent.add(tableInfo.getJSONArray("content").get(0));
                }
            }
        }

        // 添加最后一节
        if (!currentContent.isEmpty()) {
            currentSection.put("title", currentTitle);
            currentSection.put("content", currentContent);
            data.add(currentSection);
        }

        inputStream.close();
        return data;
    }

    /**
     * 提取文档内容
     * 2级粒度
     * 按照分隔符为单位提取
     *
     * @param inputStream 源文档输入流
     * @param options     提取选项
     * @param delimiter   内容分隔符
     * @return 提取后的文档内容
     */
    public static JSONArray granularity2(InputStream inputStream, ExtractionOptions options, String delimiter)
        throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();
        JSONArray currentContent = new JSONArray();
        boolean newSectionStarted = true; // 初始化为true以包含第一个分隔符之前的内容

        for (Section section : doc.getSections()) {
            NodeCollection nodes = section.getBody().getChildNodes(NodeType.ANY, true);
            for (Node node : (Iterable<Node>) nodes) {
                if (node.getNodeType() == NodeType.PARAGRAPH) {
                    Paragraph para = (Paragraph) node;
                    // 检查段落文本是否包含分隔符
                    if (para.getText().contains(delimiter)) {
                        if (!currentContent.isEmpty()) {
                            // 结束当前部分并开始新的部分
                            JSONObject sections = new JSONObject();
                            sections.put("content", currentContent);
                            data.add(sections);
                            currentContent = new JSONArray();
                        }
                        newSectionStarted = false; // 遇到分隔符后，设置为false
                        continue; // 跳过包含分隔符的段落
                    }
                    if (newSectionStarted || !para.getText().trim().isEmpty()) {
                        // 属于当前分隔符组的内容或第一个分隔符之前的内容
                        JSONObject paragraphInfo = ExtractorElement.extractParagraph(para, options);
                        if (paragraphInfo != null) {
                            currentContent.add(paragraphInfo.getJSONObject("content"));
                        }
                    }
                } else if (node.getNodeType() == NodeType.TABLE) {
                    Table table = (Table) node;
                    JSONObject tableInfo = ExtractorElement.extractTable(table, options);
                    currentContent.add(tableInfo.getJSONArray("content").get(0));
                }
            }
            newSectionStarted = true; // 每次遍历新的section时重置，以处理下一个分隔符之前的内容
        }

        // 添加最后一个分隔符组的内容（如果存在）
        if (!currentContent.isEmpty()) {
            JSONObject section = new JSONObject();
            section.put("content", currentContent);
            data.add(section);
        }

        inputStream.close();
        return data;
    }
}
