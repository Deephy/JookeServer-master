package org.dartmouth.setup;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class StartServer extends HttpServlet{
	
	
	
	
	public void init() throws ServletException{
		System.out.println("---- start the server-----");
		
		// start a thread
		EventCheckingThread eventThread = new EventCheckingThread();
		eventThread.start();
	}

}
