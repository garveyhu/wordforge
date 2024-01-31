package com.sunyard.wordforge.feature.contents;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.AsposeWordUtil;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

/**
 * 目录操作
 *
 * @author Archer
 */
class ContentsOperationTest {

    @Test
    public void generateTableOfContentsTest() throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        String sourcePath = FileResourceConstant.MULTIPLE_ELEMENT_DOCUMENT;
        InputStream inputStream = StreamUtil.filePathToInputStream(sourcePath);
        OutputStream outputStream = ContentsOperation.generateTableOfContents(inputStream);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "tableOfContents.docx");
    }

    @Test
    public void removeTableOfContentsTest() throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        String sourcePath = FileResourceConstant.CONTENTS_DOC;
        InputStream inputStream = StreamUtil.filePathToInputStream(sourcePath);
        OutputStream outputStream = ContentsOperation.removeTableOfContents(inputStream);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "removeTableOfContents.docx");
    }
}