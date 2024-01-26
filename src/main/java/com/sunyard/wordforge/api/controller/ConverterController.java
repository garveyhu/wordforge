package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.complex.constant.MimeTypeConstant;
import com.sunyard.wordforge.feature.converter.ConverterPDF;
import com.sunyard.wordforge.feature.converter.ConverterXML;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.InputStream;
import java.io.OutputStream;
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/word2pdf")
    public void word2pdf(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = ConverterPDF.wordToPdf(inputStream);

        StreamUtil.outputStreamToResponse(
            outputStream,
            response,
            "word2pdf.pdf",
            MimeTypeConstant.APPLICATION_PDF
        );
    }

    @ApiOperation(value = "word转wordml")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/word2wordml")
    public void word2wordml(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = ConverterXML.convertWordToWordML(inputStream);

        StreamUtil.outputStreamToResponse(
            outputStream,
            response,
            "word2wordml.xml",
            MimeTypeConstant.APPLICATION_XML
        );
    }

    @ApiOperation(value = "wordml转word")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/wordml2word")
    public void wordml2word(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = ConverterXML.convertWordMLToWord(inputStream);

        StreamUtil.outputStreamToResponse(
            outputStream,
            response,
            "wordml2word.docx",
            MimeTypeConstant.APPLICATION_MSWORD
        );
    }
}
