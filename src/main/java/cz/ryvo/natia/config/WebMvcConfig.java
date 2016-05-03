package cz.ryvo.natia.config;

import cz.ryvo.natia.log.LoggingFilter;
import cz.ryvo.natia.log.LoggingFilter.Builder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean loggingFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoggingFilter(Builder.create().excludedPaths("/app")));
        return registration;
    }
}
