public class Foo implements Listener{

    public Foo(){

    }

    @Override
    public void onRecognitionResult(String result) {
        System.out.println("Message received >>> "+result);
    }
}