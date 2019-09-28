import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.ArrayList;

public class VoiceHelper {

    private ArrayList<Listener> clients;
    private Thread recognitionThread;

    private static VoiceHelper instance;

    private boolean isRunning = false;

    public static VoiceHelper getInstance(){
        if(instance == null){
            instance = new VoiceHelper();
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        }
        return instance;
    }

    private VoiceHelper(){
        clients = new ArrayList<>();
    }


    public void register(Listener listener){
        if( ! isRunning){
            start();
            isRunning = true;
        }
        clients.add(listener);
        System.out.println("New client registered");
    }



    private void spreadResult(String result){
        for(Listener listener : clients){
            listener.onRecognitionResult(result);
        }
        System.out.println("Message sent to clients");
    }



    public void start(){
        recognitionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startRecognition();
            }
        });

        recognitionThread.start();
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


//        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        try{
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            for (int i=0 ; i<6 ; i++){
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


    public void say(String text){
        Voice voice;//Creating object of Voice class
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            voice.setRate(120);//Setting the rate of the voice
            voice.setPitch(100);//Setting the Pitch of the voice
            voice.setVolume(3);//Setting the volume of the voice
            String[] divs = text.split(" ");
            for (String s : divs){
                voice.speak(s);//Calling speak() method

            }


        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}
