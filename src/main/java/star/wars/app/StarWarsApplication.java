package star.wars.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarWarsApplication {

//	private static final Logger log = LoggerFactory.getLogger(StarWarsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StarWarsApplication.class, args);
	}

//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.build();
//	}

//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
//			Person person = restTemplate.getForObject(
//					"https://swapi.dev/api/people/1/", Person.class);
//			log.info(person.toString());
//		};
//	}

}
