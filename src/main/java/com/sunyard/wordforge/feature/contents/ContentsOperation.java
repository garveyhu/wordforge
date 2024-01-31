package com.sunyard.wordforge.feature.contents;

import com.aspose.words.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
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

        List<FieldStart> tocFieldStarts = new ArrayList<>();
        List<Node> nodesToRemove = new ArrayList<>();

        // 找到所有 TOC 字段的开始部分
        for (FieldStart start : (Iterable<FieldStart>) doc.getChildNodes(NodeType.FIELD_START, true)) {
            if (start.getFieldType() == FieldType.FIELD_TOC) {
                tocFieldStarts.add(start);
            }
        }

        // 遍历每个 TOC 字段，收集要删除的节点
        for (FieldStart start : tocFieldStarts) {
            Node currentNode = start;
            nodesToRemove.add(currentNode);
            while (currentNode != null && currentNode.getNodeType() != NodeType.FIELD_END) {
                currentNode = currentNode.nextPreOrder(doc);

                // 对于每个 TOC 字段，我们在遇到 FieldEnd 之前添加所有节点
                if (currentNode != null && currentNode.getNodeType() == NodeType.FIELD_END) {
                    FieldEnd end = (FieldEnd) currentNode;
                    if (end.getFieldType() == FieldType.FIELD_TOC) {
                        nodesToRemove.add(currentNode);
                        break; // 结束当前 TOC 字段的处理
                    }
                }

                nodesToRemove.add(currentNode);
            }
        }

        // 删除收集到的所有节点
        for (Node node : nodesToRemove) {
            node.remove();
        }

        // 更新目录后的文档布局
        doc.updatePageLayout();

        // 将修改后的文档保存到输出流并返回
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream, SaveFormat.DOCX);

        return outputStream;
    }
}
