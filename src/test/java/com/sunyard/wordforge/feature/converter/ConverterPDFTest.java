package com.sunyard.wordforge.feature.converter;

import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import org.junit.jupiter.api.Test;

/**
 * word转pdf
 *
 * @author Archer
 */
class ConverterPDFTest {

    @Test
    public void wordToPdfTest() {
        String sourcePath = FileResourceConstant.MULTIPLE_ELEMENT_DOCUMENT;
        ConverterPDF.wordToPdf(sourcePath);
    }
}
