package com.netural.remoteappcontrol.library.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by stephan.schober on 27.05.15.
 */
public class NetworkUtils {

    public static long checkLastUpdate(String url) throws IOException {
        URL u = new URL(url);
        URLConnection uc = u.openConnection();
        uc.setUseCaches(false);
        return uc.getLastModified();
    }

    public static String fetchUrl(String url) throws IOException {
        URL u = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        u.openStream()));

        String inputLine;
        StringBuilder sb = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }

        in.close();

        return sb.toString();
    }
}
