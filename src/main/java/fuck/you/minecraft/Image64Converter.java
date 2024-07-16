package fuck.you.minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Image64Converter {

    public static String convertToBase64(String path) {
        try {
            InputStream fileInputStreamReader = Image64Converter.class.getClassLoader().getResourceAsStream(path);
            assert fileInputStreamReader != null;
            byte[] bytes = fileInputStreamReader.readAllBytes();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
