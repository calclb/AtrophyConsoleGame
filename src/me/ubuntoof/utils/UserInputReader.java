package me.ubuntoof.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    private static void print(String s) { Colorizer.print(s); }
}
