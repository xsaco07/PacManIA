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
        start();
    }


    void register(Listener listener){
        if( ! isRunning){
            start();
        }
        if( ! clients.contains(listener)) {
            clients.add(listener);
            System.out.println("New client registered:"+listener.getClass().getName());
        }
        else{
            System.out.println("Already registered client tried to register: "+listener);
        }
    }

    void unregister(Listener listener){
        if(clients.contains( listener)){
            clients.remove(listener);
            System.out.println("Voice helper removed a client");
        }
    }



    private void spreadResult(String result){
        for(Listener listener : clients){
            System.out.println("Voice Helper about to send <"+result+" > to " +listener.getClass().getName());
            listener.onRecognitionResult(result);
        }
        System.out.println("Voice Helper sent <"+result+" >to clients");
    }



    private void start(){
        recognitionThread = new Thread(this::startRecognition);
        recognitionThread.start();
        isRunning = true;
        System.out.println("Voice Helper started recognition thread");
    }



    public void stop(){
        recognitionThread.stop();
        System.out.println("Recognition thread stopped");
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
                System.out.println("Recognizer alive! ");

                // Start recognition process pruning previously cached data.
                recognizer.startRecognition(true);

                SpeechResult result = recognizer.getResult();

                System.out.println("Recognizer got \""+result.getHypothesis()+"\"");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        spreadResult(result.getHypothesis());
                    }
                }).start();

                // Pause recognition process. It can be resumed then with startRecognition(false).
                recognizer.stopRecognition();
            }
            System.out.println("Voice Helper stopped recognition");
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
