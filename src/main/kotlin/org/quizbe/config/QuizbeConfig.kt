package org.quizbe.config

import org.quizbe.dao.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class QuizbeConfig : WebMvcConfigurer {
    var logger : Logger = LoggerFactory.getLogger(QuizbeConfig::class.java)

    @Bean
    fun primaryTemplateResolver(): ClassLoaderTemplateResolver {
        val primaryTemplateResolver = ClassLoaderTemplateResolver()
        primaryTemplateResolver.prefix = "templates/"
        primaryTemplateResolver.suffix = ".html"
        primaryTemplateResolver.templateMode = TemplateMode.HTML
        primaryTemplateResolver.characterEncoding = "UTF-8"
        primaryTemplateResolver.order = 1
        primaryTemplateResolver.checkExistence = true
        return primaryTemplateResolver
    } //
    //  @Bean
    //  public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
    //  TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
    //    @Override
    //    protected void postProcessContext(Context context) {
    //      SecurityConstraint securityConstraint = new SecurityConstraint();
    //      securityConstraint.setUserConstraint("CONFIDENTIAL");
    //      SecurityCollection collection = new SecurityCollection();
    //      collection.addPattern("/*");
    //      securityConstraint.addCollection(collection);
    //      context.addConstraint(securityConstraint);
    //    }
    //  };
    //  return tomcat;
    //  }
    //
    //  @Bean
    //  public SecurityWebFilterChain securityWebFilterChain(
    //          ServerHttpSecurity http) {
    //    return http.authorizeExchange()
    //            .pathMatchers("/actuator/**").permitAll()
    //            .anyExchange().authenticated()
    //            .and().build();
    //  }
}