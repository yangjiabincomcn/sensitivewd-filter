package cn.modo.sensitive.service;

import cn.modo.sensitive.dao.GameMapper;
import cn.modo.sensitive.dao.SensitiveWordMapper;
import cn.modo.sensitive.dto.SensitiveDto;
import cn.modo.sensitive.pojo.SensitiveWord;
import cn.modo.sensitive.util.ResponseMsg;
import cn.modo.sensitive.util.WordFilter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static cn.modo.App.GAME_MAP;

/**
 * @author yjb
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveWordService {

    private final SensitiveWordMapper sensitiveWordMapper;

    private final GameMapper gameMapper;

    public ResponseMsg querySensitiveWord(JSONObject jsonObject) {
        Integer wordType = jsonObject.getInteger("wordType");
        String keyword = jsonObject.getString("keyword");
        Integer strategy = jsonObject.getInteger("strategy");
        List<SensitiveWord> all = sensitiveWordMapper.findAll(wordType, keyword, strategy);
        return ResponseMsg.successData(all);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseMsg add(JSONObject jsonObject) {
        JSONArray sensitiveWords = jsonObject.getJSONArray("sensitiveWords");
        Integer type = jsonObject.getInteger("type");
        if (sensitiveWords.isEmpty() || type == null) {
            return ResponseMsg.error("参数有误");
        }
        SensitiveWord sensitiveWord = new SensitiveWord();
        for (String name : sensitiveWords.toJavaList(String.class)) {
            if (sensitiveWordMapper.findByName(name) != null) {
                return ResponseMsg.error("敏感词已存在 :" + name);
            }
            sensitiveWord.setSensitiveWord(name);
            sensitiveWord.setType(type);
            int insert = sensitiveWordMapper.insert(sensitiveWord);
            if (insert != 1) {
                return ResponseMsg.error();
            }
        }
        return ResponseMsg.successMsg("新增成功");
    }

    public ResponseMsg update(JSONObject jsonObject) {
        Integer id = jsonObject.getInteger("id");
        SensitiveWord byParamKey = sensitiveWordMapper.selectByParamKey(id);
        if (byParamKey == null) {
            return ResponseMsg.error("敏感词不存在");
        }
        Integer type = jsonObject.getInteger("type");
        String sensitiveWord = jsonObject.getString("sensitiveWord");
        byParamKey.setType(type);
        byParamKey.setSensitiveWord(sensitiveWord);
        int i = sensitiveWordMapper.updateByPrimaryKeySelective(byParamKey);
        if (i != 1) {
            return ResponseMsg.error("修改失败");
        }
        return ResponseMsg.successMsg("修改成功");
    }

    public ResponseMsg delete(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("ids");
        if (jsonArray == null) {
            return ResponseMsg.error("参数有误");
        }
        List<Integer> ids = jsonArray.toJavaList(Integer.class);
        int update = sensitiveWordMapper.updateDeleteFlag(ids);
        if (update > 1) {
            return ResponseMsg.success();
        }
        return ResponseMsg.error();
    }

    public ResponseMsg checkSensitive(SensitiveDto sensitiveDto) {
        String text = sensitiveDto.getText();
        String gameId = sensitiveDto.getGameId();
        if ("".equals(text) || text.length() < 1) {
            return ResponseMsg.error("参数有误");
        }
        if (!GAME_MAP.containsKey(Long.valueOf(sensitiveDto.getGameId()))) {
            return ResponseMsg.error("参数有误");
        }
        log.info("param:{}", sensitiveDto);
        Map<String, Object> re = null;
        String rel = null;
        try {
            //精确匹配
            rel = WordFilter.precise(text, Integer.valueOf(gameId));

            //模糊匹配
            re = WordFilter.doFilter(rel, Integer.valueOf(gameId));

        } catch (Exception e) {
            log.error("敏感词解析异常 ", e);
            return ResponseMsg.error("敏感词解析异常");
        }
        String result = (String) re.get("result");
        log.info(" 模糊匹配  result:{}", result);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("isContains", !text.equals(result));
        return ResponseMsg.successData(jsonObject);
    }


    public ResponseMsg refreshSensitiveWord() {
        try {
            GAME_MAP = gameMapper.queryAll();
            WordFilter.init(sensitiveWordMapper.getKeyword(null, null));
            return ResponseMsg.successMsg("敏感词库,刷新成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMsg.error("刷新失败");
        }
    }
}
