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
        JSONArray data = extract(inputStream);
        JSONObject result = new JSONObject();
        result.put("data", data);
        result.put("file_name", fileName);

        return result;
    }

    /**
     * 提取文档内容
     *
     * @param inputStream 源文档输入流
     * @return 提取后的文档内容
     */
    private static JSONArray extract(InputStream inputStream) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        JSONArray data = new JSONArray();

        // 遍历文档的所有段落和表格
        for (Section section : doc.getSections()) {
            for (Paragraph para : (Iterable<Paragraph>) section.getBody().getChildNodes(NodeType.PARAGRAPH, true)) {
                // 提取段落内容和标题
                JSONObject paragraphInfo = extractParagraph(para);
                if (paragraphInfo != null) {
                    data.add(paragraphInfo);
                }
            }

            for (Table table : (Iterable<Table>) section.getBody().getChildNodes(NodeType.TABLE, true)) {
                // 提取表格内容
                JSONObject tableInfo = extractTable(table);
                if (tableInfo != null) {
                    data.add(tableInfo);
                }
            }
        }

        return data;
    }

    /**
     * 提取段落内容
     *
     * @param paragraph 源段落
     * @return 提取后的段落内容
     */
    private static JSONObject extractParagraph(Paragraph paragraph) {
        if (paragraph.getChildNodes(NodeType.RUN, true).getCount() == 0) {
            return null; // Skip empty paragraphs
        }

        JSONObject paragraphInfo = new JSONObject();
        paragraphInfo.put("title", ""); // You might want to extract title from paragraph if applicable
        JSONArray content = new JSONArray();

        for (Run run : (Iterable<Run>) paragraph.getChildNodes(NodeType.RUN, true)) {
            JSONObject textInfo = new JSONObject();
            JSONObject style = new JSONObject();
            style.put("bold", run.getFont().getBold());
            style.put("italic", run.getFont().getItalic());
            style.put("color", new int[]{run.getFont().getColor().getRed(), run.getFont().getColor().getGreen(), run.getFont().getColor().getBlue()});

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
        tableInfo.put("title", ""); // You might want to extract title for table if applicable
        JSONArray content = new JSONArray();

        // Extract table content
        for (Row row : table.getRows()) {
            JSONArray rowData = new JSONArray();
            for (Cell cell : row.getCells()) {
                rowData.add(cell.getText().trim());
            }
            content.add(rowData);
        }

        tableInfo.put("table", content);
        return tableInfo;
    }
}
