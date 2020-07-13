package cn.modo.sensitive.service;

import cn.modo.sensitive.dao.SensitiveWordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yjb
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeywordService {

    private final SensitiveWordMapper sensitiveWordMapper;





}
