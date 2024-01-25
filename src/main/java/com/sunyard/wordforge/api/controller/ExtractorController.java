package com.sunyard.wordforge.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunyard.wordforge.complex.vo.ResultVO;
import com.sunyard.wordforge.feature.extractor.ExtractorDoc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "提取文档内容")
    @ApiImplicitParams({ @ApiImplicitParam(name = "file", value = "源文件", required = true) })
    @PostMapping("/file")
    public ResultVO<?> extractFile(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject jsonObject = ExtractorDoc.extractFile(file);
        return ResultVO.ok(jsonObject);
    }
}
