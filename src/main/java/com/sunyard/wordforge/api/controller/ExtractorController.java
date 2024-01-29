package com.sunyard.wordforge.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunyard.wordforge.api.vo.ExtractionOptions;
import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.vo.ResultVO;
import com.sunyard.wordforge.feature.extractor.ExtractorDoc;
import com.sunyard.wordforge.feature.extractor.ExtractorRender;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.OutputStream;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 提取模块
 * Controller
 *
 * @author Archer
 */
@Api(tags = "提取模块")
@RestController
@RequestMapping("/extractor")
public class ExtractorController {

    @ApiOperation(value = "提取文档内容v0")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "file", value = "源文件", dataType = "__file", required = true),
            @ApiImplicitParam(
                name = "includeHeadingInContent",
                value = "是否提取标题作为内容（默认false）",
                dataType = "boolean"
            ),
            @ApiImplicitParam(
                name = "extractAsWholeParagraph",
                value = "是否作为整个段落提取（默认true）",
                dataType = "boolean"
            ),
            @ApiImplicitParam(name = "extractStyle", value = "是否提取样式（默认false）", dataType = "boolean"),
            @ApiImplicitParam(name = "extractTitle", value = "是否提取标题（默认false）", dataType = "boolean"),
            @ApiImplicitParam(name = "filterEmptyContent", value = "是否过滤空内容（默认true）", dataType = "boolean")
        }
    )
    @PostMapping("/extract/v0")
    public ResultVO<?> extractM0(@RequestParam("file") MultipartFile file, @ModelAttribute ExtractionOptions options)
        throws Exception {
        JSONObject jsonObject = ExtractorDoc.extract0(file, options);
        return ResultVO.ok(jsonObject);
    }

    @ApiOperation(value = "提取文档内容v1")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "file", value = "源文件", dataType = "__file", required = true),
            @ApiImplicitParam(
                name = "includeHeadingInContent",
                value = "是否提取标题作为内容（默认false）",
                dataType = "boolean"
            ),
            @ApiImplicitParam(
                name = "extractAsWholeParagraph",
                value = "是否作为整个段落提取（默认true）",
                dataType = "boolean"
            ),
            @ApiImplicitParam(name = "extractStyle", value = "是否提取样式（默认false）", dataType = "boolean"),
            @ApiImplicitParam(name = "extractTitle", value = "是否提取标题（默认false）", dataType = "boolean"),
            @ApiImplicitParam(name = "filterEmptyContent", value = "是否过滤空内容（默认true）", dataType = "boolean")
        }
    )
    @PostMapping("/extract/v1")
    public ResultVO<?> extractM1(@RequestParam("file") MultipartFile file, @ModelAttribute ExtractionOptions options)
        throws Exception {
        JSONObject jsonObject = ExtractorDoc.extract1(file, options);
        return ResultVO.ok(jsonObject);
    }

    @ApiOperation(value = "渲染文档内容v0")
    @ApiImplicitParams({ @ApiImplicitParam(name = "json", value = "源文件json", required = true) })
    @PostMapping("/render/v0")
    public ResultVO<?> renderFile(@RequestParam("json") String json) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(json);
        OutputStream outputStream = ExtractorRender.renderJsonToWord(jsonObject);

        StreamUtil.outputStreamToFile(
            outputStream,
            FilePathConstant.OUTPUT,
            jsonObject.getJSONObject("data").getString("file_name")
        );
        return ResultVO.ok();
    }
}
