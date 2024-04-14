package ssafy.ggame.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// 스웨거 추가
// swagger-ui/index.html로 접속가능
@OpenAPIDefinition(info = @Info(title = "짜르륵의 API 명세서",
                                description = "게임 추천 API 모음",
                                version= "v1"))
@Configuration
public class SwaggerConfig {
}
