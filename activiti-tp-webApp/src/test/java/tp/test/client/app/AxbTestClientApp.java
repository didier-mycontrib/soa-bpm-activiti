package tp.test.client.app;

import generic.ws.util.client.DynReflectSoapClient;

public class AxbTestClientApp {

	public static void main(String[] args) {
		DynReflectSoapClient dynSoapClient = new DynReflectSoapClient();
		double a=2,x=3,b=8;
		double res = (Double)
		dynSoapClient.dynSoapCall("http://localhost:8080/activiti-soa-webApp/services/axb",
				                  "tp.process.services.AxbPortType", 
				                  "axb", a,x,b);
		System.out.println("axb(" + a + "," + x + "," + b +")=" + res);
	}

}
