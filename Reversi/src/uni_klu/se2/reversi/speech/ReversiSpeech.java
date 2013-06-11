package uni_klu.se2.reversi.speech;

import com.sun.speech.freetts.VoiceManager;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class ReversiSpeech {
	
	public ReversiSpeech() { }
	
	/**
	 * The beneath code is just a usage example
	 */
	public void example() {
		
		String recognizedConfirmation = "";
		String recognizedText = "";
		
		do {
			recognizedText = recognizeSpeech("ReversiInputNumbersAndLetters.config.xml", 
					"Say: (A | B | C | D | E | F | G | H) ( One | Two | Three | Four | Five | Six | Seven | Eight)");
			
			System.out.println("Are you sure?");
			
			recognizedConfirmation = recognizeSpeech("ReversiInputConfirmation.config.xml",
					"Say: (yes | no)");
			
		} while(recognizedConfirmation.equals("no"));
		
		
		System.out.println("ok, as you wish");
		System.out.println();
		System.out.println();
		System.out.println("--------------------------");
		System.out.println("Result: " + recognizedText);
		System.out.println("--------------------------");
		saySomething(recognizedText);
		
	}
	
	public void saySomething(String text) {
		Voice voiceKevin16 = new Voice("kevin16");

        String[] thingsToSay = new String[]
        {
            "You said"
        };

        
        voiceKevin16.say(thingsToSay);
        voiceKevin16.say(text.split(" "));
        voiceKevin16.dispose();
	}
	
	public String recognizeSpeech(String configFile, String output) {
		String returnVal = "";
		
		ConfigurationManager cm = new ConfigurationManager(ReversiSpeech.class.getResource(configFile));
		
		
		Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
 
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
 
        System.out.println(output);
 
 
        Result result = recognizer.recognize();
 
        if (result != null) {
            returnVal = result.getBestFinalResultNoFiller();
            
	        System.out.println("You said: " + returnVal + '\n');
        } else {
            System.out.println("I can't hear what you said.\n");
        }
		
        microphone.stopRecording();
        microphone.clear();
        recognizer.deallocate();
		
		return returnVal;
	}
}

class Voice
{
    private String name;
    private com.sun.speech.freetts.Voice systemVoice;

    public Voice(String name)
    {
        this.name = name;
        this.systemVoice = VoiceManager.getInstance().getVoice(this.name);
        this.systemVoice.allocate();
    }

    public void say(String[] thingsToSay)
    {
        for (int i = 0; i < thingsToSay.length; i++)
        {
            this.say( thingsToSay[i] );
        }
    }

    public void say(String thingToSay)
    {
        this.systemVoice.speak(thingToSay);
    }

    public void dispose()
    {
        this.systemVoice.deallocate();
    }
}
