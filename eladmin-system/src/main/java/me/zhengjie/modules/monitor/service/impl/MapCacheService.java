package me.zhengjie.modules.monitor.service.impl;

import lombok.Data;
import me.zhengjie.modules.monitor.domain.vo.RedisVo;
import me.zhengjie.modules.monitor.service.CacheService;
import me.zhengjie.utils.PageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class MapCacheService implements CacheService {

     @Data
     class  CacheContext{
         long expiration;
         Object o;
     }


    private static final ConcurrentHashMap<String,CacheContext> CACHE_MAP = new ConcurrentHashMap<>();
    //过期单位.分钟
    @Value("${loginCode.expiration}")
    private Long expiration;

    @Value("${jwt.online}")
    private String onlineKey;

    @Value("${jwt.codeKey}")
    private String codeKey;


    @Override
    public Page findByKey(String key, Pageable pageable) {
        List<RedisVo> redisVos = new ArrayList<>();
        Set<String> keys = CACHE_MAP.keySet();
            if(!"*".equalsIgnoreCase(key)){
                key = key.replaceAll("\\*","");
                String finalKey = key;
                keys =keys.parallelStream().filter(t->t.contains(finalKey)).collect(Collectors.toSet());
            }
        for (String s : keys) {
            // 过滤掉权限的缓存
            if (s.contains("role::loadPermissionByUser") || s.contains("user::loadUserByUsername") || s.contains(onlineKey) || s.contains(codeKey)) {
                continue;
            }
            RedisVo redisVo = new RedisVo(s, Objects.requireNonNull(getValue(key)).toString());
            redisVos.add(redisVo);
        }
        return new PageImpl<RedisVo>(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),redisVos),
                pageable,
                redisVos.size());
    }

    @Override
    public String getCodeVal(String key) {
        try {
            return Objects.requireNonNull(getValue(key)).toString();
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public void saveCode(String key, Object val) {
       setValue(key,val,expiration*60*1000);

    }

    @Override
    public void delete(String key) {
        CACHE_MAP.remove(key);
    }

    @Override
    public void deleteAll() {
        CACHE_MAP.clear();
    }

    public static void expireValue() {
        Iterator<Map.Entry<String, CacheContext>> iterator = CACHE_MAP.entrySet().iterator();
        Map.Entry<String, CacheContext> entry = null;
        while (iterator.hasNext()){
           entry =  iterator.next();
            CacheContext cacheContext = entry.getValue();
            if(cacheContext!=null&&cacheContext.getExpiration()>0){
                if(cacheContext.getExpiration()<System.currentTimeMillis()){
                    iterator.remove();
                    continue;
                }
            }
        }
    }

    @Override
    public Object getValue(String key){
        CacheContext cacheContext = CACHE_MAP.get(key);
        if(cacheContext==null){
            return null;
        }
        if(cacheContext!=null&&cacheContext.getExpiration()>0){
            if(cacheContext.getExpiration()<System.currentTimeMillis()){
                return null;
            }
        }
        return cacheContext.getO();
    }

    @Override
    public void setValue(String key,Object value,long time){

        CacheContext cacheContext = new CacheContext();
        cacheContext.setO(value);
        if(time<=0){
            cacheContext.setExpiration(System.currentTimeMillis()+time);
        }
        CACHE_MAP.put(key,cacheContext);

    }

    @Override
    public Collection keys(String key) {
        Set<String> keys = CACHE_MAP.keySet();
        if(!"*".equalsIgnoreCase(key)){
            key = key.replaceAll("\\*","");
            String finalKey = key;
            keys =keys.parallelStream().filter(t->t.contains(finalKey)).collect(Collectors.toSet());
        }
        return keys;
    }

}
