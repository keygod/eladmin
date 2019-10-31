package me.zhengjie.modules.security.service;

import me.zhengjie.modules.monitor.service.CacheService;
import me.zhengjie.modules.security.security.JwtUser;
import me.zhengjie.modules.security.security.OnlineUser;
import me.zhengjie.utils.EncryptUtils;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @Date 2019年10月26日21:56:27
 */
@Service
@SuppressWarnings({"unchecked","all"})
public class OnlineUserService {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.online}")
    private String onlineKey;

    private final CacheService cacheService;

    public OnlineUserService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void save(JwtUser jwtUser, String token, HttpServletRequest request){
        String job = jwtUser.getDept() + "/" + jwtUser.getJob();
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(jwtUser.getUsername(), job, browser , ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheService.setValue(onlineKey + token,onlineUser,expiration);
    }

    public Page<OnlineUser> getAll(String filter, Pageable pageable){
        List<String> keys = new ArrayList<String>(cacheService.keys(onlineKey + "*"));
        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) cacheService.getValue(key);
            if(StringUtils.isNotBlank(filter)){
                if(onlineUser.toString().contains(filter)){
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        Collections.sort(onlineUsers, (o1, o2) -> {
            return o2.getLoginTime().compareTo(o1.getLoginTime());
        });
        return new PageImpl<OnlineUser>(
                PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(),onlineUsers),
                pageable,
                keys.size());
    }

    public void kickOut(String val) throws Exception {
        String key = onlineKey + EncryptUtils.desDecrypt(val);
        cacheService.delete(key);
    }

    public void logout(String token) {
        String key = onlineKey + token;
        cacheService.delete(key);
    }
}
