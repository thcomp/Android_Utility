package jp.co.thcomp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtil {
    public static String getString(InputStream stream){
        StringBuilder ret = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(stream);
        char[] readBuffer = new char[1024];
        int readSize = 0;

        try {
            while((readSize = reader.read(readBuffer)) > 0){
                ret.append(readBuffer, 0, readSize);
            }
        } catch (IOException e) {
            LogUtil.e(StreamUtil.class.getSimpleName(), e.getLocalizedMessage());
            return null;
        }

        return ret.toString();
    }
}
