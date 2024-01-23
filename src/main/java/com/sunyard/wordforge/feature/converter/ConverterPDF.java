package com.sunyard.wordforge.feature.converter;

import com.aspose.words.Document;
import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.util.AsposeWordUtil;
import com.sunyard.wordforge.util.ComplexUtil;
import java.io.File;
import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * word转pdf
 *
 * @author Archer
 */
@Slf4j
public class ConverterPDF {

    /**
     * word转pdf
     *
     * @param src 源文件路径
     */
    public static void wordToPdf(String src) {
        log.info("开始转换文件：" + src);
        String outputFilePath = FilePathConstant.OUTPUT + ComplexUtil.getFileNameWithoutExtension(src) + ".pdf";
        long start = System.currentTimeMillis();
        try {
            AsposeWordUtil.getInstance().registerLicense();
            Document doc = new Document(src);
            doc.save(outputFilePath, com.aspose.words.SaveFormat.PDF);
        } catch (Exception e) {
            log.error("wordToPdf error", e);
        }
        log.info("转换耗时：" + (System.currentTimeMillis() - start) + "ms");
    }
}
