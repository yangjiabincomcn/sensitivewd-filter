package cn.modo.sensitive.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensitiveWord {
    private Integer id;

    private String sensitiveWord;

    private String createTime;

    private String updateTime;

    private Integer type;

    private Integer deleteFlag;

}