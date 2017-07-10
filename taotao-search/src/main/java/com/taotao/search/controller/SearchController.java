package com.taotao.search.controller;

import com.sun.rowset.internal.Row;
import com.taotao.search.bean.Item;
import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Created by Ellen on 2017/7/10.
 */
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    public static final int ROWS = 32;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keyWord,
                               @RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mv = new ModelAndView("search");
        SearchResult result = null;
        String keyword = null;
        try {
            keyword = new String(keyWord.getBytes("ISO-8859-1"), "UTF-8");
            result = searchService.search(keyword, page, ROWS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SearchResult(new ArrayList<Item>(0), 0);
        }

        //搜索关键字
        mv.addObject("query", keyword);

        //搜索结果集itemList
        mv.addObject("itemList", result.getList());

        //当前页数
        mv.addObject("page", page);

        //总页数
        int total = ((Long)result.getTotal()).intValue();
        int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
        mv.addObject("pages", pages);

        return mv;
    }
}
