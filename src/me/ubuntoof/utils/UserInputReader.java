package me.ubuntoof.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static me.ubuntoof.utils.TextFormatter.print;

public class UserInputReader
{
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String getResponse()
    {
        print("〉");
        String userInput = "";

        try
        {
            userInput = br.readLine();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return userInput;
    }

    /**
     * Attempts to repeat a prompt until the user's response matches the regex parameter given. If the input String isn't valid,
     * the method shows an error message and continues to receive an input.
     * @param regex The regex to test the input against.
     * @param errorMsg The message to give if the regex fails.
     */
    public static String getResponseWhenRegexMatched(String regex, String errorMsg)
    {
        Pattern pattern = Pattern.compile(regex);
        String uinput;
        boolean isValid;
        do
        {
            uinput = getResponse();
            isValid = pattern.matcher(uinput).matches();
            if(!isValid) System.out.println(errorMsg);
        } while(!isValid);
        return uinput;
    }
}
