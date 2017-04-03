package tech.echoclassify.AlexaSkill;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
	
	private boolean done = false;
	
	private static FirebaseHandler instance = null;
	
	public static FirebaseHandler getInstance(){
		if(instance == null){
			throw new NullPointerException("The database connection must be initialized! Use the other getInstance() overload!");
		}
		else{
			return instance;
		}
	}
	
	public static FirebaseHandler getInstance(FileInputStream credentials, String databaseUrl){
		if(instance == null){
			instance = new FirebaseHandler(credentials, databaseUrl);
		}
		
		return instance;
	}
	
	
	private FirebaseHandler(FileInputStream credentials, String databaseUrl){
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredential(FirebaseCredentials.fromCertificate(credentials))
				.setDatabaseUrl(databaseUrl)
				.build();
		
		FirebaseApp.initializeApp(options);
		
		pushAssignments = FirebaseDatabase.getInstance().getReference("Assignments").push();
		setPeople = FirebaseDatabase.getInstance().getReference("People").push();
		
		readAssignments = FirebaseDatabase.getInstance().getReference("Assignments");
		readPeople = FirebaseDatabase.getInstance().getReference("People").push();
	}
	
	private static class Person{
		
		private String name;
		private int points;
		
		private Person(String name, int points){
			this.name = name;
			this.points = points;
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

		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				points = Integer.parseInt(person.child("points").toString());
				points += numPoints;
				person.getRef().child("points").setValue(points);
				done = true;
			}
			
		});
		
		while(!done){}
		done = false;
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
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				points = Integer.parseInt(person.child("points").toString());
				points -= numPoints;
				person.getRef().child("points").setValue(points);
				done = true;
			}
			
		});
		
		while(!done){}
		done = false;
		return points;
	}
	
	/**
	 * Add an assignment to the database
	 * 
	 * @param name The name of the assignment to add
	 * @param date The due date for the assignment as a string
	 * @param monthAsInt The month the assignment is due in, as an int (i.e. January is 1, Feburary is 2, etc.)
	 * @param dayAsInt The day of the month the assignemnt is due in
	 */
	public void addAssignment(String name, String date, int monthAsInt, int dayAsInt){
		
			
	}
	
	/**
	 * Make the students happy and remove an assignment from the database
	 * 
	 * @param name The name of the assignment to remove
	 * @return Whether the assignment was removed successfully
	 */
	public boolean removeAssignment(String name){
		
		return true;
	}
	
	/**
	 * Get the number of points someone has
	 * 
	 * @param name The person to get points for
	 * @return The number of points the person has
	 */
	public int getPoints(final String name){
		readPeople.addListenerForSingleValueEvent(new ValueEventListener(){

			public void onCancelled(DatabaseError snapshot) {
				System.out.println("Read cancelled!");
			}

			public void onDataChange(DataSnapshot snapshot) {
				DataSnapshot person = snapshot.child(name);
				
				points = Integer.parseInt(person.child("points").toString());
				person.getRef().child("points").setValue(points);
				done = true;
			}
			
		});
		
		while(!done){}
		done = false;
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
	
	/**
	 * Get the person who has the most points
	 * 
	 * @return The name of the person with the most points
	 */
	public String getGreatestPoints(){
		return "";
	}
}
