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
     * 提取文档内容，保持内容的整体性
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
     *
     * @param inputStream 源文档输入流
     * @param options     提取选项
     * @return 提取后的文档内容
     */
    public static JSONArray granularity1(InputStream inputStream, ExtractionOptions options) throws Exception {
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
                JSONObject paragraphInfo = ExtractorElement.extractParagraph(para, options);
                if (paragraphInfo != null) {
                    data.add(paragraphInfo);
                }
            }

            // 提取所有表格
            NodeCollection tables = section.getBody().getChildNodes(NodeType.TABLE, false);
            for (Table table : (Iterable<Table>) tables) {
                JSONObject tableInfo = ExtractorElement.extractTable(table, options);
                data.add(tableInfo);
            }
        }

        inputStream.close();

        return data;
    }
}
