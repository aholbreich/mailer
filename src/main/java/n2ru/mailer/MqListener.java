package n2ru.mailer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@EnableRabbit
@Component
public class MqListener {

	private static final Email DEFAULT_FROM = new Email("info@n2ru.info");

	public static final Logger LOG = LoggerFactory.getLogger(MqListener.class);
	@Autowired
	private SendGrid sendGrid;

	@Autowired
	private TextHelper textHelper;

	// {"to":"test@test.land", "subject":"test subject", "body":"test Body", "contentType":"text/plain"}
	@RabbitListener(queues = "mailOutQueue")
	public void processQueue1(@Payload MailRequest request) {
		LOG.info("Received from queue {}", request);
		String devTO = System.getenv("SENDGRID_DEV_TO");
		if (devTO != null) {
			LOG.warn("Overriding TO by {}", devTO);
			request.setTo(devTO);
		}
		sendEmail(request);
	}

	public void sendEmail(MailRequest request) {

		Content content = new Content(checkAndGetContentType(request.getContentType()), request.getBody());
		Mail mail = new Mail(DEFAULT_FROM, textHelper.pruneSubject(request.getSubject()), new Email(request.getTo()), content);
		includeBCCIfIProvided(mail);
		sendToSendGrid(mail);
	}

	private void includeBCCIfIProvided(Mail mail) {
		String devBCC = System.getenv("SENDGRID_DEV_BCC");
		Personalization personalization = mail.personalization.get(0);
		if (devBCC != null && personalization != null) {
			LOG.warn("Including BCC {}", devBCC);
			personalization.addBcc(new Email(devBCC));

		}
	}

	private String checkAndGetContentType(String ct) {
		String result = "text/plain";
		if (ct != null) {
			try {
				MediaType.parseMediaType(ct);
				result = ct;
			} catch (InvalidMediaTypeException e) {
				LOG.warn("Not parsable Media Type {}", ct);
			}
		}
		LOG.info("Using ContentType:{}", result);
		return result;
	}

	private void sendToSendGrid(Mail mail) {
		Request apiRequest = new Request();
		try {
			apiRequest.method = Method.POST;
			apiRequest.endpoint = "mail/send";
			apiRequest.body = mail.build();
			Response response = sendGrid.api(apiRequest);

			LOG.info("Email was send. Return StatusCode {}", response.statusCode);
			LOG.debug("Response Headers {}, Body {} ", response.headers, response.body);

		} catch (IOException e) {
			LOG.error("Something got wrong", e);
		}
	}

}