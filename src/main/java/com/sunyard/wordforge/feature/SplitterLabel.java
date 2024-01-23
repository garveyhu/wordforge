package com.sunyard.wordforge.feature;

import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspose文档标记拆分
 *
 * @author Archer
 */
@Slf4j
public class SplitterLabel {

    public static void main(String[] args) throws Exception {
        String sourcePath = "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\标记拆分.docx";
        splitDocument(sourcePath);
    }

    /**
     * 按照指定的分隔符拆分文档
     *
     * @param src 源文档路径
     */
    private static void splitDocument(String src) {
        try {
            AsposeWordUtil.getInstance().registerLicense();
            Document doc = new Document(src);
            ArrayList<Document> splitDocuments = splitDocumentBySeparator_0(doc, "§");
            saveSplitDocuments(splitDocuments);
        } catch (Exception e) {
            log.error("splitDocument error", e);
        }
    }

    /**
     * 按照指定的分隔符拆分文档
     * 标记段落不保留
     *
     * @param doc       源文档
     * @param separator 分隔符
     * @return 拆分后的文档集合
     */
    private static ArrayList<Document> splitDocumentBySeparator_0(Document doc, String separator) {
        ArrayList<Document> splitDocs = new ArrayList<>();
        DocumentBuilder builder = new DocumentBuilder(doc);
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
    private static ArrayList<Document> splitDocumentBySeparator_1(Document doc, String separator) {
        ArrayList<Document> splitDocs = new ArrayList<>();
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
    private static void saveSplitDocuments(ArrayList<Document> documents) throws Exception {
        int docNumber = 1;
        String targetPath = "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\output\\标记拆分_";
        for (Document splitDoc : documents) {
            splitDoc.save(targetPath + docNumber + ".docx");
            docNumber++;
        }
    }
}
