package br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Classe que envia um email com o Spring Mail
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class EMailSender {
	
	private JavaMailSender javaMailSender;
	
	public EMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendEmail(MailToSend mailToSend) {
        SimpleMailMessage msg = new SimpleMailMessage();
        StringBuilder builder = new StringBuilder();
        builder.append("Drone ID: ").append(mailToSend.getDroneId()).append("\n");
        builder.append("Drone Temperature:").append(mailToSend.getDroneTemperatureReading()).append("\n");
        builder.append("Drone Humidity:").append(mailToSend.getDroneHumidityReading()).append("\n");
        msg.setTo("ceduardo.roque@gmail.com");
        msg.setSubject("Drone Reading Alarm Mail");
        msg.setText(builder.toString());
        javaMailSender.send(msg);
    }
	
}
