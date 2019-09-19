import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.util.ArrayList;

public class VoiceManager {

    private static ArrayList<Listener> clients = new ArrayList<>();
    private static Thread recognitionThread;



    public static void register(Listener listener){
        clients.add(listener);
        System.out.println("New client registered");
    }



    private static void spreadResult(String result){
        for(Listener listener : clients){
            listener.onRecognitionResult(result);
        }
        System.out.println("Message sent to clients");
    }


    public static void start(){
        recognitionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startRecognition();
            }
        });

        recognitionThread.start();
    }

    public static void stop(){
        recognitionThread.stop();
        System.out.println("Recognition thread stopped");
    }


    public static void startRecognition(){
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


}
