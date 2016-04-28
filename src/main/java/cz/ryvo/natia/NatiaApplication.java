package cz.ryvo.natia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class NatiaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(NatiaApplication.class, args);
	}

    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NatiaApplication.class);
	}
}