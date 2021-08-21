package com.eastbabel.bo.base;

import com.eastbabel.constant.enums.CommonStatus;
import com.eastbabel.utils.RuntimeUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 9148654417530170978L;
    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("状态码")
    private int status = 200;
    @ApiModelProperty("错误信息")
    private String message;

    public ResponseEntity() {
    }

    public ResponseEntity(T data) {
        this.data = data;
    }

    public static ResponseEntity<String> succeed() {
        return new ResponseEntity<>(CommonStatus.SUCCEED.name());
    }

    public static <T> ResponseEntity<T> nothing() {
        return new ResponseEntity<>(null);
    }

    public static <T> ResponseEntity<T> ok(T data) {
        return new ResponseEntity<T>(data);
    }

    @ApiModelProperty("实例名称")
    public String getInstance() {
        return RuntimeUtils.getHostName();
    }
}
