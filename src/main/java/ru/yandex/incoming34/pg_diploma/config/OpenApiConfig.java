package ru.yandex.incoming34.pg_diploma.config;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootConfiguration
public class OpenApiConfig {

    public static final String TITLE = "Дипломный проект по курсу \"PostgreSQL для администраторов " +
            "баз данных и разработчиков.\" Автор: Айдинов Сергей";

    @Bean
    OpenAPI customOpenApi() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(info());
        return openAPI;
    }

    Info info() {
        return new Info()
                .title(TITLE)
                .description("Простой графический интерфейс для работы с базой данных")
                .version(componentVersion()).contact(contact());
    }

    Contact contact() {
        return new Contact().email("incoming34@yandex.ru").name("Sergei Aidinov");
    }

    @SuppressWarnings("deprecation")
    private String componentVersion() {
        final String propertiesFileName = "pom.xml";
        String componentVersion = "Версия не указана";
        List<Path> pathList = null;
        try (Stream<Path> files = Files.walk(Paths.get(System.getenv().get("PWD")))) {
            pathList = files
                    .filter(f -> f.getFileName().toString().equals(propertiesFileName))
                    .collect(Collectors.toList());
            System.out.println();

        } catch (IOException ignored) {
        }
        if (pathList.isEmpty()) return componentVersion;
        File file = new File(String.valueOf(pathList.get(0)));
        XmlMapper xmlMapper = new XmlMapper();
        try {
            JsonSchema jsonSchema = xmlMapper.generateJsonSchema(String.class);
            JsonSchema json = xmlMapper.readValue(file, jsonSchema.getClass());
            componentVersion = Objects.nonNull(json.getSchemaNode().get("version"))
                    ? String.valueOf(json.getSchemaNode().get("version")).replaceAll("\"", "")
                    : componentVersion;
        } catch (Exception e) {
            return componentVersion;
        }
        return componentVersion;
    }

}