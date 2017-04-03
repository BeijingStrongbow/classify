package tech.echoclassify.AlexaSkill;

import java.util.List;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Texting {
	private static final String ACCOUNT_SID = "ACbf8126ea1f615a890f506afff793b7f5";
	private static final String AUTH_TOKEN = "6e7ffb6c971918f36bdd591cf290bc9e";
	
	private static final String SEND_FROM = "+12138631089";
	
	public static void sendBadMessage(String name, String points, String phoneNumber){
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    Message.creator(new PhoneNumber(phoneNumber), 
	    		new PhoneNumber(SEND_FROM), 
	    		name + " isn't doing so well. They have " + points + " points out of 100")
	    		.create();
	}
	
	public static void sendGoodMessage(String name, String points, String phoneNumber){
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		

	    Message.creator(new PhoneNumber(phoneNumber), 
	    		new PhoneNumber(SEND_FROM), 
	    		name + " is awesome! They have " + points + " points out of 100!")
	    		.create();
	}
	
	public static void sendAssignmentTexts(final String name, final String dueDate, final List<String> phoneNumbers){
		for(String number : phoneNumbers){
			addAssignmentText(name, dueDate, number);
		}
	}
	
	private static void addAssignmentText(String name, String dueDate, String phoneNumber){
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		String message;
		
		if(dueDate == null){
			message = "You don't have to worry about " + name + " any more. Your/your student's teacher removed it!";
		}
		else{
			message = "There is a new assignment: " + name + ". It's due on " + dueDate + ".";
		}
		Message.creator(new PhoneNumber(phoneNumber), 
	    		new PhoneNumber(SEND_FROM), 
				message)
	    		.create();
	}
}
