package TFG.Terranaturale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "Entity")
public class TerranaturaleApplication {
	public static void main(String[] args) {
		SpringApplication.run(TerranaturaleApplication.class, args);
	}
}
