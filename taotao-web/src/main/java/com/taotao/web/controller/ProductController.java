package com.taotao.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ellen on 2017/7/27.
 */
@Controller
public class ProductController {

    @RequestMapping(value = "/product/{itemCatId}")
    public String productCon(@PathVariable(value = "itemCatId") long itemCatId) {

        return "redirct:";
    }
}
