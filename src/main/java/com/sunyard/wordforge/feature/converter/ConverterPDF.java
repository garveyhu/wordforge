package com.sunyard.wordforge.feature.converter;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.sunyard.wordforge.util.AsposeWordUtil;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * word转pdf
 *
 * @author Archer
 */
@Slf4j
public class ConverterPDF {

    /**
     * Word转PDF
     *
     * @param inputStream 源文件输入流
     * @return PDF数据输出流
     */
    public static OutputStream wordToPdf(InputStream inputStream) throws Exception {
        log.info("开始转换文件");
        long start = System.currentTimeMillis();

        OutputStream outputStream = new ByteArrayOutputStream();
        AsposeWordUtil.getInstance().registerLicense();

        Document doc = new Document(inputStream);
        doc.save(outputStream, SaveFormat.PDF);

        log.info("转换耗时：" + (System.currentTimeMillis() - start) + "ms");

        inputStream.close();

        return outputStream;
    }
}
