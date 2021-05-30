package kjm.linkverifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableMongoAuditing
@Slf4j
public class LinkverifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkverifierApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void insertToDatabase() {
//        opinionRepository.save(new Opinion(OpinionEnum.FRAUD));
//        opinionRepository.save(new Opinion(OpinionEnum.INDECENT_CONTENT));
//        opinionRepository.save(new Opinion(OpinionEnum.FAKE_NEWS));
//        opinionRepository.save(new Opinion(OpinionEnum.RELIABLE));
//        opinionRepository.save(new Opinion(OpinionEnum.SAFE));
//        opinionRepository.save(new Opinion(OpinionEnum.VIRUS));
//        opinionRepository.save(new Opinion(OpinionEnum.NEUTRAL));
    }
}