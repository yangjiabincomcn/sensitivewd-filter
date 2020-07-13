package cn.modo.sensitive.controller;

import cn.modo.sensitive.dto.SensitiveDto;
import cn.modo.sensitive.service.SensitiveWordService;
import cn.modo.sensitive.util.ResponseMsg;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yjb
 **/
@Slf4j
@RestController
@RequestMapping(value = "sensitiveWord")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveWordController {
    private final SensitiveWordService sensitiveWordService;

    @RequestMapping(value = "checkSensitive")
    public Mono<ResponseMsg> checkSensitive(@RequestBody @Valid SensitiveDto sensitiveDto) {
        return Mono.create(sink -> {
            long a = System.nanoTime();
            ResponseMsg responseMsg = sensitiveWordService.checkSensitive(sensitiveDto);
            a = System.nanoTime() - a;
            System.out.println("时间 微秒/ : " + a + "ns");
            System.out.println("时间 毫秒/: " + a / 1000000 + "ms");
            sink.success(responseMsg);
        });
    }

    @PostMapping(value = "query")
    public ResponseMsg querySensitiveWord(@RequestBody String param) {
        return sensitiveWordService.querySensitiveWord(JSONObject.parseObject(param));
    }

    @PostMapping(value = "add")
    public ResponseMsg add(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        return sensitiveWordService.add(jsonObject);
    }

    @PostMapping(value = "delete")
    public ResponseMsg delete(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        return sensitiveWordService.delete(jsonObject);
    }

    @PostMapping(value = "update")
    public ResponseMsg update(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        return sensitiveWordService.update(jsonObject);
    }


    /**
     * 刷新敏感词库
     *
     * @return
     */
    @PostMapping(value = "refreshSensitiveWord")
    public ResponseMsg refreshSensitiveWord() {
        return sensitiveWordService.refreshSensitiveWord();
    }


}
