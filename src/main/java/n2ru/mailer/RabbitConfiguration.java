package n2ru.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

@EnableRabbit
public class RabbitConfiguration {

	public static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private String password;
	private String user;

	@Bean
	public ConnectionFactory connectionFactory() {

		CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
		factory.setUsername(getUser());
		factory.setPassword(getPassword());

		return factory;
	}

	private String getUser() {
		if (this.user == null) {
			this.user = System.getenv("RABBIT_USER");
			if (this.user == null) {
				LOG.warn("=====================================================");
				LOG.warn("=== Rabbit: User not set");
				LOG.warn("=====================================================");
			} else {
				LOG.info("Rabbit user:{}", user);
			}
		}
		return this.user;
	}

	private String getPassword() {
		if (this.password == null) {
			this.password = System.getenv("RABBIT_PASSWORD");
			if (this.password == null) {
				LOG.warn("=====================================================");
				LOG.warn("=== Rabbit: Password not set");
				LOG.warn("=====================================================");
			} else {
				LOG.info("Rabbit password with lenght:{}", this.password.length());
			}
		}
		return this.password;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrentConsumers(1);
		factory.setMaxConcurrentConsumers(1);
		factory.setMessageConverter(new Jackson2JsonMessageConverter());

		return factory;
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}

}