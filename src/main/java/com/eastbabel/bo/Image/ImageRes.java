package com.eastbabel.bo.Image;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImageRes {
    @ApiModelProperty("图片key")
    private String imageKey;
    @ApiModelProperty("图片链接地址")
    private String imageUrl;
}
