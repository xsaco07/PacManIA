import com.sun.jdi.VMOutOfMemoryException;
import com.sun.jdi.VoidValue;
import com.sun.speech.freetts.Voice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UserInput implements Listener {

    private static UserInput instance;

    // Constants for the ranges of user input data
    public static final int MIN_ROWS = 5;
    public static final int MAX_ROWS = 25;

    public static final int MIN_COLS = 5;
    public static final int MAX_COLS = 25;

    public static final int MIN_CELLSIZE = 20;
    public static final int MAX_CELLSIZE = 100;




    // Constants for the indexes in the array
    public static final int WIDTH = 0;
    public static final int HEIGHT= 1;
    public static final int CELLSIZE = 2;


//     For knowing which value hasn't been entered correctly
    private static final int UNKNOWN = -1;

    // Valid Bounds for the values that the user can enter
    private int MIN_VALUE = UNKNOWN;
    private int MAX_VALUE = UNKNOWN;



    // The index of the value being asked (acts like a cursor)
    private int askingValue;


    // What data this class is asking to the user
    private int DIMENSIONS = 0;
    private int POSITION = 1;

    private int askingData; // this can be DIMENSIONS or POSITION


    // The result returned by askDimensions()
    private int dimensions[];


    // The result returned by askPosition()
    private int position[];
    private int cursor; //used in askPosition()


    private UserInput(){
        initNumMapping();
//        System.out.println(numMapping);
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
        // For the onResult method to know where to write
        askingData = DIMENSIONS;

        // Initialize the result to be returned
        dimensions = new int[]{UNKNOWN, UNKNOWN, UNKNOWN};

        // Register to get voice recognitions
        VoiceHelper.getInstance().register(this);

        // Set the cursor to the first position
        askingValue = WIDTH;
        String valuesNames[] = {"width", "height", "cell size"};

        for( String valueName : valuesNames){

            // Set the correct bounds
            if(valueName.equals("width")){ MIN_VALUE = MIN_COLS; MAX_VALUE = MAX_COLS;}
            else if(valueName.equals("height")){ MIN_VALUE = MIN_ROWS; MAX_VALUE = MAX_ROWS;}
            else if(valueName.equals("cell size")){ MIN_VALUE = MIN_CELLSIZE; MAX_VALUE = MAX_CELLSIZE;}

            // Ask for the input
            VoiceHelper.getInstance().say("Please say the desired "+ valueName
                    + ". Beetween " + MIN_VALUE + " and " + MAX_VALUE + ".");

            System.out.printf("Asking %s between %d and %d\n", valueName, MIN_VALUE, MAX_VALUE );

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

        askingData = UNKNOWN;
        VoiceHelper.getInstance().unregister(this);
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
            if(askingData == DIMENSIONS){
                // Put the value in the current value being asked
                dimensions[askingValue] = value;
            }
            else if(askingData == POSITION){
                position[cursor] = value;

            }
            else{
                System.out.println("Error: asking value unset or invalid");
            }

        }
        else{
            System.out.println("Invalid input");
            VoiceHelper.getInstance().say("That is an invalid value please say another value");

        }

    }


    /**
     * Asks the user to say a row and column to locate some character
     * @param grid needed to verify the user has entered an empty cell
     * @return A array of int [row,column]
     */
    public int[] askPosition(Grid grid){
        cursor = 0; // Position of current value being asked
        position = new int[]{UNKNOWN, UNKNOWN};
        boolean validInputEntered = false;
        String valuesNames[] = {"row", "column"};
        askingData = POSITION;

        // Set bounds for the onResult method to validate the result
        // Asks the row fist so the bound is the height of the grid
        MIN_VALUE = 0; MAX_VALUE = grid.height-1;

        // Register to get voice recognitions
        VoiceHelper.getInstance().register(this);


        while( ! validInputEntered ) {
            for (String valueName : valuesNames) {
                // Ask for the input
                VoiceHelper.getInstance().say("Please say the desired " + valueName
                        + ". Beetween " + MIN_VALUE + " and " + MAX_VALUE + ".");



                // Wait until a valid value is said
                while (position[cursor] == UNKNOWN) {
                    try {
                        System.out.printf("Asking %s between %d and %d\n", valueName, MIN_VALUE, MAX_VALUE );
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println("Error sleeping while asking user input");
                        e.printStackTrace();
                    }
                }
                // Move the cursor to the next position
                cursor += 1;

                System.out.printf("%s set to %d\n", valueName, position[cursor-1]);


            }
            validInputEntered = true;
        }

        VoiceHelper.getInstance().unregister(this);
        askingData = UNKNOWN;
        return position;
    }
}