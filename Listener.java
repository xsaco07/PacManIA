import javax.speech.*;
import javax.speech.recognition.*;
import java.io.FileReader;
import java.util.Locale;

public class Listener extends ResultAdapter {

    static Recognizer recognizer;
    String gst;

    @Override
    public void resultAccepted(ResultEvent re) {
        try {
            Result res = (Result) (re.getSource());
            ResultToken tokens[] = res.getBestTokens();

            String args[] = new String[1];
            args[0] = "";
            for (int i = 0; i < tokens.length; i++) {
                gst = tokens[i].getSpokenText();
                args[0] += gst + " ";
                System.out.print(gst + " ");
            }
            System.out.println();
            if (gst.equals("stop")) {
                recognizer.deallocate();
                System.exit(0);
            } else {
                recognizer.suspend();
                //Lee.main(args);
                recognizer.resume();
            }
        } catch (Exception ex) {
            System.out.println("Error" + ex);
        }
    }

    public static void main(String args[]) {
        try {
            recognizer = Central.createRecognizer(new EngineModeDesc(Locale.ROOT));
            recognizer.allocate();

            FileReader grammar1 = new FileReader("C:\\Users\\Kenneth Vargas\\Desktop\\libs\\SimpleGrammarES2.txt");

            RuleGrammar rg = recognizer.loadJSGF(grammar1);
            rg.setEnabled(true);

            recognizer.addResultListener(new Listener());

            System.out.println("Start Talking (Only words in the grammar file will be processed)");
            recognizer.commitChanges();

            recognizer.requestFocus();
            recognizer.resume();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
    }
}