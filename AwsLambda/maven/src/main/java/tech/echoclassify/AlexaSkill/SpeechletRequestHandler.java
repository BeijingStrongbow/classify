package tech.echoclassify.AlexaSkill;

import java.util.HashSet;
import java.util.Set;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class SpeechletRequestHandler extends SpeechletRequestStreamHandler {
	private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        
        supportedApplicationIds.add("amzn1.ask.skill.5b25b9d2-16ad-48fd-ba56-49b949ee3499");
    }
	
    public SpeechletRequestHandler(){
    	super(new ClassifySpeechlet(), supportedApplicationIds);
    }
}
