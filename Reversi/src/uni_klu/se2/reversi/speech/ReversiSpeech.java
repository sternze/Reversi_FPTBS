package uni_klu.se2.reversi.speech;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.Window;
import jfx.messagebox.MessageBox;
import uni_klu.se2.reversi.data.Move;

import com.sun.speech.freetts.VoiceManager;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class ReversiSpeech {
	
	public ReversiSpeech() { }
	
	
	public void createTemporaryFile(List<Move> possibleMoves) {
		try {
			PrintWriter writer = new PrintWriter("./src/uni_klu/se2/reversi/speech/CurrentPossibleGrammar.gram", "UTF-8");
			writer.println("#JSGF V1.0;");
			writer.println("/**");
			writer.println(" * JSGF Grammar for temporary generated file");
			writer.println(" */");
			writer.println("grammar CurrentPossibleGrammar;");
			
			String grammarString = "public <input> = (";
			
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
				case 0: grammarString += "One) | ";
				break;
				case 1: grammarString += "Two) | ";
				break;
				case 2: grammarString += "Three) | ";
				break;
				case 3: grammarString += "Four) | ";
				break;
				case 4: grammarString += "Five) | ";
				break;
				case 5: grammarString += "Six) | ";
				break;
				case 6: grammarString += "Seven) | ";
				break;
				case 7: grammarString += "Eight) | ";
				break;
				default:
					break;
				}
			}
			
			grammarString = grammarString.substring(0, grammarString.length()-3);
			
			grammarString += ");";
			writer.print(grammarString);
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public Move recognizeNow(List<Move> possibleMoves, Window primaryStage) {
		String recognizedConfirmation = "";
		String recognizedText = "";
		
		saySomething("I will try to recognize your move now.", false);
		
		do {
			recognizedText = recognizeSpeech(null, primaryStage, false);
			
			if(recognizedText != null) {
				saySomething(recognizedText, true);
				saySomething("Are you sure?", false);
				recognizedConfirmation = recognizeSpeech("Say yes, no, or abort.", primaryStage, true);
			}
			
			if(recognizedConfirmation.equals("no")) {
				saySomething("Ok, please repeat your move when prompted.", false);
			}
			
		} while(recognizedConfirmation.equals("no"));
		
		if(recognizedConfirmation.toLowerCase().equals("abort")) {
			saySomething("abort", true);
			return null;
		}

		saySomething("Ok, I will make this move now.", false);
		
		System.out.println("ok, as you wish");
		System.out.println();
		System.out.println();
		System.out.println("--------------------------");
		System.out.println("Result: " + recognizedText);
		System.out.println("--------------------------");
		
		return getMoveAccordingToSpeechInput(possibleMoves, recognizedText);
	}

	private Move getMoveAccordingToSpeechInput(List<Move> possibleMoves, String recognizedText) {
		int PosXOfMove = getValueOfRecognizedText(recognizedText, true);
		int PosYOfMove = getValueOfRecognizedText(recognizedText, false);
		
		for(Move m : possibleMoves) {
			if(m.getX() == PosXOfMove && m.getY() == PosYOfMove) {
				return m;
			}
		}
		return null;
	}


	private int getValueOfRecognizedText(String recognizedText, boolean isXValue) {
		int val = 0;
		String X = "";
		String Y = "";
		
		if(isXValue) {
			X = recognizedText.trim().split(" ")[0].toLowerCase();
			
			switch (X) {
				case "a": val = 0;
				break;
				case "b": val = 1;
				break;
				case "c": val = 2;
				break;
				case "d": val = 3;
				break;
				case "e": val = 4;
				break;
				case "f": val = 5;
				break;
				case "g": val = 6;
				break;
				case "h": val = 7;
				break;
				default: val = 0;
				break;
			}
		} else {
			Y = recognizedText.trim().split(" ")[1].toLowerCase();
			
			switch (Y) {
				case "one": val = 0;
				break;
				case "two": val = 1;
				break;
				case "three": val = 2;
				break;
				case "four": val = 3;
				break;
				case "five": val = 4;
				break;
				case "six": val = 5;
				break;
				case "seven": val = 6;
				break;
				case "eight": val = 7;
				break;
				default: val = 0;
				break;
			}
		}
		
		return val;
	}
	
	
	
//	private boolean IsRecognizedTextAValidMove(String recognizedText, List<Move> possibleMoves) {
//		boolean ok = false;
//		List<String> mov = new ArrayList<String>();
//		
//		for(Move m : possibleMoves) {
//			String grammarString = "";
//			
//			switch (m.getX()) {
//			case 0: grammarString += "a ";
//				break;
//			case 1: grammarString += "b ";
//				break;
//			case 2: grammarString += "c ";
//				break;
//			case 3: grammarString += "d ";
//				break;
//			case 4: grammarString += "e ";
//				break;
//			case 5: grammarString += "f ";
//				break;
//			case 6: grammarString += "g ";
//				break;
//			case 7: grammarString += "h ";
//				break;
//			default: grammarString += "a ";
//				break;
//			}
//			
//			switch (m.getY()) {
//			case 0: grammarString += "one";
//			break;
//			case 1: grammarString += "two";
//			break;
//			case 2: grammarString += "three";
//			break;
//			case 3: grammarString += "four";
//			break;
//			case 4: grammarString += "five";
//			break;
//			case 5: grammarString += "six";
//			break;
//			case 6: grammarString += "seven";
//			break;
//			case 7: grammarString += "eight";
//			break;
//			default: grammarString += "one";
//				break;
//			}
//			
//			mov.add(grammarString);
//		}
//		
//		if(mov.contains(recognizedText.toLowerCase())) {
//			ok = true;
//		}
//		
//		return ok;
//	}


	public void saySomething(String text, boolean onlyRecognizedText) {
		Voice voiceKevin16 = new Voice("kevin16");

        String[] thingsToSay = new String[]
        {
            "You said"
        };


        if(onlyRecognizedText) {
        	voiceKevin16.say(thingsToSay);
            voiceKevin16.say(text.split(" "));
        } else {
            voiceKevin16.say(text);
        }
        
        voiceKevin16.dispose();
	}
	
	public String recognizeSpeech(String output, Window primaryStage, boolean confirmation) {
		String returnVal = "";
		
		ConfigurationManager cm  = null;
		if(confirmation) {
			cm = new ConfigurationManager(ReversiSpeech.class.getResource("ReversiInputConfirmation.config.xml"));
		} else {
			cm = new ConfigurationManager(ReversiSpeech.class.getResource("ReversiInputCurrentPossibleGrammar.config.xml"));
		}
		
		if(output != null) {
			saySomething(output, false);
		}
		
		Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
 
        
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        
        MessageBox.show(primaryStage, "Please start speaking after clicking on ok.", "Start speaking.", MessageBox.ICON_INFORMATION | MessageBox.OK);
        
        if (!microphone.startRecording()) {
        	MessageBox.show(primaryStage, "There was a problem while starting your microphone.", "Error while starting Mic.", MessageBox.ICON_ERROR | MessageBox.OK);
            //System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            //System.exit(1);
            return null;
        }
  
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
