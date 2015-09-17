package tp.process.services;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface AxbPortType {
	
	public double axb(@WebParam(name="a")double a,
			          @WebParam(name="x")double x,
			          @WebParam(name="b")double b); //compute and return a*x+b

}
