package blibliwarmupproject.blogg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BloggApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggApplication.class, args);
	}

}
