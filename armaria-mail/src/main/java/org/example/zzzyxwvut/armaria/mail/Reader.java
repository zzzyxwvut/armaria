package org.example.zzzyxwvut.armaria.mail;

//import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class Reader
{
	@Autowired
	private Postmaster postmaster;

	private final Logger logger	= LogManager.getLogger();

	/* Note: "Re: " is optional according to RFC 5322, $3.6.5. */
//	private static final Pattern re	= Pattern.compile("Re: ",
//						Pattern.CASE_INSENSITIVE);

	public Reader() { }

	public void receive(MimeMessage message)
	{
		try {
			String subject	= message.getSubject();

			if (subject != null
					/*&& Reader.re.matcher(subject).lookingAt()*/)
				postmaster.readRepliedMessage(message);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
