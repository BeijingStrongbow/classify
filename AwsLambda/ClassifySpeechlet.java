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

import java.text.ParseException;
import java.util.List;

import org.parse4j.Parse;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

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
				addParse(student, points);
			}
			else if(i.getName().equals("RemovePoints")){
				String student = i.getSlot("StudentName").getValue();
				int points = Integer.parseInt(i.getSlot("Points").getValue());
				output.setText("I took " + points + " points from " + student);
				subtractParse(student, points);
			}
			else if(i.getName().equals("AddAssignment")){
				String date = i.getSlot("DueDate").getValue();
				String name = i.getSlot("AssignmentName").getValue();
				if(name.substring(0,2).equals("a ")){
					name = name.substring(2);
				}
				
				name = name.substring(0,1).toUpperCase() + name.substring(1);
				String time = i.getSlot("DueTime").getValue();
				
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
					
					if(time == null){
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
					
					output.setText("Added assignment " + name + " on " + date);
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
	
	private void addParse(String name, int numPoints){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Points");
    	query.whereEqualTo("Name", name.substring(0, 1).toUpperCase() + name.substring(1));
    	List<ParseObject> person;

    	try {
    		person = query.find();
    		ParseObject p = person.get(0);
    		int oldPoints = p.getInt("Points");
    		if(oldPoints + numPoints <= 100){
    			p.put("Points", oldPoints + numPoints);
    			p.save();
    		}

    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		
	}
	
	private void subtractParse(String name, int numPoints){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Points");
    	query.whereEqualTo("Name", name.substring(0, 1).toUpperCase() + name.substring(1));
    	List<ParseObject> person;

    	try {
    		person = query.find();
    		ParseObject p = person.get(0);
    		int oldPoints = p.getInt("Points");
    		if(oldPoints - numPoints >= 0){
    			p.put("Points", oldPoints - numPoints);
        		p.save();
    		}
    		
    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		
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
}
