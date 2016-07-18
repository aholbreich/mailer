package n2ru.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class SendGridConfiguration {

	public static final Logger LOG = LoggerFactory.getLogger(SendGridConfiguration.class);
	
	@Bean
	public SendGrid createSendGrid() {
		String apiKey =  System.getenv("SENDGRID_API_KEY");
	    if(apiKey==null || apiKey.length() <5){
	    	LOG.warn("=====================================================");
	    	LOG.warn("==== Api key seem to be to short or null, KEY={} ==", apiKey);
	    	LOG.warn("=====================================================");
	    }
	    else{
	    	LOG.info("Api key used {}...", apiKey.substring(0, 5));
	    }
		SendGrid sg = new SendGrid(apiKey);
		return sg;
	}
	

}
