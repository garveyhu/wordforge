package com.sunyard.wordforge.feature;

import com.aspose.words.*;
import java.io.File;

/**
 * Aspose文档表格拆分
 *
 * @author Archer
 */
public class SplitterTable {

    public static void main(String[] args) {
        // Specify the input Word document file path
        //        String inputFilePath = "D:\\home\\go\\resources\\代码测试文档\\spilt\\手机银行客户端（普通版）用户使用指南-2021年冬奥会纪念钞预约.docx";
        String inputFilePath =
            "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\本说明文档旨在对截图控件的浏览器控件进行说明.docx";

        // Specify the output directory for the split documents
        //        String outputDirectory = "D:\\home\\go\\resources\\代码测试文档\\spilt";
        String outputDirectory = "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\";

        // Create the output directory if it doesn't exist
        File outputDir = new File(outputDirectory);
        outputDir.mkdirs();

        // Split the Word document using Aspose.Words
        splitDocumentByHeadline(inputFilePath, outputDirectory);
    }

    /**
     * 按照段落对文档进行拆分
     * @param inputFilePath
     * @param outputDirectory
     */
    public static void splitWordDocumentByParagraph(String inputFilePath, String outputDirectory) {
        try {
            // Initialize the Aspose.Words document
            Document document = new Document(inputFilePath);

            // Get all tables in the document
            NodeCollection<Table> tables = document.getChildNodes(NodeType.TABLE, true);

            int tableCount = 1;
            for (Table table : tables) {
                // Get all paragraphs in the table
                NodeCollection<Paragraph> paragraphs = table.getChildNodes(NodeType.PARAGRAPH, true);

                int paragraphCount = 1;
                for (Paragraph paragraph : paragraphs) {
                    if (paragraph.getText().trim().isEmpty()) {
                        continue;
                    }

                    // Create a new document for each paragraph
                    Document newDocument = new Document();
                    NodeImporter importer = new NodeImporter(
                        document,
                        newDocument,
                        ImportFormatMode.KEEP_SOURCE_FORMATTING
                    );

                    Node importedNode = importer.importNode(paragraph, true);
                    newDocument.getFirstSection().getBody().appendChild(importedNode);

                    // Save the new document to a file
                    String outputFilePath =
                        outputDirectory +
                        File.separator +
                        "Table_" +
                        tableCount +
                        "_Paragraph_" +
                        paragraphCount +
                        ".docx";
                    newDocument.save(outputFilePath);

                    System.out.println(
                        "Table " + tableCount + ", Paragraph " + paragraphCount + " saved to: " + outputFilePath
                    );

                    paragraphCount++;
                }

                tableCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按照标题对文档进行拆分
     * @param inputFilePath
     * @param outputDirectory
     */
    public static void splitDocumentByHeadline(String inputFilePath, String outputDirectory) {
        /*try {
            //使用 Aspose.Words for Java 按标题拆分文档的 Java 代码
            Document doc = new Document(inputFilePath);
            LayoutCollector layoutCollector = new LayoutCollector(doc);

            NodeCollection<Table> tables = doc.getChildNodes(NodeType.TABLE, true);

            for (Table table : tables) {
                for (Paragraph paragraph : (Iterable<Paragraph>) table.getChildNodes(NodeType.PARAGRAPH, true)) {
                    if (paragraph.getText().trim().isEmpty()){
                        continue;
                    }
                    if (paragraph.getParagraphFormat().getStyle().getName().startsWith("Heading")) {
                        int pageIndex = layoutCollector.getStartPageIndex(paragraph);
                        int endIndex = layoutCollector.getEndPageIndex(paragraph);

                        Document headingDoc = new Document();
                        for (int i = pageIndex; i <= endIndex; i++) {
                            headingDoc.getFirstSection().getBody().appendChild(doc.getSections().get(i).deepClone(true));
                        }

                        headingDoc.save(outputDirectory + paragraph.getText().trim() + ".docx");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        /*try {
            //使用 Aspose.Words for Java 按标题拆分文档的 Java 代码
            Document doc = new Document(inputFilePath);
            LayoutCollector layoutCollector = new LayoutCollector(doc);

            for (Paragraph paragraph : (Iterable<Paragraph>) doc.getChildNodes(NodeType.PARAGRAPH, true)) {
                if (paragraph.getText().trim().isEmpty()){
                    continue;
                }
                if (paragraph.getParagraphFormat().getStyle().getName().startsWith("Heading")) {
                    int pageIndex = layoutCollector.getStartPageIndex(paragraph);
                    int endIndex = layoutCollector.getEndPageIndex(paragraph);

                    Document headingDoc = new Document();
                    for (int i = pageIndex; i <= endIndex; i++) {
                        SectionCollection sections = doc.getSections();
                        Section section = sections.get(i);
                        headingDoc.getFirstSection().getBody().appendChild(section.deepClone(true));
                    }

                    headingDoc.save(outputDirectory + paragraph.getText().trim() + ".docx");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        try {
            //使用 Aspose.Words for Java 按标题拆分文档的 Java 代码
            Document doc = new Document(inputFilePath);
            HtmlSaveOptions options = new HtmlSaveOptions();
            options.setDocumentSplitCriteria(DocumentSplitCriteria.HEADING_PARAGRAPH);
            doc.save(outputDirectory + "newDocument.docx", options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
