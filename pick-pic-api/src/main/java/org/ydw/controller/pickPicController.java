package org.ydw.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ydw.service.ICheckData;
import org.ydw.vo.request.PickRequest;
import org.ydw.vo.response.PickResponse;

@RestController
@RequestMapping("/pick")
public class pickPicController {

    @Autowired
    ICheckData checkData;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("check")
    public PickResponse checkPictrue(@RequestBody PickRequest request){
        return checkData.checkData(request.getPictruePath());
    }
}
