package com.devops.projeto_ac2.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação automática da API
 * 
 * Acesso:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Alunos - AC2 DevOps")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de alunos desenvolvida com:
                                - **TDD** (Test-Driven Development)
                                - **DDD** (Domain-Driven Design)
                                - **Clean Architecture**
                                
                                ## Funcionalidades
                                - Cadastro de alunos
                                - Registro de tentativas (máx 3)
                                - Conclusão de curso com bônus
                                - Sistema de ranking
                                
                                ## Regras de Negócio
                                - Aprovado: média >= 7.0 (ganha 3 cursos extras)
                                - Excelência: média >= 9.0 (ganha 5 cursos extras)
                                - Reprovado: média < 5.0
                                - Em recuperação: 5.0 <= média < 7.0
                                - Limite de 3 tentativas por aluno
                                """)
                        .contact(new Contact()
                                .name("Equipe DevOps")
                                .email("devops@faculdade.edu.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Testes")
                ));
    }
}
