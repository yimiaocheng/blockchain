package com.cwt.app.controller;

import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.BalancetApi;
import com.cwt.app.controller.api.UserApi;
import com.cwt.common.enums.UserResultEnums;
import com.cwt.domain.dto.ResultDto;
import com.cwt.service.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    private static final Logger LOG = LoggerFactory.getLogger(FileUploadController.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;

    @Value("${FILES_DIR.HEAD_IMG}")
    private String FILE_HEAD_IMG_PATH;//头像上传目录
    @Value("${FILES_DIR.APPEAL}")
    private String FILE_APPEAL_PATH;//申诉文件上传

    @ApiOperation(value = UserApi.updateHeadImg.METHOD_API_NAME,
            notes = UserApi.updateHeadImg.METHOD_API_NOTE)
    @RequestMapping(value = "/updateHeadImg", method = RequestMethod.POST)
    public ResultDto updateHeadImg(@ApiParam(UserApi.updateHeadImg.METHOD_API_IMG) @RequestParam(value = "img", required = false) String img, HttpServletRequest request) {
        //返回的状态信息
        ResultDto resultDto = new ResultDto();
        try {
            //获取图片文件
            String imgName = generateImage(img, FILE_HEAD_IMG_PATH);
            String userId = SessionUtils.getSession(request.getSession()).getUserId();
            userService.updateHeadImg(userId, imgName);
            return ResultUtils.buildSuccessDto(imgName);
        } catch (Exception e) {
            resultDto.setCode(UserResultEnums.FILE_ERROR.getCode());
            resultDto.setMsg(UserResultEnums.FILE_ERROR.getMsg());
            LOG.error(e.getMessage());
            return resultDto;
        }
    }

    /***
     * 申述文件上传
     * @param appealFile 文件
     */
    @ApiOperation(value = BalancetApi.headerAppealFile.METHOD_API_NAME
            , notes = BalancetApi.headerAppealFile.METHOD_API_NOTE)
    @RequestMapping(value = "/headerAppealFile", method = RequestMethod.POST)
    public ResultDto headerAppealFile(@ApiParam(BalancetApi.headerAppealFile.METHOD_AIP_APPEALFILE) @RequestParam("appealFile") MultipartFile appealFile) {
        try {
            //申诉文件保存到本地/服务器
            String fileName = saveFile(appealFile,FILE_APPEAL_PATH);
            return ResultUtils.buildSuccessDto(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultDto("500", "文件上传失败");
        }
    }


    /**
     * 将base64字符串存储为jpg文件
     *
     * @param imgStr      base64字符串
     * @param fileRootDir 文件根目录
     * @return 文件名
     * @throws Exception 存储异常
     */
    private String generateImage(String imgStr, String fileRootDir) throws IOException {   //对字节数组字符串进行Base64解码并生成图片
        if(StringUtils.isEmpty(imgStr)){
            throw new RuntimeException("file base64 string is empty");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b = decoder.decodeBuffer(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {//调整异常数据
                b[i] += 256;
            }
        }
        //自定义文件名
        String fileName = createFileName() + ".jpg";
        //判断文件夹是否存在
        File filePath = new File(fileRootDir);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        //生成jpeg图片
        String imgFilePath = fileRootDir + fileName;//新生成的图片
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            return fileName;
        } catch (IOException e) {
            throw e;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存文件
     * @param appealFile 文件
     * @param fileRootDir 文件存储根目录
     * @throws IOException 存储异常
     */
    private String saveFile(MultipartFile appealFile,String fileRootDir) throws IOException {
        //源文件名
        String name = appealFile.getOriginalFilename();
        //文件后缀名
        String type = name.substring(name.lastIndexOf("."));
        //新文件名
        String fileName = createFileName() + type;
        //申诉文件保存到本地/服务器
        String filePath = fileRootDir + fileName;
        appealFile.transferTo(new File(filePath));
        return fileName;
    }
    /**
     * 文件名命名规则
     * @return 文件名
     */
    private String createFileName() {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        String timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return userId + "-" + timeFormat;
    }
}
