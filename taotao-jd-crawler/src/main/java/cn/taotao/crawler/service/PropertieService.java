package cn.taotao.crawler.service;

import cn.taotao.crawler.spring.PropertyConfig;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {
	
	@PropertyConfig
	public String IMAGE_DIR;
	
	@PropertyConfig
	public String MAX_POOL_SIZE;

}
