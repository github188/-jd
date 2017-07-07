package com.taotao.web.controller;

import com.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ellen on 2017/7/4.
 */
@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "{index}", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        //查询大广告位
        mv.addObject("indexAd1", indexService.getIndexAd1());
        return mv;
    }
}
