package com.tlgc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "Sync")
public class SyncUrlConfig {
    private String introUrl;
    private String gtUrl;
}