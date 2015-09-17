package tp.camel.route;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.language.Simple;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReferencementCamelActivitiRouteBuilder extends RouteBuilder {
	
	private final Helper helper = new Helper();
	


	@Override
	public void configure() throws Exception {
		
		XmlJsonDataFormat xmlJsonFormatReferencement = new XmlJsonDataFormat();
		xmlJsonFormatReferencement.setEncoding("UTF-8");
		xmlJsonFormatReferencement.setRootName("referencement");//for xml from json
		
		XmlJsonDataFormat xmlJsonFormatVerifSiret = new XmlJsonDataFormat();
		xmlJsonFormatVerifSiret.setEncoding("UTF-8");
		xmlJsonFormatVerifSiret.setRootName("verifSiret");//for xml from json
	       
	        from("file:data/inRef")
	        .marshal(xmlJsonFormatReferencement) // convert xml to json
	            .setBody(method(helper,"getProcessVariablesForReferencement"))
	            .setProperty("PROCESS_KEY_PROPERTY", simple("${body['referencement']['siret']}"))
	           .to("activiti:processReferencement") //startEvent of "processReferencement" with or without message
	           .log("processReferencement started" );
	        
	        
	        from("activiti:processReferencement:servicetaskSendVerifSiret?copyVariablesToBodyAsMap=true")
	        .log("Sending VerifSiret from activi process: ${body}")
	        .setBody(simple("${body['verifSiret']}"))
	         .log("Sending extracted VerifSiret from activi process: ${body}")
	        .setHeader("CamelFileName", simple("verifSiretOut${body['siret']}.xml"))
	        .setBody(method(helper,"getJsonStringFromBodyJavaMap"))
	        .unmarshal(xmlJsonFormatVerifSiret) // convert json to xml
	        .to("file:data/outVerifSiret");
	        
	        from("file:data/inVerifSiret")
	         .marshal(xmlJsonFormatVerifSiret) // convert xml to json
            .setBody(method(helper,"getProcessVariablesForVerifSiret"))
            .setProperty("PROCESS_KEY_PROPERTY", simple("${body['verifSiret']['siret']}"))
           .to("activiti:processReferencement:receivetaskVerifSiret") 
            .log("Process to handle incoming result of VerifSiret has been notified" );
	        
	        from("activiti:processReferencement:servicetaskSendNotifRef?copyVariablesToBodyAsMap=true")
	        .log("sending notification from activi process: ${body}")
	        .setBody(simple("${body['referencement']}"))
	        .setHeader("CamelFileName", simple("notificationReferencement${body['siret']}.xml"))
	        .setBody(method(helper,"getJsonStringFromBodyJavaMap"))
	        .unmarshal(xmlJsonFormatReferencement) // convert json to xml
	        .to("file:data/outNotif");
	     
	      
	        
           
		
	}
	
	/*
     * A few helper methods used for routing
     */
    public static final class Helper {
    	
    	private Map<String,String> convertJsonStringToJavaMap(String jsonString){
    	Map<String,String> javaMap = null; 
    	ObjectMapper jsonMapper = new ObjectMapper();
    	      	
    	try {
    		javaMap = jsonMapper.readValue(jsonString, new TypeReference<HashMap<String,String>>(){});
		} catch (Exception e) {
				//e.printStackTrace();
			javaMap = new HashMap<String,String>();
			javaMap.put("execption", e.getMessage());
		}
    	return javaMap;
    	}
    	
    	private String convertJavaMapToJsonString(Map javaMap){
        	String jsonString = "";
        	ObjectMapper jsonMapper = new ObjectMapper();
        	      	
        	try {
        		jsonString = jsonMapper.writeValueAsString(javaMap);
    		} catch (Exception e) {
    				//e.printStackTrace();
    			jsonString = "{ 'exception' : '"+  e.getMessage() +"' }";
    		}
        	return jsonString;
        	}
		
    	 @Handler
    	public String getJsonStringFromBodyJavaMap(@Body Map javaMap){
    		return convertJavaMapToJsonString(javaMap);
    	}

        /*
         * This method will extract information from the Exchange (using Camel annotations) and put them in a
         * Map that will be used for setting up the process' variables.
         */
        @Handler
        public Map getProcessVariablesForReferencement(@Body String body,
                                       @Simple("${date:now:yyyy-MM-dd kk:mm:ss}") String timestamp) {
            Map<String, Object> variables = new HashMap<String, Object>();
            //variables.put("messageReferencement", body);
            Map<String, String> referencement = null;
            referencement=convertJsonStringToJavaMap(body);
            /*
            referencement = new HashMap<String, Object>();
            referencement.put("siret", "123456789");
            referencement.put("raisonSociale", "ABCDEF");
            referencement.put("email", "user1@localhost");
            referencement.put("statut", "?");
            */
            variables.put("referencement", referencement);
            variables.put("timestamp", timestamp);
            return variables;
        }
        
        @Handler
        public Map getProcessVariablesForVerifSiret(@Body String body,
                                       @Simple("${date:now:yyyy-MM-dd kk:mm:ss}") String timestamp) {
            Map<String, Object> variables = new HashMap<String, Object>();
            //variables.put("messageVerifSiret", body);
            Map<String, String> verifSiret = null;
            verifSiret=convertJsonStringToJavaMap(body);
            /*
            verifSiret = new HashMap<String, Object>();
            verifSiret.put("siret", "123456789");
            verifSiret.put("raisonSociale", "ABCDEF");
            verifSiret.put("validite", "ok");
            */
            variables.put("verifSiret", verifSiret);
            variables.put("timestamp", timestamp);
            return variables;
        }
    }

}
