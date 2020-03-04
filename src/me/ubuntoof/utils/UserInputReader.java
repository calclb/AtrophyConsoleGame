package me.ubuntoof.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputReader
{
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String getResponse()
    {
        System.out.print(Colorizer.RESET + "〉");
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
}
