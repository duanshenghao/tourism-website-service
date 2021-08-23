package com.eastbabel.bo.email;

import lombok.Data;

import java.io.Serializable;

@Data
public class ToEmail implements Serializable {

    private String[] tos;

    private String subject;

    private String content;
}
