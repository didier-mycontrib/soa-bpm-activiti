package tp.test;
import generic.bpm.ProcessManager;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tp.test.activiti.pers.Personne;


public class DirectActivitiTestApp {

	public static void main(String[] args) {
		try{
			ClassPathXmlApplicationContext applicationContext = 
				    new ClassPathXmlApplicationContext("service-activiti-spring.xml");
					
			ProcessManager processManager = (ProcessManager) applicationContext.getBean("activitiBpmProcessManager");
			
			processManager.deployProcessDefinition("activiti_incr_pers_age_via_ws.bpmn");
			
			Map<String,Object> params = new HashMap<String,Object>();
			//NB: each param/variable (with all sub-objects) must be serializable !!!
			//params.put("username","toto");	
			//params.put("age",new Integer(12));
			params.put("pers", new Personne("toto",44L));
			
			String processInstanceId=processManager.startProcessInstance("incrementAgeWithWsCalculateur", params);
			System.out.println("For activiti_incr_pers_age_via_ws.bpmn , started (with spring) new processInstanceId="+processInstanceId);

	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	/*
	
	public static void main_v2_(String[] args) {
		try{
			ClassPathXmlApplicationContext applicationContext = 
				    new ClassPathXmlApplicationContext("springContext.xml");
			RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			RuntimeService activitiRuntimeService = (RuntimeService) applicationContext.getBean("runtimeService");
			String deploymentId = repositoryService
			  .createDeployment()
			  .addClasspathResource("test_activiti_with_props.bpmn")
			  .deploy()
			  .getId();
			
			Map<String,Object> params = new HashMap<String,Object>();
			//NB: each param/variable (with all sub-objects) must be serializable !!!
			//params.put("username","toto");	
			//params.put("age",new Integer(12));
			params.put("pers", new Personne("toto",44L));
			 ProcessInstance activitiProcessInstance = activitiRuntimeService.startProcessInstanceByKey("myProcess", params);
				
				// dès le démarrage ---> initialisation dans eventuel ActivitiStartProcessListener
			    // si défini dans .bpmn
				String processInstanceId=activitiProcessInstance.getId();
				System.out.println("started (with spring) new processInstanceId="+processInstanceId);

	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	public static void mainWithoutSpring(String[] args) {
		try{
		Map<String,Object> params = new HashMap<String,Object>();
		//NB: each param/variable (with all sub-objects) must be serializable !!!
		//params.put("username","toto");	
		//params.put("age",new Integer(12));
		params.put("pers", new Personne("toto",44L));
		//params.put("aCtx", new SimpleActivitiContext()); //now done via StartListener
					
		// Create Activiti process engine
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		
		  // Get Activiti services
		  RepositoryService repositoryService = processEngine.getRepositoryService();
		  RuntimeService activitiRuntimeService = processEngine.getRuntimeService();
		  
		  // Deploy the process definition
		  repositoryService.createDeployment()
		    .addClasspathResource("test_activiti_with_props.bpmn")
		    .deploy();			
		
		

		  ProcessInstance activitiProcessInstance = activitiRuntimeService.startProcessInstanceByKey("myProcess", params);
	
		// le démarrage ---> initialisation dans ActivitiStartProcessListener
		String processInstanceId=activitiProcessInstance.getId();
		System.out.println("started new processInstanceId="+processInstanceId);
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
*/

}
