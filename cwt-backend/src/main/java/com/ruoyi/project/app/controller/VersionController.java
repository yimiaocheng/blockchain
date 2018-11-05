package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.VersionDto;
import com.cwt.service.service.IVersionService;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 应用版本 信息操作处理
 *
 * @author ruoyi
 * @date 2018-09-06
 */
@Controller
@RequestMapping("/app/version")
public class VersionController extends BaseController {
    private String prefix = "app/version";

    @Value("${FILES_DIR.APP_APK}")
    private String APP_APK_PATH;//app APK上传下载路径

    @Autowired
    private IVersionService versionService;

    @RequiresPermissions("app:version:view")
    @GetMapping()
    public String version() {
        return prefix + "/version";
    }

    /**
     * 查询应用版本列表
     */
    @RequiresPermissions("app:version:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(VersionDto version) {
        startPage();
        List<VersionDto> list = versionService.selectVersionList(version);
        return getDataTable(list);
    }

    /**
     * 新增应用版本
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("versionList", versionService.selectVersionList(null));
        return prefix + "/add";
    }

    /**
     * 新增保存应用版本
     */
    @RequiresPermissions("app:version:add")
    @Log(title = "应用版本", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(VersionDto versionDto, @RequestParam("apkUrlFile") MultipartFile apkUrlFile) {
        String fileName = "";

        if(! apkUrlFile.isEmpty()){
            try {
                //apk应用保存到本地/服务器
                fileName = saveFile(apkUrlFile, APP_APK_PATH, versionDto.getVersion());
            } catch (Exception e) {
                e.printStackTrace();
                return toAjax(0);  //返回操作失败
            }
        }
        versionDto.setApkUrl(fileName);

        return toAjax(versionService.insertVersion(versionDto));
    }

    /**
     * 修改应用版本
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        VersionDto version = versionService.selectVersionById(id);
        mmap.put("versionObj", version);
        return prefix + "/edit";
    }

    /**
     * 修改保存应用版本
     */
    @RequiresPermissions("app:version:edit")
    @Log(title = "应用版本", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(VersionDto versionDto, @RequestParam("apkUrlFile") MultipartFile apkUrlFile) {
        String fileName = "";

        if(! apkUrlFile.isEmpty()){
            try {
                //apk应用保存到本地/服务器
                fileName = saveFile(apkUrlFile, APP_APK_PATH, versionDto.getVersion());
            } catch (Exception e) {
                e.printStackTrace();
                return toAjax(0);  //返回操作失败
            }
        }
        versionDto.setApkUrl(fileName);

        return toAjax(versionService.updateVersion(versionDto));
    }

    /**
     * 删除应用版本
     */
    @RequiresPermissions("app:version:remove")
    @Log(title = "应用版本", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(versionService.deleteVersionByIds(ids));
    }

    /**
     * 保存文件
     *
     * @param appealFile  文件
     * @param fileRootDir 文件存储根目录
     * @throws IOException 存储异常
     */
    private String saveFile(MultipartFile appealFile, String fileRootDir, String version) throws IOException {
        //判断文件夹是否存在
        File filePathDir = new File(fileRootDir);
        if (!filePathDir.exists() && !filePathDir.isDirectory()) {
            filePathDir.mkdir();
        }
        //源文件名
        String name = appealFile.getOriginalFilename();
        //文件后缀名
        String type = name.substring(name.lastIndexOf("."));
        //新文件名
        String fileName = "CT_" + version + type;
        //申诉文件保存到本地/服务器
        String filePath = fileRootDir + fileName;
        appealFile.transferTo(new File(filePath));
        return fileName;
    }
}
