package com.sunyard.wordforge.feature;

import com.aspose.words.Document;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.File;
import java.io.FileOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * Aspose文档pdf转换
 *
 * @author Archer
 */
@Slf4j
public class ConverterPDF {

    public static void main(String[] args) {
        String sourcePath = "C:\\Users\\lenovo\\Downloads\\aspose\\doc\\多元素文档.docx";
        word(sourcePath);
    }

    private static void word(String src) {
        log.info("开始转换文件：" + src);
        File file = new File(src + ".pdf");
        try (FileOutputStream os = new FileOutputStream(file)) {
            AsposeWordUtil.getInstance().registerLicense();
            long start = System.currentTimeMillis();
            Document doc = new Document(src);
            doc.save(os, com.aspose.words.SaveFormat.PDF);
            log.info("转换耗时：" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            log.error("word error", e);
        }
    }
}
