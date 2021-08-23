package com.eastbabel.aop;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebContext {

    private static final String USER_ID = "user_id";
    private static final String TOKEN = "token";

    private final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal();

    public WebContext() {
    }

    private void put(String key, Object value) {
        Map<String, Object> map = (Map)this.threadLocal.get();
        if (map == null) {
            map = new HashMap();
        }

        ((Map)map).put(key, value);
        this.threadLocal.set(map);
    }

    private <T> T get(String key) {
        Map<String, Object> map = (Map)this.threadLocal.get();
        if (map == null) {
            map = new HashMap();
        }

        return (T) ((Map)map).get(key);
    }

    public void setToken(String token) {
        this.put(TOKEN, token);
    }

    public String getToken() {
        return (String)this.get(TOKEN);
    }

    public void clear() {
        this.threadLocal.remove();
    }

    public Integer getUserId() {
        return (Integer)this.get(USER_ID);
    }

    public void setUserId(Integer userId) {
        this.put(USER_ID, userId);
    }
}
