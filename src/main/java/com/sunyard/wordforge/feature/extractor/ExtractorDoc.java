package com.sunyard.wordforge.feature.extractor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunyard.wordforge.api.vo.ExtractionOptions;
import com.sunyard.wordforge.complex.function.ThrowableFunction;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 * 解析提取
 *
 * @author Archer
 */
public class ExtractorDoc {

    /**
     * 提取文档内容
     *
     * @param file 源文档
     * @return 提取后的文档内容
     */
    public static JSONObject extract0(MultipartFile file, ExtractionOptions options) throws Exception {
        options.fillDefaultValues();
        return extractContent(file, inputStream -> ExtractorGranularity.granularity0(inputStream, options));
    }

    /**
     * 提取文档内容
     *
     * @param file 源文档
     * @return 提取后的文档内容
     */
    public static JSONObject extract1(MultipartFile file, ExtractionOptions options) throws Exception {
        options.fillDefaultValues();
        return extractContent(file, inputStream -> ExtractorGranularity.granularity1(inputStream, options));
    }

    /**
     * 提取文档内容的通用方法
     *
     * @param file          源文档
     * @param dataExtractor 数据提取函数
     * @return 提取后的文档内容
     */
    private static JSONObject extractContent(
        MultipartFile file,
        ThrowableFunction<InputStream, JSONArray> dataExtractor
    )
        throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            JSONArray data = dataExtractor.apply(inputStream);
            JSONObject result = new JSONObject();
            result.put("data", data);
            result.put("file_name", fileName);

            return result;
        }
    }
}
