package tp.test.activiti.pers.bpm;

import generic.bpm.delegate.BpmContextWithAnnot;
import generic.bpm.delegate.ProcessDataAccess;
import generic.bpm.delegate.annot.Action;
import tp.test.activiti.pers.Personne;

public class PersBpmContext extends BpmContextWithAnnot {
	
	@Action("increment_age")
	public Object doIncrementAge(ProcessDataAccess pda, Object... args) {
		Object result=null;
		Personne p  = (Personne) pda.getVariable("pers");
		p.setAge(p.getAge()+1);
		//System.out.println("process instance id="+pda.getProcessInstanceId());
		return result;
	}

}
