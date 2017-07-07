package com.taotao.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 片式集群的redis操作
 * Created by Ellen on 2017/7/7.
 */
@Service
public class RedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = false)//如果运行环境有注入，没有不注入
    private ShardedJedisPool shardedJedisPool;

    private <T> T excute(Function<T, ShardedJedis> function) {
        ShardedJedis shardedJedis = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("redis开启set操作");
            }

            shardedJedis = shardedJedisPool.getResource();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("set操作完成");
            }
            return function.callback(shardedJedis);
        } catch (Exception e) {
            LOGGER.error("set操作失败");
            return null;
        }
    }

    /**
     * 执行set操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        return excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    /**
     * 执行get操作
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    /**
     * 执行delete操作
     *
     * @param key
     * @return
     */
    public Long delete(String key) {
        return excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }
        });
    }

    /**
     * 设置生存时间，单位为秒
     *
     * @param key
     * @param time
     * @return
     */
    public Long expire(String key, int time) {
        return excute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callback(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, time);
            }
        });
    }

    /**
     * 带有生存时间的set操作
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public String setExpire(String key, String value, int time) {
        return excute(new Function<String, ShardedJedis>() {
            @Override
            public String callback(ShardedJedis shardedJedis) {
                String res = shardedJedis.set(key, value);
                shardedJedis.expire(key, time);
                return res;
            }
        });
    }
}
