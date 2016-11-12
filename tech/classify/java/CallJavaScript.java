package tech.classify.java;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CallJavaScript {
	
	public static void main(String[] args){
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine scriptEngine = manager.getEngineByName("nashorn");
		
		try{
			scriptEngine.eval(new FileReader("script.js"));
		}
		catch(FileNotFoundException ex){
			System.out.println("File not found!");
		}
		catch(ScriptException ex){
			System.out.println(ex.toString());
		}
	}
	
}
