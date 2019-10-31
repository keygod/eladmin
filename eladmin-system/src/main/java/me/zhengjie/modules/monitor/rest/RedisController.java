package me.zhengjie.modules.monitor.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.monitor.domain.vo.RedisVo;
import me.zhengjie.modules.monitor.service.CacheService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-10
 */
@RestController
@RequestMapping("/api/redis")
@Api(tags = "系统：Redis缓存管理")
public class RedisController {

    private final CacheService cacheService;

    public RedisController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Log("查询Redis缓存")
    @GetMapping
    @ApiOperation("查询Redis缓存")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
    public ResponseEntity getRedis(String key, Pageable pageable){
        return new ResponseEntity<>(cacheService.findByKey(key,pageable), HttpStatus.OK);
    }

    @Log("删除Redis缓存")
    @DeleteMapping
    @ApiOperation("删除Redis缓存")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ResponseEntity delete(@RequestBody RedisVo resources){
        cacheService.delete(resources.getKey());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("清空Redis缓存")
    @DeleteMapping(value = "/all")
    @ApiOperation("清空Redis缓存")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ResponseEntity deleteAll(){
        cacheService.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }
}
