package com.sunyard.wordforge.feature.merger;

import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * 合并文档
 *
 * @author Archer
 */
public class MergeDoc {

    /**
     * 合并文档
     *
     * @param files 源文件集合
     * @return 合并后的文件输出流
     */
    public static OutputStream merge(MultipartFile[] files) throws Exception {
        List<InputStream> inputStreams = new ArrayList<>();
        for (MultipartFile file : files) {
            inputStreams.add(file.getInputStream());
        }
        return mergeDocs(inputStreams);
    }

    /**
     * 合并文档
     *
     * @param inputStreams 源文件输入流集合
     * @return 合并后的文件输出流
     */
    public static OutputStream mergeDocs(List<InputStream> inputStreams) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        List<Document> documentList = new ArrayList<>();
        for (InputStream inputStream : inputStreams) {
            Document doc = new Document(inputStream);
            documentList.add(doc);
            inputStream.close();
        }
        Document document = MergeDoc.AddDocs2Doc(documentList);
        MergeDoc.addContents(document);
        OutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream, SaveFormat.DOCX);
        return outputStream;
    }

    /**
     * 合并文档
     *
     * @param documentList 源文件集合
     * @return 合并后的文件
     */
    public static Document AddDocs2Doc(List<Document> documentList) {
        if (!documentList.isEmpty()) {
            // 取第一个文档作为主文档，将其与的文档合并到它这里
            Document docAll = documentList.get(0);
            for (int i = 1; i < documentList.size(); i++) {
                Document doc = documentList.get(i);
                // CONTINUOUS:分节;length:分页
                doc.getFirstSection().getPageSetup().setSectionStart(SectionStart.CONTINUOUS);
                docAll.appendDocument(doc, ImportFormatMode.KEEP_DIFFERENT_STYLES);
            }
            return docAll;
        }
        return null;
    }

    /**
     * 添加目录
     *
     * @param document 文档
     */
    public static void addContents(Document document) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(document);
        document.getFirstSection().getBody().prependChild(new Paragraph(document));
        builder.moveToDocumentStart();

        // 设置目录的格式
        // “目录”两个字居中显示、加粗、搜宋体
        builder.getCurrentParagraph().getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
        builder.setBold(true);
        builder.getFont().setName("宋体");
        builder.writeln("目录");
        // 清清除所有样式设置
        builder.getParagraphFormat().clearFormatting();
        // 目录居左
        builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
        // 插入目录，这是固定的
        builder.insertTableOfContents("\\o \"1-3\" \\h \\z \\u");
        builder.insertBreak(BreakType.PAGE_BREAK);
        document.updateFields();
        document.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_3).getParagraphFormat().setLineSpacing(18); //改变目录行距
    }
}
