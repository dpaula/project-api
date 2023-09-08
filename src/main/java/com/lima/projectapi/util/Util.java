package com.lima.projectapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Configuration
public class Util {

    public static String LOG_PREFIX;

    public static final ZoneId ZONA_ID = ZoneId.of("America/Sao_Paulo");

    @Value("${server.undertow.accesslog.prefix}")
    public void setLogPrefix(final String logPrefix) {
        Util.LOG_PREFIX = logPrefix;
    }

    public static LocalDateTime getDataAtualDateTime() {
        return LocalDateTime.now(ZONA_ID);
    }
}
