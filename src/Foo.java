import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Foo implements Listener{

    public Foo(){

    }

    @Override
    public void onRecognitionResult(String result) {
        System.out.println("Message received >>> "+result);
    }
}