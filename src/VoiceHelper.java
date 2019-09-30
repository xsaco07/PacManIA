import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import com.sun.speech.freetts.en.us.FeatureProcessors;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.ArrayList;

public class VoiceHelper {

    private ArrayList<Listener> clients;
    private Thread recognitionThread;

    // Singleton implementation
    private static VoiceHelper instance;

    private boolean isRunning = false;

    // Singleton method
    static VoiceHelper getInstance(){
        if(instance == null){
            instance = new VoiceHelper();
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        }
        return instance;
    }

    private VoiceHelper(){
        clients = new ArrayList<>();
    }


    void register(Listener listener){
        if( ! isRunning){
            start();
            isRunning = true;
        }
        if( ! clients.contains(listener)) {
            clients.add(listener);
            System.out.println("New client registered");
        }
        else{
            System.out.println("Already registered client tried to register: "+listener);
        }
    }

    void unregister(Listener listener){
        if(clients.contains( listener)){
            clients.remove(listener);
        }
    }



    private void spreadResult(String result){
        for(Listener listener : clients){
            listener.onRecognitionResult(result);
        }
        //System.out.println("Message sent to clients");
    }



    private void start(){
        recognitionThread = new Thread(this::startRecognition);

        recognitionThread.start();
    }



    public void stop(){
        recognitionThread.stop();
        //System.out.println("Recognition thread stopped");
    }



    private void startRecognition(){
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

        configuration.setGrammarPath("resources/grammars/");
        configuration.setGrammarName("grammar");
        configuration.setUseGrammar(true);

        try{
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            while( isRunning ){
                // Start recognition process pruning previously cached data.
                recognizer.startRecognition(true);

                SpeechResult result = recognizer.getResult();

                System.out.println("Recognizer got \""+result.getHypothesis()+"\"");

                spreadResult(result.getHypothesis());

                // Pause recognition process. It can be resumed then with startRecognition(false).
                recognizer.stopRecognition();
            }
        }catch (Exception e){
            System.out.println("Error initializing the recognizer");
            e.printStackTrace();
        }


    }


    void say(String text){
        Voice voice; //Creating object of Voice class
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            assert voice != null;
            voice.setRate(120); //Setting the rate of the voice
            voice.setPitch(100); //Setting the Pitch of the voice
            voice.setVolume(3); //Setting the volume of the voice
            String[] divs = text.split(" ");
            for (String s : divs){
                voice.speak(s); //Calling speak() method

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
