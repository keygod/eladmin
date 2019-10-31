package me.zhengjie.config;

import me.zhengjie.modules.monitor.service.CacheService;
import me.zhengjie.modules.monitor.service.impl.MapCacheService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CacheConfig {

//        @Bean
//        @ConditionalOnProperty(prefix = "spring",name = "cache",havingValue = "redis")
//        @ConditionalOnMissingBean(me.zhengjie.modules.monitor.service.CacheService.class)
//        public CacheService redisService(RedisTemplate redisTemplate){
//                return new RedisServiceImpl(redisTemplate);
//        }

        @Configuration
        @EnableScheduling
        @ConditionalOnProperty(prefix = "spring",name = "cache",havingValue = "map")
        public static class MapCacheConfig{


                @Bean
                public CacheService redisService(){
                        return new MapCacheService();
                }

                @Scheduled(cron = "0/30 * * * * ? ")
                public void expireValue(){
                        MapCacheService.expireValue();
                }

        }

}
