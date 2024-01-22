package org.core.oss.service.impl.alist.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.oss.service.impl.alist.model.list.FsList;
import org.core.oss.service.impl.alist.model.list.req.ListReq;
import org.core.oss.service.impl.alist.model.list.res.ContentItem;
import org.core.oss.service.impl.alist.model.list.res.ListRes;
import org.core.oss.service.impl.alist.model.login.req.LoginReq;
import org.core.oss.service.impl.alist.model.login.res.DataRes;
import org.core.oss.service.impl.alist.model.login.res.LoginRes;
import org.springframework.beans.BeanUtils;

import java.util.*;

@Slf4j
public class AlistUtil {
    private AlistUtil() {
    }
    
    private static String req(String url, String body) {
        return req(url, body, null);
    }
    
    private static String req(String url, String body, Map<String, String> headers) {
        HttpRequest post = HttpRequest.post(url);
        post.body(body);
        post.headerMap(headers, true);
        try (HttpResponse execute = post.execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new BaseException(ResultCode.OSS_ACCESS_ERROR);
        }
    }
    
    public static String login(String host, String accessKey, String secretKey) throws JsonProcessingException {
        LoginReq req = new LoginReq();
        req.setUsername(accessKey);
        req.setPassword(secretKey);
        ObjectMapper mapper = new ObjectMapper();
        String resStr = req(host + "/api/auth/login", mapper.writeValueAsString(req));
        LoginRes loginRes = mapper.readValue(resStr, LoginRes.class);
        return Optional.ofNullable(loginRes.getData()).orElse(new DataRes()).getToken();
    }
    
    public static Set<FsList> list(SaveConfig config, String path, boolean refresh, String loginJwtCache) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", loginJwtCache);
        
        ListReq req = new ListReq();
        req.setPath(path);
        req.setPage(1);
        req.setPerPage(0);
        req.setRefresh(refresh);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String host = config.getHost();
            String s = CharSequenceUtil.removeSuffix(config.getHost(), "/");
            String resStr = req(s + "/api/fs/list", mapper.writeValueAsString(req), headers);
            ListRes res = mapper.readValue(resStr, ListRes.class);
            if (res.getCode() != 200) {
                log.error(resStr);
                throw new BaseException(ResultCode.OSS_ACCESS_ERROR);
            }
            
            LinkedHashSet<FsList> fsLists = new LinkedHashSet<>();
            List<ContentItem> content = Optional.ofNullable(res.getData().getContent()).orElse(Collections.emptyList());
            if (res.getData().getTotal() == content.size()) {
                for (ContentItem contentItem : content) {
                    FsList e = new FsList();
                    BeanUtils.copyProperties(contentItem, e);
                    e.setPath(path + '/' + contentItem.getName());
                    e.setIsDir(contentItem.getDir());
                    fsLists.add(e);
                }
            } else {
                int sum = res.getData().getTotal();
                int count = content.size();
                int page = 1;
                while (sum == count) {
                    page++;
                    ListReq req1 = new ListReq();
                    req1.setPath(path);
                    req1.setPage(page);
                    req1.setPerPage(0);
                    String resStr2 = req(host + path, mapper.writeValueAsString(req1));
                    ListRes res2 = mapper.readValue(resStr2, ListRes.class);
                    for (ContentItem contentItem : res2.getData().getContent()) {
                        FsList e = new FsList();
                        e.setPath(path + '/' + e.getName());
                        BeanUtils.copyProperties(contentItem, e);
                        fsLists.add(e);
                    }
                    count += content.size();
                }
            }
            return fsLists;
        } catch (JsonProcessingException e) {
            throw new BaseException(ResultCode.OSS_CONNECT_ERROR);
        }
    }
}
