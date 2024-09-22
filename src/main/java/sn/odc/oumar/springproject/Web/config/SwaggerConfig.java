//package sn.odc.oumar.springproject.Web.config;
//
//import com.google.common.base.Predicate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.RequestHandler;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2) // Ou OAS_30 si tu préfères OpenAPI 3
//                .select()
//                .apis((Predicate<RequestHandler>) RequestHandlerSelectors.basePackage("sn.odc.oumar.springproject.Web.controller")) // Remplace par ton package
//                .paths((Predicate<String>) PathSelectors.any())
//                .build();
//    }
//}
