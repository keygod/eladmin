//package me.zhengjie.modules.monitor.service.impl;
//
//import me.zhengjie.modules.monitor.domain.vo.RedisVo;
//import me.zhengjie.modules.monitor.service.CacheService;
//import me.zhengjie.utils.PageUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
///**
// * @author Zheng Jie
// * @date 2018-12-10
// */
////@Service
//@SuppressWarnings({"unchecked","all"})
//public class RedisServiceImpl implements CacheService {
//
//    private final RedisTemplate redisTemplate;
//
//    @Value("${loginCode.expiration}")
//    private Long expiration;
//
//    @Value("${jwt.online}")
//    private  String onlineKey;
//
//    @Value("${jwt.codeKey}")
//    private String codeKey;
//
//
//
//    public RedisServiceImpl(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public Page<RedisVo> findByKey(String key, Pageable pageable){
//
//        List<RedisVo> redisVos = new ArrayList<>();
//        if(!"*".equals(key)){
//            key = "*" + key + "*";
//        }
//        Set<String> keys = redisTemplate.keys(key);
//        for (String s : keys) {
//            // 过滤掉权限的缓存
//            if (s.contains("role::loadPermissionByUser") || s.contains("user::loadUserByUsername") || s.contains(onlineKey) || s.contains(codeKey)) {
//                continue;
//            }
//            RedisVo redisVo = new RedisVo(s, Objects.requireNonNull(redisTemplate.opsForValue().get(s)).toString());
//            redisVos.add(redisVo);
//        }
//        return new PageImpl<RedisVo>(
//                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),redisVos),
//                pageable,
//                redisVos.size());
//    }
//
//    @Override
//    public void delete(String key) {
//        redisTemplate.delete(key);
//    }
//
//    @Override
//    public void deleteAll() {
//        Set<String> keys = redisTemplate.keys(  "*");
//        redisTemplate.delete(keys.stream().filter(s -> !s.contains(onlineKey)).filter(s -> !s.contains(codeKey)).collect(Collectors.toList()));
//    }
//
//    @Override
////    public Object getValue(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    @Override
//    public void setValue(String key, Object value, long time) {
//        redisTemplate.opsForValue().set(key,value);
//        redisTemplate.expire(key,time, TimeUnit.SECONDS);
//    }
//
//    @Override
//    public Collection keys(String s) {
//        return redisTemplate.keys(s);
//    }
//
//    @Override
//    public String getCodeVal(String key) {
//        try {
//            return Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString();
//        }catch (Exception e){
//            return "";
//        }
//    }
//
//    @Override
//    public void saveCode(String key, Object val) {
//        redisTemplate.opsForValue().set(key,val);
//        redisTemplate.expire(key,expiration, TimeUnit.MINUTES);
//    }
//}
