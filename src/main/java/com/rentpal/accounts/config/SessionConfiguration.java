package com.rentpal.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * This class helps to establish a connection to redis database that helps to store user session information.
 */

@Configuration
@EnableRedisHttpSession
public class SessionConfiguration extends AbstractHttpSessionApplicationInitializer {

    /**
     * Creates a redis connection using Jedis
     *
     * @return the jedis connection factory
     */
    @Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }
}
