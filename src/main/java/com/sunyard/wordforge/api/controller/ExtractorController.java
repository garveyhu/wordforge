package com.sunyard.wordforge.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunyard.wordforge.complex.constant.FilePathConstant;
import com.sunyard.wordforge.complex.vo.ResultVO;
import com.sunyard.wordforge.feature.extractor.ExtractRender;
import com.sunyard.wordforge.feature.extractor.ExtractorDoc;
import com.sunyard.wordforge.util.StreamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.OutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @ApiOperation(value = "提取文档内容m0")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/extract/m0")
    public ResultVO<?> extractM0(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject jsonObject = ExtractorDoc.extract0(file);
        return ResultVO.ok(jsonObject);
    }

    @ApiOperation(value = "提取文档内容m1")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/extract/m1")
    public ResultVO<?> extractM1(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject jsonObject = ExtractorDoc.extract1(file);
        return ResultVO.ok(jsonObject);
    }

    @ApiOperation(value = "提取文档内容m2")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/extract/m2")
    public ResultVO<?> extractM2(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject jsonObject = ExtractorDoc.extract2(file);
        return ResultVO.ok(jsonObject);
    }

    @ApiOperation(value = "渲染文档内容m0")
    @ApiImplicitParams({ @ApiImplicitParam(name = "json", value = "源文件json", required = true) })
    @PostMapping("/render")
    public ResultVO<?> renderFile(@RequestParam("json") String json) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(json);
        OutputStream outputStream = ExtractRender.renderJsonToWord(jsonObject);

        StreamUtil.outputStreamToFile(
            outputStream,
            FilePathConstant.OUTPUT,
            jsonObject.getJSONObject("data").getString("file_name")
        );
        return ResultVO.ok();
    }
}
