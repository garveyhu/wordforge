package com.sunyard.wordforge.feature.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

/**
 * word wordml互转
 *
 * @author Archer
 */
class ConverterXMLTest {

    @Test
    public void convertWordToWordXMLTest() throws Exception {
        String sourcePath = FileResourceConstant.ANIMATION_MOVIE;
        InputStream inputStream = StreamUtil.filePathToInputStream(sourcePath);
        OutputStream outputStream = ConverterXML.convertWordToWordXML(inputStream);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "word2xml.xml");

    }

    @Test
    public void convertWordXMLToWordTest() throws Exception {
        String sourcePath = FileResourceConstant.ANIMATION_MOVIE_XML;
        InputStream inputStream = StreamUtil.filePathToInputStream(sourcePath);
        OutputStream outputStream = ConverterXML.convertWordXMLToWord(inputStream);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "xml2word.docx");
    }
}