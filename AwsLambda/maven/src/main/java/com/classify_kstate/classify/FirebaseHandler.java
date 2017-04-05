package com.classify_kstate.classify;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.google.firebase.*;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHandler {

	private DatabaseReference pushAssignments;
	private DatabaseReference setPeople;
	
	private DatabaseReference readAssignments;
	private DatabaseReference readPeople;
	
	private int points;
	
	private final HashSet<String> phoneNumbers = new HashSet<String>();
	
	private static FirebaseHandler instance = null;
	
	public static FirebaseHandler getInstance(){
		if(instance == null){
			throw new NullPointerException("The database connection must be initialized! Use the other getInstance() overload!");
		}
		else{
			return instance;
		}
	}
	
	public static FirebaseHandler getInstance(InputStream credentials, String databaseUrl) throws NullPointerException{
		if(credentials == null){
			throw new NullPointerException("Couldn't find certificate file!");
		}
		
		if(instance == null){
			instance = new FirebaseHandler(credentials, databaseUrl);
		}
		
		return instance;
	}
	
	
	private FirebaseHandler(InputStream credentials, String databaseUrl){
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredential(FirebaseCredentials.fromCertificate(credentials))
				.setDatabaseUrl(databaseUrl)
				.build();
		
		FirebaseApp.initializeApp(options);
		
		pushAssignments = FirebaseDatabase.getInstance().getReference("Assignments").push();
		setPeople = FirebaseDatabase.getInstance().getReference("People");
		
		readAssignments = FirebaseDatabase.getInstance().getReference("Assignments");
		readPeople = FirebaseDatabase.getInstance().getReference("People");
		
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError arg0) {}

			public void onDataChange(DataSnapshot data) {
				for(DataSnapshot s : data.getChildren()){
					phoneNumbers.add(s.child("phoneNumber").getValue().toString());
				}
			}
			
		});
	}
	
	private static class Assignment{
		
		public int dayDue;
		public String monthDue;
		public String timeDue;
		public String title;
		
		private Assignment(int dayDue, String monthDue, String timeDue, String title){
			this.dayDue = dayDue;
			this.monthDue = monthDue;
			this.timeDue = timeDue;
			this.title = title;
		}
		
	}
	
	/**
	 * Give points to someone
	 * 
	 * @param name The person to give points to
	 * @param numPoints The number of points to give
	 * @return The number of points the person now has
	 */
	public int addPoints(final String name, final int numPoints){
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				points = Integer.parseInt(person.child("points").getValue().toString());
				points += numPoints;
				
				if(points > 100){
					points = 100;
				}
				
				if(points > 90){
					Texting.sendGoodMessage(name, Integer.toString(points), person.child("phoneNumber").getValue().toString());
				}
				
				person.getRef().child("points").setValue(points);
				latch.countDown();
			}
			
		});
		try{
			latch.await();
		}
		catch(InterruptedException ex){}

		return points;
	}
	
	/**
	 * Take points away from someone
	 * 
	 * @param name The person to take points from
	 * @param numPoints The number of points to take
	 * @return The number of points the person now has
	 */
	public int subtractPoints(final String name, final int numPoints){
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				points = Integer.parseInt(person.child("points").getValue().toString());
				points -= numPoints;
				
				if(points < 100){
					points = 0;
				}
				
				if(points < 10){
					Texting.sendBadMessage(name, Integer.toString(points), person.child("phoneNumber").getValue().toString());
				}
				
				person.getRef().child("points").setValue(points);
				latch.countDown();
			}
			
		});
		try{
			latch.await();
		}
		catch(InterruptedException ex){}

		return points;
	}
	
	/**
	 * Add an assignment to the database that is due at a specific time
	 * 
	 * @param dayDue The day of the month the assignment is due
	 * @param monthDue The month the assignment is due in
	 * @param timeDue The time the assignment is due
	 * @param name The name of the assignment
	 */
	public void addAssignment(int dayDue, String monthDue, String timeDue, String name){
		_addAssignment(dayDue, monthDue, timeDue, name);
	}
	
	/**
	 * Add and assignment to the database that is not due at a specific time
	 * 
	 * @param dayDue The day of the month the assignment is due
	 * @param monthDue The month the assignment is due in
	 * @param name The name of the assignment
	 */
	public void addAssignment(int dayDue, String monthDue, String name){
		_addAssignment(dayDue, monthDue, "00:00", name);
	}
	
	/**
	 * Internally used to add an assignment to the database.
	 * 
	 * @param dayDue The day of the month the assignment is due
	 * @param monthDue The month the assignment is due in
	 * @param timeDue The time the assignment is due
	 * @param name The name of the assignment
	 */
	private void _addAssignment(int dayDue, String monthDue, String timeDue, String name){
		pushAssignments.setValue(new Assignment(dayDue, monthDue, timeDue, processAssignmentName(name)));
		String due;
		
		if(timeDue != "00:00"){
			due = monthDue + " " + dayDue + " at " + timeDue;
		}
		else{
			due = monthDue + " " + dayDue;
		}
		
		Texting.sendAssignmentTexts(name, due, phoneNumbers);
	}
	
	/**
	 * The value to return in removeAssignment(String name)
	 */
	private volatile boolean toReturn = false;
	
	/**
	 * Make the students happy and remove an assignment from the database
	 * 
	 * @param name The name of the assignment to remove
	 * @return Whether the assignment was removed successfully
	 */
	public boolean removeAssignment(final String name){
		final CountDownLatch latch = new CountDownLatch(1);
		
		readAssignments.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError arg0) {}

			public void onDataChange(DataSnapshot snapshot) {
				for(DataSnapshot s : snapshot.getChildren()){
					if(s.child("title").getValue().toString().toLowerCase().equals(processAssignmentName(name).toLowerCase())){
						toReturn = true;
						snapshot.getRef().child(s.getKey()).setValue(null);
						Texting.sendAssignmentTexts(name, null, phoneNumbers);
						latch.countDown();
					}
				}
				
				if(!toReturn){
					latch.countDown();
				}
			}
			
		});
		
		try{
			latch.await();
		}
		catch(InterruptedException ex){}
		
		boolean toReturnTemp = toReturn;
		toReturn = false;
		return toReturnTemp;
	}
	
	/**
	 * Get the number of points someone has
	 * 
	 * @param name The person to get points for
	 * @return The number of points the person has
	 */
	public int getPoints(final String name){
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				if(person.exists()){
					points = Integer.parseInt(person.child("points").getValue().toString());
				}
				else{
					points = 0;
				}
				person.getRef().child("points").setValue(points);
				
				latch.countDown();
			}
			
		});

		try{
			latch.await();
		}
		catch(InterruptedException ex){}
		
		return points;
	}
	
	/**
	 * Process the raw assignment name from Alexa. Right now this only changes string numbers
	 * to numbers (i.e. four to 4, 20 to twenty, etc.)
	 * 
	 * @param name The raw assignment name to process
	 * @return The processed assignment name
	 */
	private String processAssignmentName(String name){
		StringBuilder output = new StringBuilder();
		String[] input = name.split(" ");
		
		for(int i = 0; i < input.length; i++){
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
	
	private String studentWithMostPoints = "";
	
	private int mostPoints = 0;
	
	/**
	 * Get the person who has the most points
	 * 
	 * @return The name of the person with the most points
	 */
	public String getGreatestPoints(){
		final CountDownLatch latch = new CountDownLatch(1);
		
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError arg0) {}

			public void onDataChange(DataSnapshot data) {
				for(DataSnapshot s : data.getChildren()){
					String name = s.getKey();
					int points = Integer.parseInt(s.child("points").getValue().toString());
					
					if(studentWithMostPoints.equals("")){
						studentWithMostPoints = name;
						mostPoints = points;
					}
					else if(points == mostPoints){
						studentWithMostPoints += ", " + s.getKey();
					}
					else if(points > mostPoints){
						studentWithMostPoints = name;
						mostPoints = points;
					}
				}
				
				latch.countDown();
			}
			
		});
		
		try{
			latch.await();
		}
		catch(InterruptedException ex){}
		String studentTemp = studentWithMostPoints;
		studentWithMostPoints = "";
		return studentTemp;
	}
}
