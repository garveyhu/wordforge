package com.sunyard.wordforge.feature.splitter;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.OutputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 标记拆分
 *
 * @author Archer
 */
class SplitterLabelTest {

    @Test
    void splitDocumentBySeparatorTest() {
        String sourcePath = FileResourceConstant.SPLITTER_LABEL;
        List<OutputStream> outputStreams = SplitterLabel.splitDocumentBySeparator(
            StreamUtil.filePathToInputStream(sourcePath),
            "§"
        );
        StreamUtil.outputStreamsToFiles(outputStreams, FilePathConstant.OUTPUT, null);
    }
}
