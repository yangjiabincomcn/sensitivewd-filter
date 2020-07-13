package cn.modo;

import cn.modo.sensitive.dao.GameMapper;
import cn.modo.sensitive.dao.SensitiveWordMapper;
import cn.modo.sensitive.util.WordFilter;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EnableAspectJAutoProxy
@SpringBootApplication
@MapperScan("cn.modo.sensitive.dao")
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class App implements ApplicationRunner {
    private final SensitiveWordMapper sensitiveWordMapper;

    private final GameMapper gameMapper;

    public static Map<Integer, String> GAME_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args)  {
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
