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
import com.amazon.speech.ui.Reprompt;

import java.util.List;

import org.parse4j.Parse;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

public class ClassifySpeechlet implements Speechlet{

	@SuppressWarnings("unused")
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		
		Intent i = request.getIntent();
		
		SpeechletResponse response = new SpeechletResponse();
		PlainTextOutputSpeech output = new PlainTextOutputSpeech();
		
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
			if(i.getSlot("PromptTime").getValue() != null && session.getAttribute("PromptTime") == null){session.setAttribute("PromptTime", i.getSlot("PromptTime").getValue());}
		}
		else if(i.getName().equals("RemoveAssignment")){
			if(i.getSlot("Confirm").getValue() != null && session.getAttribute("Confirm") == null){session.setAttribute("Confirm", i.getSlot("Confirm").getValue());}
			if(i.getSlot("AssignmentName").getValue() != null && session.getAttribute("AssignmentName") == null){session.setAttribute("AssignmentName", i.getSlot("AssignmentName").getValue());}

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
				
				output.setText("Alright, " + student + " now has " + addParse(student, points) + " points");
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
				
				output.setText("Alright, " + student + " now has " + subtractParse(student, points) + " points");
				
			}
			else if(session.getAttribute("IntentName").equals("RemoveAssignment")){
				if(session.getAttribute("AssignmentName") == null){
					output.setText("What assignment do you want to delete?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				
				if(session.getAttribute("Confirm") == null){
					output.setText("Are you sure you want to delete " + (String) session.getAttribute("AssignmentName") + "?");
					return SpeechletResponse.newAskResponse(output, reprompt);
				}
				else if(session.getAttribute("Confirm").equals("yes")){
					if(removeAssignment((String) session.getAttribute("AssignmentName"))){
						output.setText("I deleted the assignment");
					}
					else{
						output.setText("Sorry, I couldn't delete the assignment");
					}
					response.setOutputSpeech(output);
					return response;
				}
				else{
					output.setText("okay");
					response.setOutputSpeech(output);
					return response;
				}
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
				
				if(time == null){
					if("yes".equals(session.getAttribute("PromptTime"))){
						output.setText("What time is that?");
						return SpeechletResponse.newAskResponse(output, reprompt);
					}
					else if(session.getAttribute("PromptTime") == null){
						output.setText("Is the assignment due at a specific time?");
						return SpeechletResponse.newAskResponse(output, reprompt);
					}
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
							date = "January " + day;
							break;
						case 2:
							date = "February " + day;
							break;
						case 3:
							date = "March " + day;
							break;
						case 4:
							date = "April " + day;
							break;
						case 5:
							date = "May " + day;
							break;
						case 6:
							date = "June " + day;
							break;
						case 7:
							date = "July " + day;
							break;
						case 8:
							date = "August " + day;
							break;
						case 9:
							date = "September " + day;
							break;
						case 10:
							date = "October " + day;
							break;
						case 11:
							date = "November " + day;
							break;
						case 12:
							date = "December " + day;
							break;
					}
					
					if(time == null || time.equals("no") || time.equals("nah bro")){
						time = "All day";
						date += ", " + time;
					}
					else if(Integer.parseInt(time.substring(0, 2)) > 12){
						time = (Integer.parseInt(time.substring(0, 2)) - 12) + time.substring(2) + " pm";
						date += ", " + time;
					}
					else{
						if(time.charAt(0) == '0'){
							time = time.substring(1);
						}
						
						time += " am";
						date += ", " + time;
					}
					
					output.setText("Alright, I added assignment " + name + " on " + date);
					assignmentParse(name, date, monthAsInt, dayAsInt);
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
	
	private int addParse(String name, int numPoints){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	int points = 0;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Points");
    	query.whereEqualTo("Name", name.substring(0, 1).toUpperCase() + name.substring(1));
    	List<ParseObject> person;
    	try {
    		person = query.find();
    		ParseObject p = person.get(0);
    		int oldPoints = p.getInt("Points");
    		if(oldPoints + numPoints <= 100){
    			p.put("Points", oldPoints + numPoints);
    			points = oldPoints + numPoints;
    			p.save();
    		}
    		else{
    			p.put("Points", 100);
    			points = 100;
    			p.save();
    		}
    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		return points;
	}
	
	private int subtractParse(String name, int numPoints){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	int points = 0;
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Points");
    	query.whereEqualTo("Name", name.substring(0, 1).toUpperCase() + name.substring(1));
    	List<ParseObject> person;

    	try {
    		person = query.find();
    		ParseObject p = person.get(0);
    		int oldPoints = p.getInt("Points");
    		if(oldPoints - numPoints >= 0){
    			p.put("Points", oldPoints - numPoints);
    			points = oldPoints - numPoints;
        		p.save();
    		}
    		else{
    			p.put("Points", 0);
    			p.save();
    		}
    		
    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		return points;
	}
	
	private void assignmentParse(String name, String date, int monthAsInt, int dayAsInt){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseObject assignment = new ParseObject("Homework");
		
		assignment.put("Title", name);
		assignment.put("DueDate", date);
		assignment.put("Month", monthAsInt);
		assignment.put("DayOfMonth", dayAsInt);
		try {
			assignment.save();
		} catch (org.parse4j.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private boolean removeAssignment(String name){
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseQuery query = ParseQuery.getQuery("Homework");
		query.whereEqualTo("Name", name.substring(0, 1).toUpperCase() + name.substring(1));
    	List<ParseObject> assignment;
		
		try {
			assignment = query.find();
			assignment.get(0).delete();
			return true;
		} catch (org.parse4j.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
