package n2ru.mailer;

import org.springframework.stereotype.Service;

@Service
public class TextHelper {

	public static final String[] NOT_ALLOWED_SUFFIXES = new String[] { ".", ",", "!", ":", ";", "_", "-" };

	public String pruneSubject(String subject) {
		if (subject == null) {
			throw new IllegalArgumentException("Subject can't be null");
		}
		subject = subject.trim();

		if(subject.length()==0){
			return subject;
		}
		
		for (String suffix : NOT_ALLOWED_SUFFIXES) {
			if (subject.endsWith(suffix)) {
				return pruneSubject(subject.substring(0, subject.length() - 1));
			}
		}

		return subject;
	}

}
