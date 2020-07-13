package cn.modo.sensitive.timedtask;

import cn.modo.sensitive.dao.GameMapper;
import cn.modo.sensitive.dao.SensitiveWordMapper;
import cn.modo.sensitive.util.WordFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cn.modo.App.GAME_MAP;

/**
 * @author yjb
 **/
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Scheduling {

    private final SensitiveWordMapper sensitiveWordMapper;
    private final GameMapper gameMapper;


    @Scheduled(cron = "0/30 * * * * *")
    private void setKeyword() {
        try {
            long a = System.nanoTime();
            //初始化 游戏列表
            GAME_MAP = gameMapper.queryAll();
            // 初始化 敏感词过滤需要的  白名单 黑名单和停顿词集合
            WordFilter.init(sensitiveWordMapper.getKeyword(null, null));
            a = System.nanoTime() - a;
            System.out.println("加载时间 : " + a + "ns");
            System.out.println("加载时间 : " + a / 1000000 + "ms");
        } catch (Exception e) {
            throw new RuntimeException("初始化过滤器失败");
        }

    }

}
