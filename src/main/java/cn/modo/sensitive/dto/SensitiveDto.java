package cn.modo.sensitive.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yjb
 **/
@Data
public class SensitiveDto {
    @NotNull(message = "参数有误")
    private String text;
    @NotNull(message = "gameId not null ")
    private String gameId;
}
