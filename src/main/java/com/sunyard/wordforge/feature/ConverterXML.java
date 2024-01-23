package com.sunyard.wordforge.feature;

import com.aspose.words.*;

/**
 * Aspose文档xml转换
 *
 * @author Archer
 */
public class ConverterXML {

    public static void main(String[] args) {
        // Specify the input WordXML file path
        String xmlFilePath =
            "D:\\home\\go\\resources\\代码测试文档\\手机银行客户端（普通版）用户使用指南-2021年冬奥会纪念钞预约(1).xml";

        // Specify the output DOCX file path
        String outputDocxPath = "D:\\home\\go\\resources\\代码测试文档\\spilt\\aspose\\转换结果\\XmlToDocx.docx";

        // Convert WordXML to DOCX using Aspose.Words
        convertWordXMLToDocx(xmlFilePath, outputDocxPath);
    }

    /**
     * 将WordXml转换为Word
     * @param xmlFilePath    wordXml文件路径
     * @param outputDocxPath 生成文件路径
     */
    public static void convertWordXMLToDocx(String xmlFilePath, String outputDocxPath) {
        try {
            // 初始化文档
            Document doc = new Document(xmlFilePath);

            // 创建DocumentBuilder
            DocumentBuilder builder = new DocumentBuilder();

            // 读取WordXML文件并将其内容插入到DocumentBuilder中
            builder.insertDocument(doc, ImportFormatMode.USE_DESTINATION_STYLES);

            // Save the Document to DOCX format
            doc.save(outputDocxPath);

            System.out.println("WordXML converted to DOCX: " + outputDocxPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
