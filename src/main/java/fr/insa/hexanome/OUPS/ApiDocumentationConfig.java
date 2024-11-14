package fr.insa.hexanome.OUPS;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "(OU)PS API Documenation",
                version = "1.0",
                contact = @Contact(
                        name = "HexaTeams",
                        email = "thomas.gorisse@insa-lyon.fr"
                )

        )
)
public class ApiDocumentationConfig {
}
