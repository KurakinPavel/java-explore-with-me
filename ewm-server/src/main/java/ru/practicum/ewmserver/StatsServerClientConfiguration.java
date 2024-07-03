package ru.practicum.ewmserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statserverclient.client.StatsServerClient;

@Configuration
public class StatsServerClientConfiguration {
    @Bean
    public StatsServerClient statsServerClient(@Value("${STAT_SERVER_URL}") String statisticServerUrl, RestTemplateBuilder builder) {
        return new StatsServerClient(statisticServerUrl, builder);
    }
}
