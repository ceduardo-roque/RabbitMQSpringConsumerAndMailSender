package br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.consumers;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.mail.EMailSender;
import br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.mail.MailToSend;

/**
 * Componente Spring que representa o Consumidor da fila do RabbitMQ e cria os Spring Beans necessários para se conectar ao 
 * AMQP Server, ao SMTP Mail. Nesta classe também está representado o método Listener que escuta a fila específica de envio de emails
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Component
public class EmailQueueConsumer {

	Logger logger = LoggerFactory.getLogger(EmailQueueConsumer.class);
    private Environment environment;
    
    /**
     * Construtor do Consumidor
     * @param environment O objeto Environment representado pelo arquivo de Propriedades da aplicação
     */
    public EmailQueueConsumer(Environment environment) {
		this.environment = environment;
    }
    
    /**
     * Método Bean do Spring que retorna uma instancia de JavaMailSender. Este objeto é utilizado para se conectar 
     * a um Servidor SMTP configurado no arquivo de propriedades
     * @return Uma instancia válida de uma implementação de JavaMailSender
     */
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		// Read props from properties file
		try {
			String smtpHost = environment.getProperty("spring.mail.host");
			Integer smtpPort = Integer.parseInt(environment.getProperty("spring.mail.port"));
			String smtpUserName = environment.getProperty("spring.mail.username");
			String smtpPassword = environment.getProperty("spring.mail.password");
			String protocol = environment.getProperty("spring.mail.protocol");
			String auth = environment.getProperty("spring.mail.properties.mail.smtp.auth");
			String tls = environment.getProperty("spring.mail.properties.mail.smtp.starttls.enable");
			
			// Create the JavaMailSenderImpl object
			try {
				    mailSender.setHost(smtpHost);
				    mailSender.setPort(smtpPort);
				    mailSender.setUsername(smtpUserName);
				    mailSender.setPassword(smtpPassword);
				  
				    // Other props
				    Properties props = mailSender.getJavaMailProperties();
				    props.put("mail.transport.protocol", protocol);
				    props.put("mail.smtp.auth", auth);
				    props.put("mail.smtp.starttls.enable", tls);
				    //props.put("mail.debug", "true");
			} catch (Exception e) {
				logger.error("Falha ao configurar MailSender");
				throw e;
			}
		} catch(NumberFormatException e) {
			logger.error("Erro ao ler arquivo de propriedades. Verifique os valores dos parâmetros de configuração do SMTP Server.");
			throw e;
		} catch(Exception e) {
			logger.error("Erro ao ler arquivo de propriedades");
			throw e;
		}
		
	    return mailSender;
	}
   
	/**
	 * Bean que retorna o objeto ConnectionFactory do RabbitMQ. Este objeto é responsável por criar a conexão com o provedor de serviço
	 * AMQP e garantir que a conexão fique estabelecida.
	 * @return O objeto ConnectionFactory utilizado para a conexão com o provedor de serviço AMQP
	 */
	@Bean
	public ConnectionFactory getConnection() {

		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host")); 
		connectionFactory.setUsername(environment.getProperty("spring.rabbitmq.username"));
		connectionFactory.setPassword(environment.getProperty("spring.rabbitmq.password"));
		connectionFactory.setVirtualHost(environment.getProperty("spring.rabbitmq.virtual-host"));
		connectionFactory.setRequestedHeartBeat(Integer.parseInt(environment.getProperty("spring.rabbitmq.requested-heartbeat")));
		connectionFactory.setConnectionTimeout(Integer.parseInt(environment.getProperty("spring.rabbitmq.connection-timeout")));

		return connectionFactory;
	}

	/**
	 * Método que representará a implementação do listener da Fila de emails do RabbitMQ
	 * @param messageData A string que representa o payload enviado pela fila do RabbitMQ
	 */
	@RabbitListener(queues = "#{queueName.getQueueToListen()}")
	public void processMailQueue(String payload) {
		this.logger.info("Mail Message Received from Queue: mail-queue - Payload: " + payload);
		this.logger.info("Enviando email para os destinatários configurados no arquivo de propriedades");
		new EMailSender(this.getJavaMailSender()).sendEmail(transformPayloadToMailObject(payload));
	}

	private MailToSend transformPayloadToMailObject(String payload) {
		MailToSend mailToSend = new MailToSend();
		BasicJsonParser parser = new BasicJsonParser();
		Map<String, Object> parseMap = parser.parseMap(payload);
		logger.info(parseMap.toString());
		mailToSend.setDroneId(String.valueOf(parseMap.get("droneId")));
		mailToSend.setDroneTemperatureReading(String.valueOf(parseMap.get("droneTemperatureReading")));
		mailToSend.setDroneHumidityReading(String.valueOf(parseMap.get("droneHumidityReading")));
		return mailToSend;
	}
}
