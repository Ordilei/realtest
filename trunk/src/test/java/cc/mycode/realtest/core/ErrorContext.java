package cc.mycode.realtest.core;

import java.util.ArrayList;
import java.util.List;

public class ErrorContext {

	public static List<String> erros= new ArrayList<String>();

	public String putMessage(String string) {
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append("\n Expected Result :"+string+"\n");
		if(!erros.isEmpty()){
			stackTrace.append("List of errors \n *********************** \n");
		}

		for(String error:erros){
			stackTrace.append(error+"\n");
		}

		stackTrace.append("**********************\n");
		return stackTrace.toString();
	}

	public void putError(String error){
		erros.add(error);
	}

}