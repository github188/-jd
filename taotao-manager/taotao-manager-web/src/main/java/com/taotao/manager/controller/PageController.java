package com.taotao.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 通用的页面跳转逻辑
 * Created by Ellen on 2017/7/2.
 */
@Controller
@RequestMapping("page")
public class PageController {

    @RequestMapping(value = "{pageName}", method = RequestMethod.GET)
    public String toPage(@PathVariable(value = "pageName") String pageName) {
        return pageName;
    }
}
