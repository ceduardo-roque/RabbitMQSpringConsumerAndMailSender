package br.com.fiap.integrationsdevtools.microservice.emailconsumer.emailamqpconsumer.mail;

/**
 * POJO para aglutinar as infos do email a ser enviado
 * @author Carlos Eduardo Roque da Silva
 *
 */
public class MailToSend {

	private String droneId;
	private String droneTemperatureReading;
	private String droneHumidityReading;
	public String getDroneId() {
		return droneId;
	}
	public void setDroneId(String droneId) {
		this.droneId = droneId;
	}
	public String getDroneTemperatureReading() {
		return droneTemperatureReading;
	}
	public void setDroneTemperatureReading(String droneTemperatureReading) {
		this.droneTemperatureReading = droneTemperatureReading;
	}
	public String getDroneHumidityReading() {
		return droneHumidityReading;
	}
	public void setDroneHumidityReading(String droneHumidityReading) {
		this.droneHumidityReading = droneHumidityReading;
	}
	
	
	
}
