package com.lt.modules.sys.controller;

import com.lt.common.BaseResponse;
import com.lt.common.ErrorCode;
import com.lt.common.annotation.SysLog;
import com.lt.common.utils.ResultUtils;
import com.lt.modules.sys.model.dto.user.UserLoginRequest;
import com.lt.modules.sys.model.entity.User;
import com.lt.modules.sys.service.CaptchaService;
import com.lt.modules.sys.service.UserService;
import com.lt.modules.sys.service.UserTokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @description: 登录控制器
 * @author: ~Teng~
 * @date: 2022/11/17 13:06
 */
@RestController
public class LoginController extends AbstractController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 获取图片验证码
        BufferedImage image = captchaService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 系统登录
     */
    @PostMapping("/login")
    public BaseResponse login(@RequestBody @Validated UserLoginRequest userLoginRequest) {
        String captcha = userLoginRequest.getCaptcha();
        String username = userLoginRequest.getUsername();
        String uuid = userLoginRequest.getUuid();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password, uuid, captcha)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请输入完整登录信息");
        }
        // 1. 验证码校验
        boolean flag = captchaService.validate(uuid, captcha);
        if (!flag) {
            return ResultUtils.error(ErrorCode.CAPTCHA_ERROR, "验证码错误");
        }
        // 2. 用户信息
        User user = userService.queryByUserName(username);
        // 账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(password).toHex())) {
            return ResultUtils.error(ErrorCode.LOGIN_ERROR, "账号或密码错误");
        }
        // 账号锁定
        if (user.getStatus() == 1) {
            return ResultUtils.error(ErrorCode.LOGIN_ERROR, "账号被停用，请联系管理员");
        }
        if (user.getStatus() == 2) {
            return ResultUtils.error(ErrorCode.LOGIN_ERROR, "账号正在审核中，暂时无法登录");
        }
        // 3. 生成token，并保存到数据库
        Map<String, Object> tokenMap = userTokenService.createToken(user.getId());
        return ResultUtils.success(tokenMap);
    }

    @PostMapping("/logout")
    @SysLog("退出系统")
    public BaseResponse logout() {
        Long userId = getUserId();
        if (userId == null) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "非法退出");
        }
        userTokenService.logout(userId);
        return ResultUtils.success("退出成功");
    }

    public static void main(String[] args) {
        System.out.println(new Sha256Hash("modi").toHex());
    }
}
