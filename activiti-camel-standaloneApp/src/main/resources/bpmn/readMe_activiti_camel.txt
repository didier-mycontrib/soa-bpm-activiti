Envoi  du processus bpmn/activiti vers camel:
=============================================

  <serviceTask id="sendViaCamel" name="Send subRequest via camel" activiti:type="camel"></serviceTask>
  où activiti:type="camel" et où l'id du serviceTask correspond à la fin de l'URI de camel:
  .to("activiti:myProcess:sendViaCamel").
  
  NB: si le modeleur activiti/bpmn d'eclipse n'est pas capable de paramétrer  activiti:type="camel"
  il faut alors effectuer le paramétrage via l'éditeur "xml" (open with / xml editor) .
  
  Si le camelContext a un id différent de la valeur par défaut "camelContext", on peut
  alors le préciser via un élément d'extension:
  
  <serviceTask id="serviceTask1" activiti:type="camel">
  <extensionElements>
    <activiti:field name="camelContext" stringValue="customCamelContext" />
  </extensionElements>
</serviceTask>



Reception (entrante dans le processus bpmn/activiti) après un envoi depuis "camel" 
==================================================================================

  <receiveTask id="receiveNotif" name="receive Notif"></receiveTask>
  où l'id du "receiveTask" correspond à la fin de l'URI de camel: 
  .to("activiti:myProcess:receiveNotif")
  
  
Comportement paramétrable des échanges entre "activiti" et "camel" (en envoi de activiti vers camel):
=======================================================================================  
classes						finURIs							comportements
CamelBehaviorDefaultImpl	copyVariablesToProperties		Copy Activiti variables as Camel properties
CamelBehaviorCamelBodyImpl	copyCamelBodyToBody				Copy only Activiti variable named "camelBody" as camel message body
CamelBehaviorBodyAsMapImpl	copyVariablesToBodyAsMap		Copy all the Activiti variables in a map as Camel message body

paramétrage du coté activiti (à l'intérieur de <serviceTask> ) :
<extensionElements> 
<activiti:field  name="camelBehaviorClass" 
          stringValue="org.activiti.camel.impl.CamelBehaviorCamelBodyImpl" /> 
</extensionElements>

paramétrage du coté camel:
from("activiti:asyncCamelProcess:serviceTaskAsync2?copyVariablesToBodyAsMap=true").


Comportement paramétrable des échanges entre "camel" et "activiti" (de "camel" vers "activiti" ):
===============================================================================================

fin d'Uri						Description (du comportement)
Default							If Camel body is a map, copy each element as Activiti variable, otherwise copy the whole Camel body as "camelBody" Activiti variable
copyVariablesFromProperties		Copy Camel properties as Activiti variables of the same name
copyCamelBodyToBodyAsString		like default, but if camel Body is not a map, first convert it to String and then copy it in "camelBody"
copyVariablesFromHeader			Additionally copy camel headers to Activiti variables of the same names

