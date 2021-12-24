package we.polo.appfortrainer.saveandload;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StreamController {

    public static void writeToFile(String data, Context context, String FILE_NAME)
    {
        try {
            FileOutputStream file = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);
            outputStreamWriter.write(data);
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

    public static void deletedFile(Context context, String FILE_NAME){
        String uri = context.getApplicationInfo().dataDir + "/files/" + FILE_NAME;
        File fdelete = new File(uri);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Toast.makeText(context, "file Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
