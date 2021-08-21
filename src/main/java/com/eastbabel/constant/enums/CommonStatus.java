package com.eastbabel.constant.enums;

import lombok.Getter;

@Getter
public enum CommonStatus {
    SUCCEED("成功"), FAILED("失败");

    String desc;

    CommonStatus(String desc) {
        this.desc = desc;
    }
}