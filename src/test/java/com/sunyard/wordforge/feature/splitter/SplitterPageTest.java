package com.sunyard.wordforge.feature.splitter;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.AsposeWordUtil;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.OutputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 页码拆分
 *
 * @author Archer
 */
class SplitterPageTest {

    @Test
    public void splitDocumentByPageTest() throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        String sourcePath = FileResourceConstant.ANIMATION_MOVIE;
        List<OutputStream> outputStreams = SplitterPage.splitDocumentByPage(
            StreamUtil.filePathToInputStream(sourcePath)
        );
        StreamUtil.outputStreamsToFiles(outputStreams, FilePathConstant.OUTPUT, null);
    }

    @Test
    public void splitDocumentByOddEvenPagesTest() throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        String sourcePath = FileResourceConstant.ANIMATION_MOVIE;
        List<OutputStream> outputStreams = SplitterPage.splitDocumentByOddEvenPages(
            StreamUtil.filePathToInputStream(sourcePath)
        );
        StreamUtil.outputStreamsToFiles(outputStreams, FilePathConstant.OUTPUT, null);
    }
}
