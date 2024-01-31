package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.complex.constant.MimeTypeConstant;
import com.sunyard.wordforge.feature.merger.MergeDoc;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 合并模块
 * Controller
 *
 * @author Archer
 */
@Api(tags = "合并模块")
@RestController
@RequestMapping("/merger")
public class MergerController {
    @Resource
    private HttpServletResponse response;

    @ApiOperation(value = "合并word")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(
                name = "files",
                value = "源文件集合",
                required = true,
                dataType = "__file",
                allowMultiple = true
            )
        }
    )
    @PostMapping("/merge")
    public void merge(@RequestParam("files") MultipartFile[] files) throws Exception {
        OutputStream outputStream = MergeDoc.merge(files);
        StreamUtil.outputStreamToResponse(outputStream, response, "merge.docx", MimeTypeConstant.APPLICATION_MSWORD);
    }
}
