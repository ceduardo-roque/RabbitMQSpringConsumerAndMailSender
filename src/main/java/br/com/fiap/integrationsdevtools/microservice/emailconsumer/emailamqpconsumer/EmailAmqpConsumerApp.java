package br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application que inicia o microserviço de envio de emails. Este microsserviço se conecta a um servidor AMQP e 
 * fica escutando uma fila específica que indica que um email deve ser enviado.
 * @author Carlos Eduardo Roque da Silva
 *
 */
@SpringBootApplication
public class EmailAmqpConsumerApp {
	
	/**
	 * Início da aplicação Spring Boot
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(EmailAmqpConsumerApp.class, args);
	}
}
