package com.sunyard.wordforge.feature.merger;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.FileResourceConstant;
import com.sunyard.wordforge.util.AsposeWordUtil;
import com.sunyard.wordforge.util.StreamUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * 合并文档
 *
 * @author Archer
 */
class MergeDocTest {

    @Test
    void mergeDocsTest() throws Exception {
        AsposeWordUtil.getInstance().registerLicense();
        List<String> filePaths = Arrays.asList(
            FileResourceConstant.ANIMATION_MOVIE,
            FileResourceConstant.SPLITTER_LABEL
        );
        List<InputStream> inputStreams = StreamUtil.filePathsToInputStreams(filePaths);
        OutputStream outputStream = MergeDoc.mergeDocs(inputStreams);
        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "merge.docx");
    }
}
