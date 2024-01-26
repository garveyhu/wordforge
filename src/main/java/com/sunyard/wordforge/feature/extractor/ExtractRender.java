package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.*;
import com.aspose.words.Font;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author Archer
 */
public class ExtractRender {

    public static OutputStream renderJsonToWord(JSONObject jsonObject) throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        JSONArray dataArray = jsonObject.getJSONObject("data").getJSONArray("data");
        for (Object dataObj : dataArray) {
            JSONObject data = (JSONObject) dataObj;
            String title = data.getString("title");
            JSONArray contentArray = data.getJSONArray("content");

            // 处理标题
            if (!title.isEmpty()) {
                builder.getParagraphFormat().setStyleIdentifier(StyleIdentifier.HEADING_1);
                builder.writeln(title);
            }

            // 处理内容
            for (Object contentObj : contentArray) {
                JSONObject content = (JSONObject) contentObj;

                // 检查样式是JSONObject还是JSONArray
                if (content.get("style") instanceof JSONObject) {
                    // 单个样式
                    processSingleStyleContent(builder, content);
                } else if (content.get("style") instanceof JSONArray) {
                    // 样式数组
                    processMultipleStyleContent(builder, content);
                }

                builder.writeln(); // 新的一段
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream, SaveFormat.DOCX);

        return outputStream;
    }

    private static void processSingleStyleContent(DocumentBuilder builder, JSONObject content) {
        JSONObject style = content.getJSONObject("style");
        JSONArray textArray = content.getJSONArray("text");

        setFontStyle(builder.getFont(), style);

        // 文本输出
        for (Object textObj : textArray) {
            String text = (String) textObj;
            builder.write(text);
        }
    }

    private static void processMultipleStyleContent(DocumentBuilder builder, JSONObject content) {
        JSONArray styleArray = content.getJSONArray("style");
        JSONArray textArray = content.getJSONArray("text");

        for (int i = 0; i < styleArray.size(); i++) {
            JSONObject style = styleArray.getJSONObject(i);
            String text = textArray.getString(i);

            setFontStyle(builder.getFont(), style);
            builder.write(text);
        }
    }

    private static void setFontStyle(Font font, JSONObject style) {
        font.setColor(
            new Color(
                style.getJSONArray("color").getIntValue(0),
                style.getJSONArray("color").getIntValue(1),
                style.getJSONArray("color").getIntValue(2)
            )
        );
        font.setBold(style.getBooleanValue("bold"));
        font.setItalic(style.getBooleanValue("italic"));
    }
}
