package com.sunyard.wordforge.feature.converter;

import com.aspose.words.*;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * word wordml互转
 *
 * @author Archer
 */
public class ConverterXML {

    /**
     * Word转WordML
     *
     * @param wordInputStream 源文件输入流
     * @return WordXML数据输出流
     */
    public static OutputStream convertWordToWordML(InputStream wordInputStream) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(wordInputStream);
        OutputStream wordMLOutputStream = new ByteArrayOutputStream();
        doc.save(wordMLOutputStream, SaveFormat.WORD_ML);

        return wordMLOutputStream;
    }

    /**
     * WordML转Word
     *
     * @param wordMLInputStream 源文件输入流
     * @return Word数据输出流
     */
    public static OutputStream convertWordMLToWord(InputStream wordMLInputStream) throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        Document doc = new Document(wordMLInputStream);
        OutputStream wordOutputStream = new ByteArrayOutputStream();
        doc.save(wordOutputStream, SaveFormat.DOCX);

        return wordOutputStream;
    }
}
