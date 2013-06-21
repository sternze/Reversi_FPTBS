package uni_klu.se2.reversi.speech;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import uni_klu.se2.reversi.data.Move;

import com.sun.speech.freetts.VoiceManager;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class ReversiSpeech {
	
	public ReversiSpeech() { }
	
	public ReversiSpeech(List<Move> possibleMoves) {
		createTemporaryFile(possibleMoves);
	}
	
	private void createTemporaryFile(List<Move> possibleMoves) {
		try {
			PrintWriter writer = new PrintWriter("./src/uni_klu/se2/reversi/speech/CurrentPossibleGrammar.gram", "UTF-8");
			writer.println("#JSGF V1.0;");
			writer.println("/**");
			writer.println(" * JSGF Grammar for temporary generated file");
			writer.println(" */");
			writer.println("grammar CurrentPossibleGrammar;");
			writer.println("public <input> = (");
			
			String grammarString = "";
			
			for(Move m : possibleMoves) {
				grammarString += "(";
				
				switch (m.getX()) {
				case 0: grammarString += "A ";
					break;
				case 1: grammarString += "B ";
					break;
				case 2: grammarString += "C ";
					break;
				case 3: grammarString += "D ";
					break;
				case 4: grammarString += "E ";
					break;
				case 5: grammarString += "F ";
					break;
				case 6: grammarString += "G ";
					break;
				case 7: grammarString += "H ";
					break;
				default: grammarString += "A ";
					break;
				}
				
				switch (m.getY()) {
				case 0: grammarString += "One),";
				break;
				case 1: grammarString += "Two),";
				break;
				case 2: grammarString += "Three),";
				break;
				case 3: grammarString += "Four),";
				break;
				case 4: grammarString += "Five),";
				break;
				case 5: grammarString += "Six),";
				break;
				case 6: grammarString += "Seven),";
				break;
				case 7: grammarString += "Eight),";
				break;
				default:
					break;
				}
			}
			
			grammarString = grammarString.substring(0, grammarString.length()-1);
			
			writer.print(grammarString);
			writer.print(");");
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The beneath code is just a usage example
	 */
	public void example(List<Move> possibleMoves) {
		
		String recognizedConfirmation = "";
		String recognizedText = "";
		
		do {
			recognizedText = recognizeSpeech("Please start speaking in one second.");
			
			
			if(IsRecognizedTextAValidMove(recognizedText, possibleMoves)) {
				saySomething("Are you sure?", false);
				
				recognizedConfirmation = recognizeSpeech("Say yes or no)");
			} else {
				saySomething("I understood " + recognizedText + ". This is not a valid move. Please repeat when prompted.", false);
				recognizedConfirmation = "no";
			}
			
			
		} while(recognizedConfirmation.equals("no"));
		
		
		System.out.println("ok, as you wish");
		System.out.println();
		System.out.println();
		System.out.println("--------------------------");
		System.out.println("Result: " + recognizedText);
		System.out.println("--------------------------");
		saySomething(recognizedText, true);
		
	}
	
	private boolean IsRecognizedTextAValidMove(String recognizedText, List<Move> possibleMoves) {
		boolean ok = false;
		List<String> mov = new ArrayList<String>();
		
		for(Move m : possibleMoves) {
			String grammarString = "";
			
			switch (m.getX()) {
			case 0: grammarString += "a ";
				break;
			case 1: grammarString += "b ";
				break;
			case 2: grammarString += "c ";
				break;
			case 3: grammarString += "d ";
				break;
			case 4: grammarString += "e ";
				break;
			case 5: grammarString += "f ";
				break;
			case 6: grammarString += "g ";
				break;
			case 7: grammarString += "h ";
				break;
			default: grammarString += "a ";
				break;
			}
			
			switch (m.getY()) {
			case 0: grammarString += "one";
			break;
			case 1: grammarString += "two";
			break;
			case 2: grammarString += "three";
			break;
			case 3: grammarString += "four";
			break;
			case 4: grammarString += "five";
			break;
			case 5: grammarString += "six";
			break;
			case 6: grammarString += "seven";
			break;
			case 7: grammarString += "eight";
			break;
			default: grammarString += "one";
				break;
			}
			
			mov.add(grammarString);
		}
		
		if(mov.contains(recognizedText.toLowerCase())) {
			ok = true;
		}
		
		return ok;
	}

	public void saySomething(String text, boolean onlyRecognizedText) {
		Voice voiceKevin16 = new Voice("kevin16");

        String[] thingsToSay = new String[]
        {
            "You said"
        };

        if(onlyRecognizedText) {
        	voiceKevin16.say(thingsToSay);
        }
        voiceKevin16.say(text.split(" "));
        voiceKevin16.dispose();
	}
	
	public String recognizeSpeech(String output) {
		String returnVal = "";
		
		ConfigurationManager cm = new ConfigurationManager(ReversiSpeech.class.getResource("ReversiInputCurrentPossibleGrammar.config.xml"));
		
		
		Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
 
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }
 
        //System.out.println(output);
 
        saySomething(output, false);
 
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
