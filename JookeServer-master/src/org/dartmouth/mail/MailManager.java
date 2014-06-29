package org.dartmouth.mail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.dartmouth.common.CommonUtils;
import org.dartmouth.common.GlobalVariables;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailManager {
	
	private JavaMailSender sender;
	private Long caseTime;
	
	public MailManager(JavaMailSender mSender, Long time){
		this.sender = mSender;
		this.caseTime = time;
	}
	
	/* Generate a */
	public void send(String emailTo) throws NoSuchAlgorithmException{
		
		// use time and email address as the seed
		String md5Email = CommonUtils.generateMD5(emailTo + caseTime);
		
		// Generate the temporary reset password url
		String url = GlobalVariables.EMAIL_SERVICE.EMAIL_RESETPWD_PAGE + md5Email;
//		
//		// Construct the email content
//		String message = GlobalVariables.EMAIL_SERVICE.EMAIL_MESSAGE + url + GlobalVariables.EMAIL_SERVICE.EMAIL_TEAM;
//		
//		SimpleMailMessage messageManager = new SimpleMailMessage();
//		messageManager.setFrom(GlobalVariables.EMAIL_SERVICE.EMAIL_FROM);
//		messageManager.setTo(emailTo);
//		messageManager.setSubject(GlobalVariables.EMAIL_SERVICE.EMAIL_SUBJECT);
//		messageManager.setText("<a href=\"http://www.baidu.com\">www.baidu.com</a>");
//		sender.send(messageManager);
//		
//		
		try {
			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			
			String htmlMsg = "<a href=\" "+ url + "\">"+ url +"</a>";
			String message = GlobalVariables.EMAIL_SERVICE.EMAIL_MESSAGE + htmlMsg + GlobalVariables.EMAIL_SERVICE.EMAIL_TEAM;
			mimeMessage.setContent(message, "text/html");
			messageHelper.setFrom(GlobalVariables.EMAIL_SERVICE.EMAIL_FROM);
			messageHelper.setTo(emailTo);
			messageHelper.setSubject(GlobalVariables.EMAIL_SERVICE.EMAIL_SUBJECT);
			sender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
