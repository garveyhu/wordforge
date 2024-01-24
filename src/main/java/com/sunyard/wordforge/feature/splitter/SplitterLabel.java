package com.sunyard.wordforge.feature.splitter;

import com.aspose.words.*;
import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 标记拆分
 *
 * @author Archer
 */
@Slf4j
public class SplitterLabel {

    /**
     * 按照指定的分隔符拆分文档
     *
     * @param inputStream 源文档输入流
     * @param separator 分隔符
     * @return 拆分后的文档输出流集合
     */
    @SneakyThrows
    public static List<OutputStream> splitDocumentBySeparator(InputStream inputStream, String separator) {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(inputStream);
        List<Document> splitDocuments = separator_0(doc, separator);

        List<OutputStream> outputStreams = new ArrayList<>();
        for (Document splitDoc : splitDocuments) {
            OutputStream outputStream = new ByteArrayOutputStream();
            splitDoc.save(outputStream, SaveFormat.DOCX);
            outputStreams.add(outputStream);
        }

        return outputStreams;
    }

    /**
     * 按照指定的分隔符拆分文档
     * 标记段落不保留
     *
     * @param doc       源文档
     * @param separator 分隔符
     * @return 拆分后的文档集合
     */
    private static List<Document> separator_0(Document doc, String separator) {
        List<Document> splitDocs = new ArrayList<>();
        NodeCollection paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);

        Document currentDoc = (Document) doc.deepClone(false);
        DocumentBuilder currentBuilder = new DocumentBuilder(currentDoc);

        for (Paragraph para : (Iterable<Paragraph>) paragraphs) {
            if (para.getText().contains(separator)) {
                splitDocs.add(currentDoc);
                currentDoc = (Document) doc.deepClone(false);
                currentBuilder = new DocumentBuilder(currentDoc);
            } else {
                Node importedNode = currentDoc.importNode(para, true);
                currentBuilder.getCurrentSection().getBody().appendChild(importedNode);
            }
        }
        splitDocs.add(currentDoc);

        return splitDocs;
    }

    /**
     * 按照指定的分隔符拆分文档
     * 标记段落保留
     *
     * @param doc       源文档
     * @param separator 分隔符
     * @return 拆分后的文档集合
     */
    private static List<Document> separator_1(Document doc, String separator) {
        List<Document> splitDocs = new ArrayList<>();
        Document currentDoc = (Document) doc.deepClone(false);
        DocumentBuilder currentBuilder = new DocumentBuilder(currentDoc);

        NodeCollection paragraphs = doc.getChildNodes(NodeType.PARAGRAPH, true);
        for (Paragraph para : (Iterable<Paragraph>) paragraphs) {
            Node importedNode = currentDoc.importNode(para, true);
            currentBuilder.getCurrentSection().getBody().appendChild(importedNode);

            if (para.getText().contains(separator)) {
                splitDocs.add(currentDoc);
                currentDoc = (Document) doc.deepClone(false);
                currentBuilder = new DocumentBuilder(currentDoc);
            }
        }

        // 添加最后一个拆分的文档
        if (currentDoc.getFirstSection().getBody().getFirstChild() != null) {
            splitDocs.add(currentDoc);
        }

        return splitDocs;
    }

    /**
     * 保存拆分后的文档
     *
     * @param documents 拆分后的文档集合
     */
    private static void saveSplitDocuments(List<Document> documents) throws Exception {
        int docNumber = 1;
        String targetPath = FilePathConstant.OUTPUT + "Splitter_";
        for (Document splitDoc : documents) {
            splitDoc.save(targetPath + docNumber + ".docx");
            docNumber++;
        }
    }
}
