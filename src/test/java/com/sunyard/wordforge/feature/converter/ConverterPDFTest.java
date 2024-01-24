package com.sunyard.wordforge.feature.converter;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

/**
 * word转pdf
 *
 * @author Archer
 */
class ConverterPDFTest {

    @Test
    public void wordToPdfTest() throws Exception {
        String sourcePath = FileResourceConstant.MULTIPLE_ELEMENT_DOCUMENT;
        OutputStream outputStream = ConverterPDF.wordToPdf(StreamUtil.filePathToInputStream(sourcePath));
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "word2pdf.pdf");
    }
}
