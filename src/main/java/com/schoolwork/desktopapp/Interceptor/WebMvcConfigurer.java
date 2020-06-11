package com.schoolwork.desktopapp.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 在springboot2.0.0之后，WebMvcConfigurerAdapter已经过时了
 * 会使用WebMvcConfigurer或者WebMvcConfigurationSupport替代
 *
 * @author wyj
 * @create 2019-06-01 21:48
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
//    @Bean
//    public HttpMessageConverter<String> responseBodyConverter(){
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        return converter;
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//
//        converters.add(responseBodyConverter());
//    }
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截器按照顺序执行,如果不同拦截器拦截存在相同的URL，前面的拦截器会执行，后面的拦截器将不执行
         */
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/Login");
        super.addInterceptors(registry);
    }
}
