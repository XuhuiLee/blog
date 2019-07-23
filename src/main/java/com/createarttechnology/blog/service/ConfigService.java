package com.createarttechnology.blog.service;

import com.createarttechnology.config.Config;
import com.createarttechnology.config.ConfigFactory;
import com.createarttechnology.config.ConfigWatcher;
import com.createarttechnology.logger.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 通过ConfigFactory读取配置
 * Created by lixuhui on 2018/11/13.
 */
@Service
public class ConfigService {

    private static final Logger logger = Logger.getLogger(ConfigService.class);

    private static final String CONFIG_FILE = "admin.properties";

    private String username;
    private String password;

    @PostConstruct
    public void init() {
        ConfigFactory.load(CONFIG_FILE, new ConfigWatcher() {
            @Override
            public void changed(Config config) {
                username = config.getString("username", "admin");
                password = config.getString("password", "password");
                logger.info("username={}, password={}", username, password);
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
