package n2ru.mailer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@ComponentScan
@Import({RabbitConfiguration.class, SendGridConfiguration.class})
public class Application {

	public static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
    	
        SpringApplication.run(Application.class, args);
        System.out.println("Loglevel="+System.getenv("LOGGING_LEVEL"));
        LOG.info("===== Mailer  Started =======");
    }
}