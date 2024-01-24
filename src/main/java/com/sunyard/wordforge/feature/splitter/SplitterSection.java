package com.sunyard.wordforge.feature.splitter;

import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.util.ArrayList;

/**
 * 分节符拆分
 *
 * @author Archer
 */
public class SplitterSection {

    public static void main(String[] args) throws Exception {
        String sourcePath = "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\信用类PAD尽调内容-开次开发.docx";
        splitDocumentBySections(sourcePath);
    }

    /**
     * 按照分节符号拆分文档
     *
     * @param src 源文档路径
     */
    private static void splitDocumentBySections(String src) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(src);
        ArrayList<Document> splitDocuments = splitDocumentIntoSections(doc);
        saveSplitDocuments(splitDocuments);
    }

    /**
     * 按照分节符号拆分文档
     *
     * @param doc 源文档
     * @return 拆分后的文档集合
     */
    private static ArrayList<Document> splitDocumentIntoSections(Document doc) throws Exception {
        ArrayList<Document> sectionDocuments = new ArrayList<>();
        int sectionCount = doc.getSections().getCount();

        for (int i = 0; i < sectionCount; i++) {
            Section section = doc.getSections().get(i);
            Document newDocument = new Document();
            Section newSection = (Section) newDocument.importNode(section, true);
            newDocument.getSections().clear();
            newDocument.getSections().add(newSection);
            sectionDocuments.add(newDocument);
        }

        return sectionDocuments;
    }

    /**
     * 保存拆分后的文档
     *
     * @param documents 拆分后的文档集合
     */
    private static void saveSplitDocuments(ArrayList<Document> documents) throws Exception {
        int docNumber = 1;
        for (Document splitDoc : documents) {
            splitDoc.save("C:\\Users\\lenovo\\Downloads\\aspose\\doc\\output\\SectionDoc_" + docNumber + ".docx");
            docNumber++;
        }
    }
}
