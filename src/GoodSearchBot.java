import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class GoodSearchBot extends TelegramLongPollingBot {
    final String BOT_NAME = "goodsearchbot";
    final String BOT_TOKEN;
    private Weather weather = new Weather();
    private long chat_id;
    Timer timer = new Timer();

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); //menu

    @Override
    public void onUpdateReceived(Update update) {
        update.getUpdateId();

        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
         chat_id = update.getMessage().getChatId();
         String text = update.getMessage().getText();
         sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(processMessage(update.getMessage().getText()));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public String getWeatherForecast(){
        String info = weather.getWeatherForecastDay()
                +"\n"+weather.getWeatherForecastType()
                +"\n"+"Температура повiтря: "+weather.getWeatherForecastTemp()
                +"\n"
                +"\n"+weather.getWeatherForecastNight()
                +"\n"+weather.getWeatherForecastNightType()
                +"\n"+"Температура повiтря: "+weather.getWeatherForecastTemp2()
                +"\n"
                +"\n"+"Завтра"
                +"\n"+weather.getWeatherTomorrowType()
                +"\n"+"Температура повiтря: "+weather.getWeatherTomorrowTemp()
                +"\n"
                +"\n"+"Завтра вночi"
                +"\n"+weather.getWeatherTomorrowNightType()
                +"\n"+"Температура повiтря: "+weather.getWeatherTomorrowNightTemp();
        return info;

    }

    public String GetWeatherInfo(){

        String advice="";
        //empty string to change it in "if"

        String info = weather.getTitle()
                +"\n"+weather.getDate()
                +"\nТемпература повiтря "+weather.getWeathernow()+"C"
                +"\n"+weather.getWeatherFeel()+"C"
                +"\n"+weather.getWeatherType();

            String tempString ="";
            String tempReceived = weather.getWeathernow();
            char[] tempCharArray = tempReceived.toCharArray();

            for(int i = 0;i<tempCharArray.length;i++){

                if(Character.isDigit(tempCharArray[i])){
                    tempString+=tempCharArray[i];
                    //add digits in char to string
                }
            }

            int tempInt = Integer.parseInt(tempString);

            if (tempInt<=0)
                advice="Там очень холодно,не вылазь из дома или в очень тёплой куртке и шапке";
            if(tempInt>0&&tempInt<10)
                advice="Холодно,  советую надеть тёплую куртку и ботинки,не забудь шапку";
            if(tempInt>=10&&tempInt<20)
                advice="Тепленько,но в футболке ходить не рискуй ,можешь снять куртку и выйти в свитере(спрячь пока новые шлёпки)";
            if(tempInt>=20&&tempInt<=26)
                advice="Жара,надевай шорты и лёгкие кроссы";
            if(tempInt>=26)
                advice="Я даже не знаю ,что сказать,держись там ,что-ли ,сядь под кондицеонер(ад)";

            String umbrellaAdvice ="";

            if(weather.getWeatherType().contains("Дождь")||weather.getWeatherType().contains("Дощ"))
                umbrellaAdvice="И не заюудь про зонтик";

            return info+"\n"+advice+"\n"+umbrellaAdvice;
    }

         public String setTimer(int hour){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY,hour);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.SECOND,0);


        TimerTask getWeather = new TimerTask() {
            @Override
            public void run() {
                getWeatherForecast();
            }
        };
        timer.scheduleAtFixedRate(getWeather,today.getTime(), TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS));
        return "Дякую за користування ботом)"
                +"\n"+"щоб не отримувати повiдомлення,напишiть </stopt>";
    }


    public String processMessage (String msg){

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        if (msg.equals("/start")){
            keyboard.clear();
            keyboardRow.add("\uD83C\uDF25Погода");
            keyboardRow.add("\uD83D\uDCCCОтримувати повiдомлення зранку");
            keyboard.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Обрати...";
        }



        if (msg.equals("\uD83C\uDF25Погода")||msg.equals("/weather")){
            keyboard.clear();
            keyboardRow.clear();
            keyboardRow.add("\uD83C\uDF25Погода на даний момент");
            keyboardRow.add("\uD83C\uDF25Прогноз погоди");
            keyboard.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Обрати...";
        }

        if (msg.equals("\uD83C\uDF25Погода на даний момент"))
        return GetWeatherInfo();
        if (msg.equals("\uD83C\uDF25Прогноз погоди"))
            return getWeatherForecast();
        if (msg.equals("\uD83D\uDCCCОтримувати повiдомлення зранку")) {
            keyboard.clear();
            keyboardRow.clear();
            keyboardRow.add("\uD83C\uDF1DПерша половина дня");
            keyboardRow.add("\uD83C\uDF1A Друга половина дня");
            keyboard.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Оберіть, коли ви бажаєте отримувати повідомлення";
        }

        if(msg.equals("\uD83C\uDF1DПерша половина дня")) {
            keyboard.clear();
            keyboardRow.clear();
            keyboardRow2.clear();
            keyboardRow.add("6:00");
            keyboardRow.add("7:00");
            keyboardRow.add("8:00");
            keyboardRow2.add("9:00");
            keyboardRow2.add("10:00");
            keyboardRow2.add("11:00");
            keyboard.add(keyboardRow);
            keyboard.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Обеpiть час...";
        }
        if (msg.equals("6:00"))
            setTimer(6);
        if (msg.equals("7:00"))
            setTimer(7);
        if (msg.equals("8:00"))
            setTimer(8);
        if (msg.equals("9:00"))
            setTimer(9);
        if (msg.equals("10:00"))
            setTimer(10);
        if (msg.equals("11:00"))
            setTimer(11);
        if (msg.equals("19:00"))
            setTimer(19);
        if (msg.equals("20:00"))
            setTimer(20);
        if (msg.equals("21:00"))
            setTimer(21);
        if (msg.equals("22:00"))
            setTimer(22);
        if (msg.equals("23:00"))
            setTimer(23);
        if (msg.equals("24:00"))
            setTimer(24);
        if(msg.equals("/stopt")) {
            timer.cancel();
        return "Повiдомлення вiдмiненi";
        }

        if(msg.equals("\uD83C\uDF1A Друга половина дня")){
            keyboard.clear();
            keyboardRow.clear();
            keyboardRow2.clear();
            keyboardRow.add("19:00");
            keyboardRow.add("20:00");
            keyboardRow.add("21:00");
            keyboardRow2.add("22:00");
            keyboardRow2.add("23:00");
            keyboardRow2.add("24:00");
            keyboard.add(keyboardRow);
            keyboard.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Обеpiть час...";
        }
            return null;
    }
}