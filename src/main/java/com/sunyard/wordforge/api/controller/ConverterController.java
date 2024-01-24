package com.sunyard.wordforge.api.controller;

import com.sunyard.wordforge.complex.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "word转pdf")
    @GetMapping("/word2pdf")
    public ResultVO<?> word2pdf() {
        return ResultVO.create(true);
    }
}
