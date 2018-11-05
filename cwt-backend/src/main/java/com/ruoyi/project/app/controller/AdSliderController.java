package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.AdSliderDto;
import com.cwt.service.service.IAdSliderService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 首页广告轮播 信息操作处理
 *
 * @author ruoyi
 * @date 2018-09-03
 */
@Controller
@RequestMapping("/app/adSlider")
public class AdSliderController extends BaseController {
    private String prefix = "app/adSlider";

    @Value("${FILES_DIR.AD_SLIDER_IMG}")
    private String AD_SLIDER_IMG_PATH;//广告图片上传路径

    @Autowired
    private IAdSliderService adSliderService;

    @RequiresPermissions("app:adSlider:view")
    @GetMapping()
    public String adSlider() {
        return prefix + "/adSlider";
    }

    /**
     * 查询首页广告轮播列表
     */
    @RequiresPermissions("app:adSlider:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AdSliderDto adSlider) {
        startPage();
        List<AdSliderDto> list = adSliderService.selectAdSliderList(adSlider);
        return getDataTable(list);
    }

    /**
     * 新增首页广告轮播
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存首页广告轮播
     */
    @RequiresPermissions("app:adSlider:add")
    @Log(title = "首页广告轮播", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AdSliderDto adSliderDto, @RequestParam("adImgFile") MultipartFile adImgFile) {

        String fileName = "";
        try {
            //广告轮播图片保存到本地/服务器
            fileName = saveFile(adImgFile, AD_SLIDER_IMG_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return toAjax(0);  //返回操作失败
        }
        adSliderDto.setImgPath(fileName);

        return toAjax(adSliderService.insertAdSlider(adSliderDto));
    }

    /**
     * 修改首页广告轮播
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        AdSliderDto adSlider = adSliderService.selectAdSliderById(id);
        mmap.put("adSlider", adSlider);
        return prefix + "/edit";
    }

    /**
     * 修改保存首页广告轮播
     */
    @RequiresPermissions("app:adSlider:edit")
    @Log(title = "首页广告轮播", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AdSliderDto adSliderDto, @RequestParam("adImgFile") MultipartFile adImgFile){

        String fileName = "";
        if(!adImgFile.isEmpty()){
            try {
                //广告轮播图片保存到本地/服务器
                fileName = saveFile(adImgFile, AD_SLIDER_IMG_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                return toAjax(0);  //返回操作失败
            }
        }
        adSliderDto.setImgPath(fileName);

        return toAjax(adSliderService.updateAdSlider(adSliderDto));
    }

    /**
     * 删除首页广告轮播
     */
    @RequiresPermissions("app:adSlider:remove")
    @Log(title = "首页广告轮播", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(adSliderService.deleteAdSliderByIds(ids));
    }


    /**
     * 保存文件
     *
     * @param appealFile  文件
     * @param fileRootDir 文件存储根目录
     * @throws IOException 存储异常
     */
    private String saveFile(MultipartFile appealFile, String fileRootDir) throws IOException {
        //源文件名
        String name = appealFile.getOriginalFilename();
        //文件后缀名
        String type = name.substring(name.lastIndexOf("."));
        //新文件名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + type;
        //申诉文件保存到本地/服务器
        String filePath = fileRootDir + fileName;
        appealFile.transferTo(new File(filePath));
        return fileName;
    }

}
