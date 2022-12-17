package ekp.chatbot;

//This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)

//You need to add the following Apache HTTP client libraries to your project:
//httpclient-4.5.3.jar
//httpcore-4.4.6.jar
//commons-logging-1.2.jar

import legion.luis.LuisProxy;

public class EkpLuisProxy extends LuisProxy {
	private final static String AUTH_KEY = "58c4d495240648bdb1634d77b87fa06d";
	// String appId = "df67dcdb-c37d-46af-88e1-8b97951ca1c2";
	private final static String APP_ID = "6cd2bbba-705c-4d03-b7b1-ea384b628022"; // EK
	private final static String VERSION_ID = "0.1";

	protected EkpLuisProxy() {
		super(AUTH_KEY, APP_ID, VERSION_ID);
	}

	private final static EkpLuisProxy INSTANCE = new EkpLuisProxy();

	public final static EkpLuisProxy getInstance() {
		return INSTANCE;
	}
}
