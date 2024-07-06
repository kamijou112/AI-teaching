package com.cx.aischeduledtasks.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Component
@ConfigurationProperties(prefix = "kd.web")
public class KdWebProperties {
    private String appId;
    private String appSecret;
    private String appKey;
}
