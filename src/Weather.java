import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Weather {
    private Document document;

    public String[] tempetarures(){
        Elements elements = document.getElementsByClass("today-daypart-temp");
        String temps = elements.text();
        String delimeter = " ";
        String subStr[];
        subStr = temps.split(delimeter);
        return subStr;
    }

    public Weather() {
        connect();
    }


    private void connect() {
        try {
            document = Jsoup.connect("https://weather.com/uk-UA/weather/today/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getTitle() {
        return document.title();
    }

    public String getDate() {
        Elements elements = document.getElementsByClass("today_nowcard-timestamp");
        return elements.text();
    }

    public String getWeathernow() {
        Elements elements = document.getElementsByClass("today_nowcard-temp");
        return elements.text();
    }

    public String getWeatherFeel() {
        Elements elements = document.getElementsByClass("today_nowcard-feels");
        return elements.text();
    }

    public String getWeatherType() {
        Elements elements = document.getElementsByClass("today_nowcard-phrase");
        return elements.text();
    }

    public String getWeatherForecastDay() {
        Element element = document.getElementById("dp0-daypartName");
        return element.text();
    }

    public String getWeatherForecastType(){
        Element element = document.getElementById("dp0-phrase");
        return element.text();
    }


    public String getWeatherForecastTemp() {
        return tempetarures()[0];
    }

    public String getWeatherForecastNight(){
        Element element = document.getElementById("dp1-daypartName");
        return element.text();
    }

    public String getWeatherForecastNightType(){
        Element element1 = document.getElementById("dp1-phrase");
        return element1.text();
    }

    public String getWeatherForecastTemp2(){
        return tempetarures()[1];
    }

    public String getWeatherTomorrowTemp(){
        return tempetarures()[2];
    }

    public String getWeatherTomorrowType(){
        Element element = document.getElementById("dp2-phrase");
        return element.text();
    }
    public String getWeatherTomorrowNightType(){
        Element element = document.getElementById("dp3-phrase");
        return element.text();
    }

    public String getWeatherTomorrowNightTemp(){
        return tempetarures()[3];
    }

}
