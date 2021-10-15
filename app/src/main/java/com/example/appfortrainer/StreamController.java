package com.example.appfortrainer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StreamController {

    public static void writeToFile(String data, Context context, String FILE_NAME)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));   // После чего создаем поток для записи
            outputStreamWriter.write(data);                            // и производим непосредственно запись
            outputStreamWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String readToFile(Context context,  String FILE_NAME) throws IOException
    {
        String result = "";
        InputStream inputStream = context.openFileInput(FILE_NAME);
        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            StringBuilder stringBuilder = new StringBuilder();
            while((temp = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(temp);
                stringBuilder.append("\n");
            }
            inputStream.close();
            result = stringBuilder.toString();
        }
        return result;
    }
}
