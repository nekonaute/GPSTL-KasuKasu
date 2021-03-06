package services.config;

import java.util.Properties;

import com.mongodb.DBObject;

import dao.config.configDB;

public class Config {
	public static Properties getMailConfigurationProperties(){
		try{
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			DBObject config = configDB.getMailConfig();
			Properties props = System.getProperties();
			props.setProperty("mail.smtp.host", config.get("host").toString());
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", config.get("port").toString());
			props.setProperty("mail.smtp.socketFactory.port", config.get("port").toString());
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "true");
			props.put("mail.store.protocol", "pop3");
			props.put("mail.transport.protocol", "smtp");
			return props;
		}catch(Exception e){
			System.out.println("Mail not configurated");
			return null;
		}
	}
	
	public static DBObject getMailConfiguration(){
		return configDB.getMailConfig();
	}
	
	public static void setMailConfiguration(String mail,String pass,String host,String port,String domain){
		 configDB.setMailConfig(mail, pass, host, port,domain);
	}
	
	public static String getDomain(){
		String config;
		try{
			config=configDB.getMailConfig().get("domain").toString();
			return config;
		}catch(Exception e){
			return "http://localhost:8080";
		}
	}
	
	public static void main(String[] args) {
		setMailConfiguration("kasukasufr@gmail.com","usakusak","smtp.gmail.com","465","http://localhost:8080");
		System.out.println(configDB.getMailConfig().get("domain").toString());
		setMailConfiguration("testing@gmail.com","blabla","smtp.gmail.com","465","http://monsite:8080");
		System.out.println(configDB.getMailConfig().get("domain").toString());
		System.out.println(configDB.getMailConfig().get("host").toString());
		System.out.println(configDB.getMailConfig().get("port").toString());
	}
}
