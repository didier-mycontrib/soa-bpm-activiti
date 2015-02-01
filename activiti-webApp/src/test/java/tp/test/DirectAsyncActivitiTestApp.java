package tp.test;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tp.async.jms.data.DemandeConges;


public class DirectAsyncActivitiTestApp {

	public static void main(String[] args) {
		try{
			ClassPathXmlApplicationContext applicationContext = 
				    new ClassPathXmlApplicationContext(new String[]{"service-activiti-spring.xml","jms-spring.xml"});
			RepositoryService repositoryService = (RepositoryService) applicationContext.getBean("repositoryService");
			RuntimeService activitiRuntimeService = (RuntimeService) applicationContext.getBean("runtimeService");
			String deploymentId = repositoryService
			  .createDeployment()
			  .addClasspathResource("test_async_activiti_jms.bpmn")
			  .deploy()
			  .getId();
			
			Map<String,Object> params = new HashMap<String,Object>();
			DemandeConges demandeConges = new DemandeConges();
			demandeConges.setNumDemande(1L);demandeConges.setIdEmploye("employe1");
			demandeConges.setPrenom("Alain"); demandeConges.setNom("Therieur");
			demandeConges.setDateDebut("2013-12-23");demandeConges.setDateFin("2014-01-03");
			params.put("demandeConges", demandeConges);
			 ProcessInstance activitiProcessInstance = 
					 activitiRuntimeService.startProcessInstanceByKey("myAsyncProcess", params);
				String processInstanceId=activitiProcessInstance.getId();
				System.out.println("test_async_activiti_jms.bpmn : started new processInstanceId="+processInstanceId);
          
		  System.out.println("5s de pause");
		  Thread.sleep(5*1000);		
		  
		  Map<String,Object> params2 = new HashMap<String,Object>();
		  DemandeConges demandeConges2 = new DemandeConges();
			demandeConges2.setNumDemande(2L);demandeConges2.setIdEmploye("employe2");
			demandeConges2.setPrenom("Alex"); demandeConges2.setNom("Therieur");
			demandeConges2.setDateDebut("2014-08-01");demandeConges2.setDateFin("2014-08-15");
			params2.put("demandeConges", demandeConges2);
			 ProcessInstance activitiProcessInstance2 = 
					 activitiRuntimeService.startProcessInstanceByKey("myAsyncProcess", params2);
				String processInstanceId2=activitiProcessInstance2.getId();
				System.out.println("test_async_activiti_jms.bpmn : started new processInstanceId="+processInstanceId2);
		  //System.out.println("fin du main");
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	

}
