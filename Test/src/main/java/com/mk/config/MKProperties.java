package com.mk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "stk")
public class MKProperties {
    private String minuteBefore;
    private String minuteDate;
    private String linePath;
}
