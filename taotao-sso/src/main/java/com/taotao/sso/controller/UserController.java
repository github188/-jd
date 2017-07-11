package com.taotao.sso.controller;

import com.taotao.common.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sun.security.util.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ellen on 2017/7/8.
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private static final String COOKIE_NAME = "TT_TOKEN";

    /**
     * 注册跳转
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    /**
     * 登录跳转
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 检查数据是否可用
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkParam(@PathVariable("param") String param,
                                              @PathVariable("type") int type) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("用户检查，param = {}", param);
            }

            Boolean res = userService.checkParam(param, type);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("用户检查完成，param = {}", param);
            }
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            LOGGER.error("用户检查出错", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 实现用户的注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的注册，user = {}", user);
            }

            Map<String, Object> map = new HashMap<>();
            if (bindingResult.hasErrors()) {
                //校验中有不符合规则的内容
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                List<String> errorMessage = new ArrayList<>();
                for (ObjectError error : allErrors) {
                    errorMessage.add(error.getDefaultMessage());
                }
                map.put("status", "400");
                map.put("data", StringUtils.join(errorMessage, '|'));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("实现用户的注册校验错误，user = {}", user);
                }
                return map;
            }
            Boolean res = userService.doRegister(user);
            if (res) {
                map.put("status", "200");
            } else {
                map.put("status", "500");
                map.put("data", "好的，我知道了");
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的注册成功，user = {}", user);
            }
            return map;
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的注册失败，服务器出错，user = {}", user);
            }
            return null;
        }
    }


    /**
     * 实现用户的登录
     *
     * @param
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam("username") String userName,
                                       @RequestParam("password") String password,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的登录，user = {}");
            }
            Map<String, Object> map = new HashMap<>();

            String token = userService.doLogin(userName, password);
            if (token == null) {
                //登录失败
                map.put("status", 400);
            } else {
                //登录成功
                map.put("status", 200);
                //将token放进cookie中
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的登录成功，user = {}");
            }
            return map;
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("实现用户的登录失败，服务器出错，user = {}");
            }
            return null;
        }
    }

    /**
     * 根据token查询登录状态
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("根据token查询登录状态，user = {}");
            }
            User user = userService.queryUserBytoken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("根据token查询登录状态成功，user = {}");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("根据token查询登录状态失败，服务器出错，user = {}");
            }
            return null;
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(@CookieValue("TT_TOKEN") String token) {
        userService.logout(token);
        return "login";
    }
}
