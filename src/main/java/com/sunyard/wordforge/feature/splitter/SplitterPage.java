package com.sunyard.wordforge.feature.splitter;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 页码拆分
 *
 * @author Archer
 */
public class SplitterPage {

    /**
     * 按页拆分文档
     *
     * @param inputStream 输入流，源文档
     * @return 拆分后的文档的输出流列表
     */
    public static List<OutputStream> splitDocumentByPage(InputStream inputStream) throws Exception {
        Document document = new Document(inputStream);
        int pageCount = document.getPageCount();

        List<OutputStream> outputStreams = new ArrayList<>();

        for (int page = 1; page <= pageCount; page++) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document pageDocument = document.extractPages(page - 1, 1);
            pageDocument.save(outputStream, SaveFormat.DOCX);
            outputStreams.add(outputStream);
        }

        return outputStreams;
    }

    /**
     * 按奇偶页拆分文档
     *
     * @param inputStream 输入流，源文档
     * @return 包含两个文档的输出流列表，第一个为奇数页文档，第二个为偶数页文档
     */
    public static List<OutputStream> splitDocumentByOddEvenPages(InputStream inputStream) throws Exception {
        Document document = new Document(inputStream);
        int pageCount = document.getPageCount();

        Document oddPagesDocument = new Document();
        oddPagesDocument.removeAllChildren();
        Document evenPagesDocument = new Document();
        evenPagesDocument.removeAllChildren();

        for (int page = 1; page <= pageCount; page++) {
            Document pageDocument = document.extractPages(page - 1, 1);
            if (page % 2 == 1) {
                oddPagesDocument.appendDocument(pageDocument, com.aspose.words.ImportFormatMode.KEEP_SOURCE_FORMATTING);
            } else {
                evenPagesDocument.appendDocument(
                    pageDocument,
                    com.aspose.words.ImportFormatMode.KEEP_SOURCE_FORMATTING
                );
            }
        }

        List<OutputStream> outputStreams = new ArrayList<>();
        ByteArrayOutputStream oddOutputStream = new ByteArrayOutputStream();
        oddPagesDocument.save(oddOutputStream, SaveFormat.DOCX);
        outputStreams.add(oddOutputStream);

        ByteArrayOutputStream evenOutputStream = new ByteArrayOutputStream();
        evenPagesDocument.save(evenOutputStream, SaveFormat.DOCX);
        outputStreams.add(evenOutputStream);

        return outputStreams;
    }
}
