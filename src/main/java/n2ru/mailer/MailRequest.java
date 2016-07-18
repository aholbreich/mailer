package n2ru.mailer;

public class MailRequest {

	private String to;
	private String subject;
	private String body;
	private String contentType;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public String getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return "MailRequest [to=" + to + ", subject=" + subject + ", body=" + body +  ",contentType="+contentType+"]";
	}

}
