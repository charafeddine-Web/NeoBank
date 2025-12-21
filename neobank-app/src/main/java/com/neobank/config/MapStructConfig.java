package com.neobank.config;

import com.neobank.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MapStructConfig {

    @Bean
    @Primary
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}
