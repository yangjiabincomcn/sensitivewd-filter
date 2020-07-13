package cn.modo.sensitive.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Create by YJB on 2019/12/17-17:20
 **/
@Data
@Component
public class MainDatabaseUrl {


    @Value("${env}")
    private String env;

}
