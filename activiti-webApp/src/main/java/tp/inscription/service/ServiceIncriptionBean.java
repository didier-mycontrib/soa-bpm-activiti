package tp.inscription.service;

import generic.bpm.ProcessManager;
import generic.bpm.ProcessMapUtil;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import tp.inscription.data.Inscription;

@WebService(endpointInterface="tp.inscription.service.ServiceInscription")
public class ServiceIncriptionBean implements ServiceInscription {
	
	private ProcessManager processManager;
	
    //injection method for Spring
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
	
	@Override
	public String traiterInscription(Inscription inscription) {
		//processManager.deployProcessDefinition("inscription_activiti.bpmn"); préalablement déclenché via AutoBpmLoader (spring)
		
		String processInstanceId=processManager.startProcessInstance("inscription", ProcessMapUtil.oneEntryMap("inscription", inscription));
		System.out.println("started (with spring) new processInstanceId="+processInstanceId);
		
		String msgAR = "A.R: demande d'inscription " + inscription.getNumInscription() + " bien recue , en attente de confirmation adresse email"; 
		return msgAR;
	}


	

}
