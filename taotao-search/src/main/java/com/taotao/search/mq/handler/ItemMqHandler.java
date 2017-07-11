package com.taotao.search.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/11.
 */
public class ItemMqHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

    public void execute(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if ("insert".equals(type) || "update".equals(type)) {
                //通过后台系统的接口获取商品数据
                Item item = itemService.queryItemBuId(itemId);
                if (item != null) {
                    httpSolrServer.addBean(item);
                }
                httpSolrServer.commit();
            } else if ("delete".equals(type)){
                //将solr中的索引数据删除
                httpSolrServer.deleteById(String.valueOf(itemId));
                httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
