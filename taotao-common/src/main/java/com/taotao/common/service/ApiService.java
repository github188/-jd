package com.taotao.common.service;

import com.taotao.common.bean.HttpResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 执行get请求，200返回相应内容，其他返回null
 * <p>
 * Created by Ellen on 2017/7/7.
 */
@Service
public class ApiService {

    @Autowired(required = false)
    private CloseableHttpClient httpClient;

    @Autowired(required = false)
    private RequestConfig config;

    public String doGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        //设置请求参数
        httpGet.setConfig(config);

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
        } finally {
            if (response == null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 执行带有参数的get请求
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public String doGet(String url, Map<String, String> map)
            throws IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }
        return doGet(uriBuilder.build().toString());
    }

    /**
     * 执行带有参数的post请求
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> map) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        //设置请求参数
        httpPost.setConfig(config);
        if (map != null) {
            //设置两个post参数一个是scope，一个是q
            List<NameValuePair> parameters = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            //构造一个form表单实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
        }

        //将请求实体设置到http请求中
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);

            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));

        } finally {
            if (response == null) {
                response.close();
            }
        }
    }


    /**
     * 提交json数据
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        //设置请求参数
        httpPost.setConfig(config);
        if (json != null) {
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
        }

        //将请求实体设置到http请求中
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);

            return new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));

        } finally {
            if (response == null) {
                response.close();
            }
        }
    }

    public HttpResult doPost(String url) throws IOException {
        return doPost(url, null);
    }
}