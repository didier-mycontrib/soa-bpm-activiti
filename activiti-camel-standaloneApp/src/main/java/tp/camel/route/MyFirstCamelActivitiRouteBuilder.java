package tp.camel.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.Simple;

public class MyFirstCamelActivitiRouteBuilder extends RouteBuilder {
	
	private final Helper helper = new Helper();

	@Override
	public void configure() throws Exception {
	       
	        from("file:data/in")
	            .setBody(method(helper))
	            .setProperty("PROCESS_KEY_PROPERTY", simple("aa"))
	           .to("activiti:myProcess") //startEvent of "myProcess" with or without message
	            .log("Process to handle incoming file has been started" );
	        
	        from("file:data/notif")
            .setBody(method(helper))
            .setProperty("PROCESS_KEY_PROPERTY", simple("aa"))
           .to("activiti:myProcess:receiveNotif") 
            .log("Process to handle incoming file has been notified" );
	        
	        /*
	        from("activiti:myProcess:sendViaCamel")
            .log("Processing subRequest ${property.messageId} created on ${property.timestamp}")
            .log("  original message: ${property.message}");
            */
	        from("activiti:myProcess:sendViaCamel?copyVariablesToBodyAsMap=true")
	        .log("Processing subRequest ")
	        .convertBodyTo(String.class)
	        .to("file:data/outDefault");
	        
           
		
	}
	
	/*
     * A few helper methods used for routing
     */
    public static final class Helper {

        /*
         * This method will extract information from the Exchange (using Camel annotations) and put them in a
         * Map that will be used for setting up the process' variables.
         */
        @Handler
        public Map getProcessVariables(@Body String body,
                                       @Header(Exchange.FILE_NAME) String filename,
                                       @Simple("${date:now:yyyy-MM-dd kk:mm:ss}") String timestamp) {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("message", body);
            variables.put("messageId", filename);
            variables.put("timestamp", timestamp);
            return variables;
        }
    }

}
