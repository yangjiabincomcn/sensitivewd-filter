package cn.modo.sensitive.controller;

import cn.modo.sensitive.config.MainDatabaseUrl;
import cn.modo.sensitive.util.ResponseMsg;
import cn.modo.sensitive.util.WordFilter;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yjb
 **/
@Slf4j
@RestController
@RequestMapping(value = "/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

    private final MainDatabaseUrl mainDatabaseUrl;

    @RequestMapping(value = "/")
    public String index() {
        return mainDatabaseUrl.getEnv();
    }



}
