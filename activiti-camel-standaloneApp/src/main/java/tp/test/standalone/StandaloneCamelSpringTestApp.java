package tp.test.standalone;

import org.apache.camel.spring.Main;

public class StandaloneCamelSpringTestApp {
	
	private Main camelSpringMain; 

	public static void main(String[] args) {
		StandaloneCamelSpringTestApp myApp = new StandaloneCamelSpringTestApp();
        myApp.boot();
        
	}
	
	public void boot(){
		 // create a "org.apache.camel.spring.Main" instance: 
		camelSpringMain = new Main(); 
        // enable hangup support so you can press ctrl + c to terminate the JVM 
		camelSpringMain.enableHangupSupport(); 
		camelSpringMain.setApplicationContextUri("META-INF/spring/camel-context.xml"); 

        // run until you terminate the JVM 
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n"); 
        try {
			camelSpringMain.run();
			//no line after (blocked to run())
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
