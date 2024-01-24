package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.complex.constant.MimeTypeConstant;
import com.sunyard.wordforge.feature.converter.ConverterPDF;
import com.sunyard.wordforge.feature.splitter.SplitterLabel;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 转换模块
 * Controller
 *
 * @author Archer
 */
@Api(tags = "转换模块")
@RestController
@RequestMapping("/converter")
public class ConverterController {

    @Resource
    private HttpServletResponse response;

    @ApiOperation(value = "word转pdf")
    @PostMapping("/word2pdf")
    public void word2pdf(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = ConverterPDF.wordToPdf(inputStream);
        StreamUtil.outputStreamToResponse(outputStream, response, "word2pdf.pdf", MimeTypeConstant.APPLICATION_PDF);
    }

    @ApiOperation(value = "word切分")
    @PostMapping("/split")
    public void split(@RequestParam("file") MultipartFile file, @RequestParam("separator") String separator) throws IOException {
        InputStream inputStream = file.getInputStream();
        List<OutputStream> outputStreams = SplitterLabel.splitDocumentBySeparator(inputStream, separator);
        StreamUtil.outputStreamsToResponseAsZip(outputStreams, response, "split_documents.zip");
    }
}
