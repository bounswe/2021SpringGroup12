package cmpe451.group12.beabee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BeabeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeabeeApplication.class, args);
	}

}
