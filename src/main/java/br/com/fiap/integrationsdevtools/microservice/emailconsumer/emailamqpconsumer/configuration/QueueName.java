package br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.configuration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Classe que retorna dinamicamente o nome da fila a ser "escutada" a partir do arquivo de propriedades
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Component
public class QueueName {

    private Environment environment;

	public QueueName(Environment environment) {
		this.environment = environment;
    }
	
	public String getQueueToListen() {
		return this.environment.getProperty("spring.rabbitmq.template.default-receive-queue");
	}
	
}
