package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.feature.splitter.SplitterLabel;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 拆分模块
 * Controller
 *
 * @author Archer
 */
@Api(tags = "拆分模块")
@RestController
@RequestMapping("/splitter")
public class SplitterController {
    @Resource
    private HttpServletResponse response;

    @ApiOperation(value = "分隔符切分")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "file", value = "源文件", required = true),
            @ApiImplicitParam(name = "separator", value = "分隔符", required = true, example = "§")
        }
    )
    @PostMapping("/separator")
    public void split(@RequestParam("file") MultipartFile file, @RequestParam("separator") String separator)
        throws IOException {
        InputStream inputStream = file.getInputStream();
        List<OutputStream> outputStreams = SplitterLabel.splitDocumentBySeparator(inputStream, separator);
        StreamUtil.outputStreamsToResponseAsZip(outputStreams, response, "split_documents.zip");
    }
}
