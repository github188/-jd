package cn.taotao.crawler;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.taotao.crawler.thread.ThreadPool;

/**
 * 爬虫程序入口
 *
 * @author 传智播客  张志君
 * @version V1.0
 * @date 2014年12月18日 下午2:39:10
 */
public class Main {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext*.xml");

        //从Spring容器中获取到所有可以执行的爬虫,并且放到线程池中执行
        Map<String, Crawler> map = applicationContext.getBeansOfType(Crawler.class);
		for (Crawler crawler : map.values()) {
			ThreadPool.runInThread(crawler);
		}
//        JD3CCrawler jd3CCrawler = new JD3CCrawler("http://http://list.jd.com/list.html?cat=9987,653,655&page={page}",
//                (long) 633);
//        ThreadPool.runInThread(jd3CCrawler);

    }

}
