package tp.inscription.bpm;

import generic.bpm.delegate.BpmContextWithAnnot;
import generic.bpm.delegate.ProcessDataAccess;
import generic.bpm.delegate.annot.Action;
import generic.util.interaction.AsyncUserInteraction;
import generic.util.interaction.mail.LocalMailUserInteraction;
import tp.inscription.data.Inscription;
import tp.inscription.data.VerifEmail;

public class InscriptionBpmContext extends BpmContextWithAnnot {

	@Action("display")
	public Object doActionWithDataAccess(ProcessDataAccess pda,Object... args) {
		for(Object obj:args)
			System.out.print(obj.toString()+" ");
		System.out.print("\n");
		return null;
	}
	
	@Action("verifEmail")
	public Object doVerifEmail(ProcessDataAccess pda, Object... args) {
		Object result=null;
			Inscription inscription   = (Inscription) pda.getVariable("inscription");
			
			VerifEmail verifEmail = new VerifEmail();
			verifEmail.setEmail(inscription.getEmail());
			verifEmail.setNumInscription(inscription.getNumInscription());
			
			pda.setVariable("verifEmail", verifEmail);
			
			//simulation:
			System.out.println("envoi d'un mail de verification vers " + verifEmail.getEmail());
			//véritable envoi:
			AsyncUserInteraction asyncUserInteraction = new LocalMailUserInteraction();
			asyncUserInteraction.sendRequestMsg("verification email valide" /*subject*/, 
					"veuillez cliquer sur le lien hypertexte suivant pour confirmer votre email et votre inscription"/*message*/, 
					verifEmail.getEmail() /*requestAddress*/,
					pda.getProcessInstanceId()/*verifEmail.getNumInscription().toString()*//*corrData*/,
					"http://localhost:8080/activiti-webApp/services/rest/emailCheckedCallback/notifyEmailOk"/*responseUrl*/,
					"no-reply"/*from*/);
		return result;
	}


}

