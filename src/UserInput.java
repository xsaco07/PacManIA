import com.sun.jdi.VMOutOfMemoryException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UserInput implements Listener {

    private static UserInput instance;


    // Constants for the indexes in the array
    public static final int WIDTH = 0;
    public static final int HEIGHT= 1;
    public static final int CELLSIZE = 2;


//     For knowing which value hasn't been entered correctly
    private static final int UNKNOWN = -1;

    // Valid Bounds for the values that the user can enter
    private final int MIN_VALUE = 5;
    private final int MAX_VALUE = 100;



    // The index of the value being asked (acts like a cursor)
    private int askingValue;



    // The result returned by askDimensions()
    private int dimensions[];


    private UserInput(){
        initNumMapping();
        System.out.println(numMapping);
    }

    private void initNumMapping() {
        numMapping = new HashMap<String, Integer>();
        int value = 1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/nums.txt"));

            String line = reader.readLine();
            while (line != null) {
                numMapping.put(line, value);
                value++;
                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserInput getInstance(){
        if(instance == null){
            instance = new UserInput();
        }
        return instance;
    }


    // TEMP mapping
    private HashMap<String, Integer> numMapping;


    /**
        Asks the user to say the desired game dimensions, validates the input and returns an
        array as -> [width, height, cellsize]
     */
    public int[] askDimensions(){

        // Initialize the result to be returned
        dimensions = new int[]{UNKNOWN, UNKNOWN, UNKNOWN};

        // Register to get voice recognitions
        VoiceHelper.getInstance().register(this);

        // Set the cursor to the first position
        askingValue = WIDTH;
        String valuesNames[] = {"width", "height", "cell size"};

        for( String valueName : valuesNames){
            // Ask for the input
            VoiceHelper.getInstance().say("Please say the desired "+ valueName);

            // Wait until a valid value is said
            while(dimensions[askingValue] == UNKNOWN){
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    System.out.println("Error sleeping while asking user input");
                    e.printStackTrace();
                }
            }

            // Move the cursor to the next position
            askingValue += 1;

        }

        return dimensions;

    }

    @Override
    public void onRecognitionResult(String result) {
        // Assert the string represents a number
        // TODO: pending...

        // Convert the result string into a numeric value
        int value = numMapping.get(result);

        // Assert value in bounds
        if(MIN_VALUE <= value && value <= MAX_VALUE){
            // Put the value in the current value being asked
            dimensions[askingValue] = value;
        }
        else{
            System.out.println("Invalid input");
            VoiceHelper.getInstance().say("That is an invalid value please say another value");

        }

    }
}