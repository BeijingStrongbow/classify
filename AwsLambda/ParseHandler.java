package com.classify_kstate.classify;

import java.util.ArrayList;
import java.util.List;

import org.parse4j.Parse;
import org.parse4j.ParseObject;
import org.parse4j.ParseQuery;

public class ParseHandler {
	public static int addPoints(String name, int numPoints){
		
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
    		
    		if(points >= 90){
    			Texting.sendGoodMessage(name, Integer.toString(points), "+1" + p.get("PhoneNumbers"));
    		}

    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		return points;
	}
	
	public static int subtractPoints(String name, int numPoints){
		
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
    		
    		if(points <= 10){
    			Texting.sendBadMessage(name, Integer.toString(points), "+1" + p.get("PhoneNumbers")); 
    		}
    	} catch (org.parse4j.ParseException e) {
    		e.printStackTrace();
    	}
		return points;
	}
	
	public static void addAssignment(String name, String date, int monthAsInt, int dayAsInt){
		
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseObject assignment = new ParseObject("Homework");
		name = processAssignmentName(name);
		
		assignment.put("Title", name);
		assignment.put("DueDate", date);
		assignment.put("Month", monthAsInt);
		assignment.put("DayOfMonth", dayAsInt);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Points");
    	query.whereNotEqualTo("PhoneNumbers", Integer.toString(1));
    	List<ParseObject> data;
    	
		try {
			data = query.find();
			List<String> phoneNumbers = new ArrayList<String>();
			for(ParseObject p : data){
				String phoneNumber = (String) p.get("PhoneNumbers");
				if(!phoneNumbers.contains(phoneNumber)){
					phoneNumbers.add(phoneNumber);
				}
			}
			
			Texting.sendAssignmentTexts(name, date, phoneNumbers);
			assignment.save();
		} catch (org.parse4j.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean removeAssignment(String name){
		Parse.initialize("qsJqqoI6URL9MTUSMNHfdjtz7yeVQVHTxQLyThmc", "YUtYFESPDjjFtgp5eMgvdLh1p2vvMVjXcxIpTxBF");
    	
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Homework");
		name = processAssignmentName(name.substring(0, 1).toUpperCase() + name.substring(1));
		query.whereEqualTo("Title", name);
    	List<ParseObject> assignment;
		
    	ParseQuery<ParseObject> queryPoints = ParseQuery.getQuery("Points");
    	query.whereNotEqualTo("PhoneNumbers", Integer.toString(1));
    	List<ParseObject> data;
    	
		try {
			assignment = query.find();
			
			for(ParseObject obj : assignment){
				obj.delete();
			}
			
			data = queryPoints.find();
			List<String> phoneNumbers = new ArrayList<String>();
			for(ParseObject p : data){
				String phoneNumber = (String) p.get("PhoneNumbers");
				if(!phoneNumbers.contains(phoneNumber)){
					phoneNumbers.add(phoneNumber);
				}
			}
			
			Texting.sendAssignmentTexts(name, null, phoneNumbers);
			return true;
		} catch (org.parse4j.ParseException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return false;
		} catch (NullPointerException ex){
			return false;
		}
	}
	
	private static String processAssignmentName(String name){
		StringBuilder output = new StringBuilder();
		String[] input = name.split(" ");
		
		for(int i = 0; i < input.length; i++){
			System.out.println(input[i]);
			if(input[i].equals("zero")){
				output.append(0 + " ");
			}
			else if(input[i].equals("one")){
				output.append(1 + " ");
			}
			else if(input[i].equals("two")){
				output.append(2 + " ");
			}
			else if(input[i].equals("three")){
				output.append(3 + " ");
			}
			else if(input[i].equals("four")){
				output.append(4 + " ");
			}
			else if(input[i].equals("five")){
				output.append(5 + " ");
			}
			else if(input[i].equals("six")){
				output.append(6 + " ");
			}
			else if(input[i].equals("seven")){
				output.append(7 + " ");
			}
			else if(input[i].equals("eight")){
				output.append(8 + " ");
			}
			else if(input[i].equals("nine")){
				output.append(9 + " ");
			}
			else if(input[i].equals("ten")){
				output.append(10 + " ");
			}
			else if(input[i].equals("eleven")){
				output.append(11 + " ");
			}
			else if(input[i].equals("twelve")){
				output.append(12 + " ");
			}
			else if(input[i].equals("thirteen")){
				output.append(13 + " ");
			}
			else if(input[i].equals("fourteen")){
				output.append(14 + " ");
			}
			else if(input[i].equals("fifteen")){
				output.append(15 + " ");
			}
			else if(input[i].equals("sixteen")){
				output.append(16 + " ");
			}
			else if(input[i].equals("seventeen")){
				output.append(17 + " ");
			}
			else if(input[i].equals("eighteen")){
				output.append(18 + " ");
			}
			else if(input[i].equals("nineteen")){
				output.append(19 + " ");
			}
			else if(input[i].equals("twenty")){
				output.append(20 + " ");
			}
			else{
				output.append(input[i] + " ");
			}
		}
		return output.toString();
	}
}
