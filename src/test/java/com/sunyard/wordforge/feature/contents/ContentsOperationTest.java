package com.sunyard.wordforge.feature.contents;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

class ContentsOperationTest {

    @Test
    public void generateTableOfContentsTest() throws Exception {
        String sourcePath = FileResourceConstant.MULTIPLE_ELEMENT_DOCUMENT;
        InputStream inputStream = StreamUtil.filePathToInputStream(sourcePath);
        OutputStream outputStream = ContentsOperation.generateTableOfContents(inputStream);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "tableOfContents.docx");
    }
}