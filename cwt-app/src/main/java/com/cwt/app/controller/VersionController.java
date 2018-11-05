package com.cwt.app.controller;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.controller.api.VersionApi;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.mySettings.VersionDto;
import com.cwt.service.service.IVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(VersionApi.VERSION_CONTROLLER_API)
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    private IVersionService iVersionService;

    /***lzf***/
    /**
     * 查询最新的版本信息
     * @return
     */
    @ApiOperation(value = VersionApi.selectNewestVersion.METHOD_API_NAME,
            notes = VersionApi.selectNewestVersion.METHOD_API_NOTE)
    @RequestMapping(value = "/selectNewestVersion", method = RequestMethod.GET)
    public ResultDto selectNewestVersion() {
        VersionDto versionDto = iVersionService.selectNewestVersion();
        return ResultUtils.buildSuccessDto(versionDto);
    }
    /**
     * 根据系统类型查询最新的版本信息
     *
     * @return
     */
    @ApiOperation(value = VersionApi.selectNewestVersion.METHOD_API_NAME,
            notes = VersionApi.selectNewestVersion.METHOD_API_NOTE)
    @RequestMapping(value = "/selectNewestVersionV2", method = RequestMethod.GET)
    public ResultDto selectNewestVersionV2(@ApiParam("系统类型") @RequestParam(value = "systemType", required = false) String systemType) {
        VersionDto versionDto = iVersionService.selectNewestVersionV2(systemType);
        return ResultUtils.buildSuccessDto(versionDto);
    }

    /**
     * 查询所有系统最新的版本信息
     *
     * @return
     */
    @ApiOperation(value = VersionApi.selectNewestVersion.METHOD_API_NAME,
            notes = VersionApi.selectNewestVersion.METHOD_API_NOTE)
    @RequestMapping(value = "/selectNewestVersionAll", method = RequestMethod.GET)
    public ResultDto selectNewestVersionAll() {
        return ResultUtils.buildSuccessDto(iVersionService.selectNewestVersionAll());
    }

}

