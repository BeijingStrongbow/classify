package com.classify_kstate.classify;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebsiteConnection {
	
	public static void main(String[] args){
		establishConnection();
	}
	
	public static void establishConnection(){
		try{
			Socket s = new Socket("66.96.162.149", 4444);
		}
		catch(UnknownHostException ex){
			System.out.println("Congratulations. Ya played yourself (with the ip).");
		}
		catch(IOException ex){
			System.out.println("Congratulations. Ya played yourself (with something that (probably) isn't the ip).");
		}
	}
	
}
