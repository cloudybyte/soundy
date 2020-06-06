package net.cloudybyte.bot.core.audio.youtube;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;

public class SearchYoutube {

    public static String searchyt(String query) {


        String url = "http://www.youtube.com/results";

        //TODO IF FIRST RESULT == AD => THE AD WILL BE PLAYED

        Random random = new Random();

        int rndNumber = random.nextInt(3);

        Connection con = Jsoup.connect(url)
                .data("search_query", query);

        switch (rndNumber) {
            case 0:
                con = con.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Sec-Fetch-Mode", "navigate")
                        .header("Sec-Fetch-Site", "cross-site")
                        .header("Sec-Fetch-User", "?1")
                        .header("Dnt", "1")
                        .referrer("https://amazon.com")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                break;

            case 1:
                con = con.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")
                        .header("Connection", "keep-alive")
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .referrer("https://google.com")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                break;

            case 2:
                con = con.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36 OPR/68.0.3618.125")
                        .header("Accept-Language", "q=0.9,en-US;q=0.8,en;q=0.7")
                        .referrer("https://facebook.com")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                break;

            default:
                con = con.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")
                        .header("Connection", "keep-alive")
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .referrer("https://wikipedia.org")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                break;
        }


        Document doc = null;
        try {
            doc = con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new StringReader(doc.html()));

        String line = null;
        ArrayList<String> allLinks = new ArrayList<String>();
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains("watch?v=")) {
                    String substring = line;
                    substring = substring.substring(substring.indexOf("watch?") - 1, substring.indexOf("watch?") + 19);
                    allLinks.add(substring);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (allLinks.isEmpty()){
            return null;
        }


        //get multiple results
        for (int i = 0; i < allLinks.size(); i++) {
            allLinks.set(i, "https://youtube.com" + allLinks.get(i));
        }
        return allLinks.get(0);
    }
}
