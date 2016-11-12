package com.classify_kstate.classify;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebsiteServerSocket {
	public static void establishConnection(){
		try{
			ServerSocket s = new ServerSocket(4444);
			Socket connection = s.accept();
			System.out.println("MAJOR key");
		}
		catch(IOException ex){
			System.out.println("Congratulations. Ya played yourself (on the server)");
		}
	}
}
