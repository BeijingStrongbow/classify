package com.classify_kstate.classify;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

public class ClassifySpeechlet implements Speechlet{

	public SpeechletResponse onIntent(IntentRequest arg0, Session arg1) throws SpeechletException {
		Intent i = arg0.getIntent();
		
		SpeechletResponse response = new SpeechletResponse();
		PlainTextOutputSpeech output = new PlainTextOutputSpeech();
		if(i.getName() != null){
			if(i.getName().equals("AddPoints")){
				String student = i.getSlot("StudentName").getValue();
				int points = Integer.parseInt(i.getSlot("Points").getValue());
				output.setText("I gave " + points + " points to " + student);
			}
			else if(i.getName().equals("RemovePoints")){
				String student = i.getSlot("StudentName").getValue();
				int points = Integer.parseInt(i.getSlot("Points").getValue());
				output.setText("I took " + points + " points from " + student);
			}
			/*else if(i.getName().equals("AddAssignment")){
				String date = i.getSlot("DueDate").getValue();
				String name = i.getSlot("AssignmentName").getValue();
			}*/
			else{
				output.setText("I'm sorry, I didn't understand your request.");
				response.setShouldEndSession(true);
			}
			
			response.setOutputSpeech(output);
		}
		
		return response;
	}

	public SpeechletResponse onLaunch(LaunchRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		return null;
	}

	public void onSessionEnded(SessionEndedRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		
	}

	public void onSessionStarted(SessionStartedRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		
	}
	
}
