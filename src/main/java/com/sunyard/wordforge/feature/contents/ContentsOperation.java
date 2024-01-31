package com.sunyard.wordforge.feature.contents;

import com.aspose.words.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 目录操作内容操作
 *
 * @author Archer
 */
public class ContentsOperation {

    /**
     * 生成包含目录的文档
     *
     * @param inputStream 输入流，源文档
     * @return 包含目录的文档的输出流
     */
    public static OutputStream generateTableOfContents(InputStream inputStream) throws Exception {
        // 从输入流加载文档
        Document doc = new Document(inputStream);

        // 创建 DocumentBuilder，用于在文档中插入元素
        DocumentBuilder builder = new DocumentBuilder(doc);

        // 将光标移动到文档的开头
        builder.moveToDocumentStart();

        // 插入目录
        builder.insertTableOfContents("\\o \"1-3\" \\h \\z \\u");
        // 参数定义目录的格式和行为

        // 更新目录（可能需要两次更新才能正确显示页码）
        doc.updateFields();
        doc.updatePageLayout();

        // 创建一个 ByteArrayOutputStream 以将文档写入输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 将文档保存到输出流中
        doc.save(outputStream, SaveFormat.DOCX);

        // 返回包含文档的输出流
        return outputStream;
    }

    /**
     * 删除包含目录的文档中的目录
     *
     * @param inputStream 输入流，包含目录的文档
     * @return 不包含目录的文档的输出流
     */
    public static OutputStream removeTableOfContents(InputStream inputStream) throws Exception {
        Document doc = new Document(inputStream);

        // 查找所有TOC字段
        NodeCollection<FieldStart> fieldStarts = doc.getChildNodes(NodeType.FIELD_START, true);
        List<FieldStart> tocFieldStarts = new ArrayList<>();

        for (FieldStart fieldStart : fieldStarts) {
            if (fieldStart.getFieldType() == FieldType.FIELD_TOC) {
                tocFieldStarts.add(fieldStart);
            }
        }

        // 完全删除TOC字段
        for (FieldStart fieldStart : tocFieldStarts) {
            Field field = fieldStart.getField();
            field.remove();
        }

        // 更新文档以反映更改
        doc.updatePageLayout();
        doc.updateFields();

        // 保存更改到新的OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream, SaveFormat.DOCX);

        return outputStream;
    }
}
