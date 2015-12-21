package lesson1.our.com.gllesson1;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ShaderParser {

    public static String getShader(Context context, String fileName) {
        String shaderSource = "";

        try {
            InputStream is = context.getAssets().open(fileName);
            Scanner scanner = new Scanner(is);

            while (scanner.hasNextLine()) {
                shaderSource += scanner.nextLine() + "\n";
            }

            scanner.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shaderSource;
    }
}
