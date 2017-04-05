package com.classify_kstate.classify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
import com.amazon.speech.ui.Reprompt;

public class ClassifySpeechlet implements Speechlet{

	private static FirebaseHandler database = null;
	
	@SuppressWarnings("unused")
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {

		Intent i = request.getIntent();
		
		SpeechletResponse response = new SpeechletResponse();
		PlainTextOutputSpeech output = new PlainTextOutputSpeech();
		
		if(database == null){
			try{
				File certificate = new File(getClass().getClassLoader().getResource("classify-ee6f2-firebase-adminsdk-y88f3-fbb3a1160c.json").getFile());
				FileInputStream input = new FileInputStream(certificate);
				database = FirebaseHandler.getInstance(input, "https://classify-ee6f2.firebaseio.com");
			}
			catch(FileNotFoundException ex){
				output.setText("Couldn't connect to database!");
				response.setOutputSpeech(output);
				return response;
			}
		}
		
		Reprompt reprompt = new Reprompt();
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		repromptSpeech.setText("Sorry, I didn't quite get that. Please try again.");
		reprompt.setOutputSpeech(repromptSpeech);
		
		if(i.getName() != null && session.getAttribute("IntentName") == null){session.setAttribute("IntentName", i.getName());}
		if(i.getName().equals("AddPoints") || i.getName().equals("RemovePoints")){
			if(i.getSlot("StudentName").getValue() != null && session.getAttribute("StudentName") == null) {session.setAttribute("StudentName", i.getSlot("StudentName").getValue());}
			if(i.getSlot("Points").getValue() != null && session.getAttribute("Points") == null){session.setAttribute("Points", i.getSlot("Points").getValue());}
		}
		else if(i.getName().equals("AddAssignment")){
			if(i.getSlot("AssignmentName").getValue() != null && session.getAttribute("AssignmentName") == null){session.setAttribute("AssignmentName", i.getSlot("AssignmentName").getValue());}
			if(i.getSlot("DueDate").getValue() != null && session.getAttribute("DueDate") == null){session.setAttribute("DueDate", i.getSlot("DueDate").getValue());}
			if(i.getSlot("DueTime").getValue() != null && session.getAttribute("DueTime") == null){session.setAttribute("DueTime", i.getSlot("DueTime").getValue());}
		}
		else if(i.getName().equals("RemoveAssignment")){
			if(i.getSlot("AssignmentName").getValue() != null && session.getAttribute("AssignmentName") == null){session.setAttribute("AssignmentName", i.getSlot("AssignmentName").getValue());}
		}
		else if(i.getName().equals("GetPoints")){
			if(i.getSlot("StudentName").getValue() != null && session.getAttribute("StudentName") == null){session.setAttribute("StudentName", i.getSlot("StudentName").getValue());}

		}
		
		if(i.getName() != null){
			if(session.getAttribute("IntentName").equals("AddPoints")){
				
				String student = (String) session.getAttribute("StudentName");
				int points = 0;
				
				if(student == null){
					output.setText("Which student would you like to give points to?");
					return SpeechletResponse.newAskResponse(output, reprompt);
					
				}
				
				if(session.getAttribute("Points") == null){
					output.setText("How many points would you like to give?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				points = Integer.parseInt((String) session.getAttribute("Points"));
				
				output.setText("Alright, " + student + " now has " + database.addPoints(student, points) + " points");
			}
			else if(session.getAttribute("IntentName").equals("RemovePoints")){
				String student = (String) session.getAttribute("StudentName");
				int points = 0;
				
				if(student == null){
					output.setText("Which student would you like to remove points from?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				if(session.getAttribute("Points") == null){
					output.setText("How many points would you like to remove?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				points = Integer.parseInt((String) session.getAttribute("Points"));
				
				output.setText("Alright, " + student + " now has " + database.subtractPoints(student, points) + " points");
			}
			else if(session.getAttribute("IntentName").equals("RemoveAssignment")){
				if(session.getAttribute("AssignmentName") == null){
					output.setText("What assignment do you want to delete?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				if(database.removeAssignment((String) session.getAttribute("AssignmentName"))){
					output.setText("Deleted all assignments named " + session.getAttribute("AssignmentName"));
				}
				else{
					output.setText("Sorry, I couldn't delete the assignment");
				}
				response.setOutputSpeech(output);
				return response;
			}
			else if(session.getAttribute("IntentName").equals("GreatestPoints")){
				String student = database.getGreatestPoints();
				output.setText(student);
			}
			else if(session.getAttribute("IntentName").equals("GetPoints")){
				int points = database.getPoints((String) session.getAttribute("StudentName"));
				output.setText(session.getAttribute("StudentName") + " has " + points + " points");
			}
			else if(session.getAttribute("IntentName").equals("AddAssignment")){
				String date = (String) session.getAttribute("DueDate");
				String name = (String) session.getAttribute("AssignmentName");
				String time = (String) session.getAttribute("DueTime");
				
				if(name == null){
					output.setText("What's the name of this assignment?");
					return SpeechletResponse.newAskResponse(output, reprompt);

				}
				if(date == null){
					output.setText("When's this assignment due?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				if(name.substring(0,2).equals("a ")){
					name = name.substring(2);
				}
				
				name = name.substring(0,1).toUpperCase() + name.substring(1);
				
				String month = date.substring(5, 7);
				int monthAsInt = Integer.parseInt(month);
				String day = date.substring(8, 10);
				int dayAsInt = Integer.parseInt(day);
				
				try{
					switch(monthAsInt){
						case 1:
							month = "January";
							break;
						case 2:
							month = "February";
							break;
						case 3:
							month = "March";
							break;
						case 4:
							date = "April";
							break;
						case 5:
							month = "May";
							break;
						case 6:
							month = "June";
							break;
						case 7:
							month = "July";
							break;
						case 8:
							month = "August";
							break;
						case 9:
							month = "September";
							break;
						case 10:
							month = "October";
							break;
						case 11:
							month = "November";
							break;
						case 12:
							month = "December";
							break;
					}
					
					if(time == null || time.equals("no") || time.equals("nah bro")){
						database.addAssignment(dayAsInt, month, name);
						output.setText("Alright, I added " + name + " on " + date);
					}
					else{
						database.addAssignment(dayAsInt, month, time, name);
						output.setText("Alright, I added " + name + " on " + date + " at " + time);
					}
					
				}
				catch(NumberFormatException ex){
					output.setText("Please specify a specific date not a week or weekend");
				}
				
			}
			else{
				output.setText("I'm sorry, I didn't understand your request.");
			}
			response.setOutputSpeech(output);
		}
		
		response.setShouldEndSession(true);
		return response;
	}

	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		SpeechletResponse response = new SpeechletResponse();
		Reprompt reprompt = new Reprompt();
		speech.setText("Hi! I'm classify, your classroom assistant. Would you like to give or remove points, or add an assignment?");
		SpeechletResponse.newAskResponse(speech, reprompt);
		return response;
	}

	public void onSessionEnded(SessionEndedRequest arg0, Session arg1) throws SpeechletException {
		//nothing to do here
	}

	public void onSessionStarted(SessionStartedRequest arg0, Session arg1) throws SpeechletException {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		SpeechletResponse response = new SpeechletResponse();	
		speech.setText("Hi! I'm classify, your classroom assistant. Would you like to give or remove points, or add an assignment?");
		SpeechletResponse.newTellResponse(speech);	
	}	
}
