package com.sunyard.wordforge.feature.converter;

import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * word转xml
 *
 * @author Archer
 */
public class ConverterXML {

    /**
     * Word转WordXML
     *
     * @param wordInputStream 源文件输入流
     * @return WordXML数据输出流
     */
    public static OutputStream convertWordToWordXML(InputStream wordInputStream) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(wordInputStream);
        OutputStream wordXMLOutputStream = new ByteArrayOutputStream();
        doc.save(wordXMLOutputStream, SaveFormat.WORD_ML);

        return wordXMLOutputStream;
    }

    /**
     * WordXML转Word
     *
     * @param wordXMLInputStream 源文件输入流
     * @return Word数据输出流
     */
    public static OutputStream convertWordXMLToWord(InputStream wordXMLInputStream) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(wordXMLInputStream);
        OutputStream wordOutputStream = new ByteArrayOutputStream();
        doc.save(wordOutputStream, SaveFormat.DOCX);

        return wordOutputStream;
    }
}
