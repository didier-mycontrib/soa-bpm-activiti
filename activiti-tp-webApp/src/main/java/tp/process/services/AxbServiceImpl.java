package tp.process.services;

import generic.bpm.util.activiti.MyActivitiProcessManager;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

@WebService(endpointInterface="tp.process.services.AxbPortType")
public class AxbServiceImpl implements AxbPortType {
	
private MyActivitiProcessManager processManager;
	
    //injection method for Spring
	public void setProcessManager(MyActivitiProcessManager processManager) {
		this.processManager = processManager;
	}

	@Override
	public double axb(double a, double x, double b) {
		double res=0.0;
		// v1 (sans activiti et sans axbProcess.bpmn): res= a*x+b;
		// v2 (avec activiti et avec axbProcess.bpmn) - challenge purement technique :
		//processManager.deployProcessDefinition("bpmn/processAxb.bpmn"); préalablement déclenché via AutoBpmLoaderHelper (spring)
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("a", a); params.put("x", x); params.put("b", b);
		String processInstanceId=processManager.startProcessInstance("axbProcess", params);
		//attendre fin processus et recuperer "res"
		return res;
	}

}
