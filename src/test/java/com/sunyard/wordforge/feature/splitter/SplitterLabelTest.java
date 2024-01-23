package com.sunyard.wordforge.feature.splitter;

import static org.junit.jupiter.api.Assertions.*;

import com.sunyard.wordforge.complex.constant.FileResourceConstant;
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
        SplitterLabel.splitDocumentBySeparator(sourcePath, "§");
    }
}