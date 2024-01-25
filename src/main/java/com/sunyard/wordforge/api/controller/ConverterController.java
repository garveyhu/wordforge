package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.constant.MimeTypeConstant;
import com.sunyard.wordforge.feature.converter.ConverterPDF;
import com.sunyard.wordforge.feature.splitter.SplitterLabel;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true, dataType = "__file") })
    @PostMapping("/word2pdf")
    public void word2pdf(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = ConverterPDF.wordToPdf(inputStream);

        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "word2pdf.pdf");
        InputStream inputStreamLocal = StreamUtil.filePathToInputStream(FilePathConstant.OUTPUT + "word2pdf.pdf");
        OutputStream outputStreamLocal = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStreamLocal.read(buffer)) != -1) {
            outputStreamLocal.write(buffer, 0, length);
        }

        StreamUtil.outputStreamToResponse(
            outputStreamLocal,
            response,
            "word2pdf.pdf",
            MimeTypeConstant.APPLICATION_PDF
        );
    }

    //    @ApiOperation(value = "word转pdf")
    //    @PostMapping("/word2pdf2")
    //    public ResponseEntity<byte[]> word2pdf2(@RequestParam("file") MultipartFile file) throws Exception {
    //        InputStream inputStream = file.getInputStream();
    //        OutputStream outputStream = ConverterPDF.wordToPdf(inputStream);
    //
    //        StreamUtil.outputStreamToFile(outputStream, FilePathConstant.OUTPUT, "word2pdf.pdf");
    //
    //        byte[] body = baos.toByteArray();
    //
    //        HttpHeaders headers = new HttpHeaders();//设置响应头
    //        headers.add("Content-Disposition", "attachment;filename=test.pdf");
    //        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
    //        ResponseEntity<byte[]> resp = new ResponseEntity<byte[]>(body, headers, statusCode);
    //        return resp;
    //    }

    @ApiOperation(value = "word切分")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "file", value = "源文件", required = true, dataType = "__file"),
            @ApiImplicitParam(name = "separator", value = "分隔符", required = true, dataType = "String", example = "§")
        }
    )
    @PostMapping("/split")
    public void split(@RequestParam("file") MultipartFile file, @RequestParam("separator") String separator)
        throws IOException {
        InputStream inputStream = file.getInputStream();
        List<OutputStream> outputStreams = SplitterLabel.splitDocumentBySeparator(inputStream, separator);
        StreamUtil.outputStreamsToResponseAsZip(outputStreams, response, "split_documents.zip");
    }
}
