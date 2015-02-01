package tp.inscription.service.rest;

import generic.bpm.ProcessEventManager;
import generic.bpm.ProcessMapUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/emailCheckedCallback/")
public class EmailCheckedCallbackRest {
	
	private ProcessEventManager processEventManager;
	
	//injection method for Spring
	public void setProcessEventManager(ProcessEventManager processEventManager) {
		this.processEventManager = processEventManager;
	}
	
	
	@GET
	@Path("/notifyEmailOk")
	// pour URL = http://localhost:8080/inscription-bpmn-web/services/rest/
                                //emailCheckedCallback/notifyEmailOk?corrData=1
	@Produces("text/plain")
	public String notifyEmailOk(@QueryParam("corrData")String corrData) {
		//String numInscription = corrData; //old version
		String processInstanceId=corrData;  //new version
		System.out.println("notifyEmailOk (via rest) for processInstanceId="+processInstanceId);
		
		processEventManager.messageEvent("inscription", processInstanceId, "emailCheckedOrNot", ProcessMapUtil.oneEntryMap("verifOk", Boolean.TRUE));
		return "ok, adresse email (et inscription) bien verifie";
	}

	

}
