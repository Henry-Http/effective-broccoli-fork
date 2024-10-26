package com.nft.nftsite.utils;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class GeneralConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);

        return mapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        var LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        );
        return new Jackson2ObjectMapperBuilder().serializers(LOCAL_DATETIME_SERIALIZER);
    }

    @Bean
    public RestClient restClient() {
        RestTemplate template = new RestTemplate();
        return RestClient.create(template);
    }

}
