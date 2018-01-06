import com.google.gson.*;
import jdk.nashorn.internal.objects.NativeJSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sciense_IT {

    ArrayList<Object> response;

    public Sciense_IT(ArrayList<Object> response2) {
        this.response = response2;
    }


    public static void main(String[] args) throws IOException{
next:
        try{
        Pattern pattern;
        Document DatafromVK = null, DatafromTelegramm = null, zapros;
        String messageBot = null, message_user, command_bot_subject = null, command_bot_lvl = null, lvl = "", messageBot1 = null, inf, body, DMYzadForm, DMYfromVK = null, year, messageData = null, MVK = null, dayVK = null, sem = "семестр", kurs = "курс";
        boolean a = true;
        int idPolzovatela, zaprosID, EndInd = 0;
        Date DMYfromCalendar = new Date();
            // DatafromVK - данные из контакта;
        // DatafromTelegramm - данные из телеграмма;
        // zapros - переменная, созданная для удаления запроса пользователя;
        // messageBot - сообщение, отправляемое пользователю;
        // messageBot1 - сообщение, подготавливаемое, для отправления к пользователю;
        // command_bot_subject - искомый предмет;
        // command_bot_lvl - искомое число(семестра или курса);
        // lvl - курс/семестр;
        // a - вечный цикл;
        // idPolzovatela - id пользователя, от которого исходит запрос;
        // zaprosID - id запроса, для удаления;
        // inf - массив информации подгружаемой из телеграмма;
        // body - массив информации подгружаемой из вк;
        // DMYfromCalendar - текущая дата;
        // DMYfromVK - дата из вк;

        DatafromTelegramm = Jsoup.connect("https://api.telegram.org/bot306660493:AAGSSiJLk_FTCmvLGiReAEXcTTuUcMsygeY/getUpdates").ignoreContentType(true).get();
        inf = DatafromTelegramm.text();
        Gson gson1 = new Gson();
        TelegramGetEntry info = gson1.fromJson(inf, TelegramGetEntry.class);

        while (a) {

            for (int i = 0; i < info.result.size(); i++)
            {
                idPolzovatela = info.result.get(i).message.chat.id;
                message_user = info.result.get(i).message.text;

                pattern = Pattern.compile("(.+) ([0-9]{0,1}) (семестр|курс)");
                Matcher matcher1 = pattern.matcher(message_user);
                while (matcher1.find()) {
                    command_bot_subject = matcher1.group(1);
                    command_bot_lvl = matcher1.group(2);
                    lvl = matcher1.group(3);
                }

                DatafromVK = Jsoup.connect("https://api.vk.com/method/wall.get?owner_id=-142218276&domain=club142218276&access_token=13b81f3c13b81f3c13b81f3c1f13e77fbd113b813b81f3c49ba116f602082b8346cf183").ignoreContentType(true).get();
                //DatafromVK = Jsoup.connect("https://api.vk.com/method/wall.get?owner_id=-156411134&domain=club156411134&access_token=13b81f3c13b81f3c13b81f3c1f13e77fbd113b813b81f3c49ba116f602082b8346cf183").ignoreContentType(true).get();
                body = DatafromVK.body().html();
                body = body.replaceAll("\n", "");
                JSONArray information_in_VK = new JSONObject(body).getJSONArray("response");

                if (message_user.equals("Информатика")) {
                    command_bot_subject = "Информатика";
                    command_bot_lvl = null;
                    lvl = null;
                }

                if (message_user.equals("Инженерная графика")) {
                    command_bot_subject = "Инженерная графика";
                    command_bot_lvl = null;
                    lvl = null;
                }

                for (int j = 2; j < information_in_VK.length(); j++)
                {
                    String pos = String.valueOf(information_in_VK.getJSONObject(j).get("text"));
                    System.out.println(pos);
                    pattern = Pattern.compile(command_bot_subject + ".{0,10}" + command_bot_lvl + ".{1,10}" + lvl + ".{1,5} ([1-3]{0,1}[0-9][\\.][0-1][0-9][\\.][^\\.;]{1,})[;|\\.]");
                    Matcher matcher2 = pattern.matcher(pos);
                    while(matcher2.find())
                        messageBot1 = matcher2.group(1);

                    if(messageBot1 == null)
                    {
                        if (lvl != null) {
                            if (lvl.equals(sem)) {
                                pos = pos.replaceAll("семестр", "Q");
                                System.out.println("Замена семестра:" + pos);
                                lvl = "Q";
                            }
                            if (lvl.equals(kurs)) {
                                pos = pos.replaceAll("курс", "K");
                                lvl = "K";
                                System.out.println("Замена курса:" + pos);
                            }
                        }
                        if (lvl == null || command_bot_lvl == null)
                        {
                            lvl = "";
                            command_bot_subject = "";
                        }
                        pos = pos.replaceAll("\\..{0,1}<br>", "Z");
                        System.out.println(pos);
                        pattern = Pattern.compile(command_bot_subject + ".*" + command_bot_lvl + ".{0,15}" + lvl + "([^QKZ]{1,})");
                        Matcher matcher = pattern.matcher(pos);
                        while (matcher.find()) {
                            messageBot1 = matcher.group(1);
                            EndInd = matcher.end();
                        }
                        if (pos.charAt(EndInd) == 'Z')
                            messageBot1 = messageBot1 + ".";

                        if (messageBot1 != null) {
                            System.out.println("Я вывожу messageBot1 " + messageBot1);

                            if (lvl.equals("K"))
                                messageBot1 = messageBot1.substring(2);
                            if (lvl.equals("Q"))
                                messageBot1 = messageBot1.substring(1);

                            pattern = Pattern.compile(".*[;|.]");
                            Matcher matcher3 = pattern.matcher(messageBot1);

                            while (matcher3.find())
                                EndInd = matcher3.end();

                            messageBot1 = messageBot1.substring(0,EndInd-1);
                            messageBot1 = messageBot1.replaceAll(";", "");
                        }
                    }

                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                    DMYzadForm =  formatForDateNow.format(DMYfromCalendar);

                    SimpleDateFormat formatForDateNow2 = new SimpleDateFormat("yyyy");
                    year =  formatForDateNow2.format(DMYfromCalendar);

                    if (messageBot1 != null)
                    messageBot = messageBot1;

                    /*if (messageBot1 != null) {
                        pattern = Pattern.compile("([1-3]{0,1}[0-9]{1})[\\.]([0-1]{0,1}[0-9]{1})[\\.]" + year);
                        Matcher matcher3 = pattern.matcher(messageBot1);
                        while (matcher3.find()) {
                            dayVK = matcher3.group(1);
                            MVK = matcher3.group(2);
                        }

                        SimpleDateFormat formatForDateNow3 = new SimpleDateFormat("dd");
                        String dayCalendar = formatForDateNow3.format(DMYfromCalendar);


                        SimpleDateFormat formatForDateNow4 = new SimpleDateFormat("MM");
                        String MCalendar = formatForDateNow4.format(DMYfromCalendar);


                        if (dayVK != null && MVK != null)
                            if ((Integer.parseInt(dayCalendar) < Integer.parseInt(dayVK)) && (Integer.parseInt(MCalendar) <= Integer.parseInt(MVK)) || (Integer.parseInt(MCalendar) < Integer.parseInt(MVK)))
                                messageBot = messageBot1;*/

                        if (messageBot != null)
                            break;
                    //}
                }
                String f;
                if (messageBot == null) {
                    f = "Пересдача не предусмотрена.";
                } else {
                    f = messageBot;
                }
                f = f.replaceAll("<br>", "\n");
                String n = URLEncoder.encode(f, StandardCharsets.UTF_8.toString());
                DatafromVK = Jsoup.connect("https://api.telegram.org/bot306660493:AAGSSiJLk_FTCmvLGiReAEXcTTuUcMsygeY/sendMessage?chat_id=" + idPolzovatela + "&text=" + n).ignoreContentType(true).get();
                zaprosID = info.result.get(i).update_id + 1;
                zapros = Jsoup.connect("https://api.telegram.org/bot306660493:AAGSSiJLk_FTCmvLGiReAEXcTTuUcMsygeY/getUpdates?offset=" + zaprosID).ignoreContentType(true).get();
                messageBot = null;
                f = null;
                command_bot_subject = null;
                command_bot_lvl = "";
                lvl = "";
                messageBot1 = null;
            }
            DatafromTelegramm = Jsoup.connect("https://api.telegram.org/bot306660493:AAGSSiJLk_FTCmvLGiReAEXcTTuUcMsygeY/getUpdates").ignoreContentType(true).get();
            inf = DatafromTelegramm.text();
            info = gson1.fromJson(inf, TelegramGetEntry.class);
        }}
        catch (SocketTimeoutException e)
        {
            break next;
        }
    }

}

class Respons
{
    public int id, from_id, to_id, marked_as_ads, is_pinned;
    double date;
    public String post_type, text;
    public media media;
    public attachment attachment;
    public attachments attachments;
    public post_source post_source;
    public comments comments;
    public likes likes;
    public reposts reposts;
    public int online;
    public int  reply_count;

    public Respons(int id1, int from_id1, int to_id1, double date1, int marked_as_ads1, int is_pinned1,String post_type1, String text1, media media1, attachment attachment1, attachments attachments1, post_source post_source1, comments comments1, likes likes1, reposts reposts1, int online1, int reply_count1)
    {
        this.id = id1;
        this.from_id = from_id1;
        this.to_id = to_id1;
        this.date = date1;
        this.marked_as_ads = marked_as_ads1;
        this.is_pinned = is_pinned1;
        this.post_type = post_type1;
        this.text = text1;
        this.media = media1;
        this.attachment = attachment1;
        this.attachments = attachments1;
        this.post_source = post_source1;
        this.comments = comments1;
        this.likes = likes1;
        this.reposts = reposts1;
        this.online = online1;
        this.reply_count = reply_count1;
    }

}

class media
{
    public String type, thumb_src;
    public double owner_id, item_id;
    public media(String type1, String thumb_src1, double owner_id1, double item_id1)
    {
        this.type = type1;
        this.thumb_src = thumb_src1;
        this.owner_id = owner_id1;
        this.item_id = item_id1;
    }

}

class attachment
{
    public String type;
    public photo photo;
    public attachment(String type1, photo photo1)
    {
        this.type = type1;
        this.photo = photo1;
    }
}

class attachments
{
    public String type;
    public photo photo;
    public attachments(String type1, photo photo1)
    {
        this.type = type1;
        this.photo = photo1;
    }
}

class post_source
{
    public String type;
    public post_source(String type1)
    {
        this.type = type1;
    }
}

class comments
{
    public int count, can_post;
    public comments(int count1, int can_post1)
    {
        this.count = count1;
        this.can_post = can_post1;
    }
}

class likes
{
    public int count, user_likes, can_like, can_publish;
    public likes(int count1, int user_likes1, int can_like1, int can_publish1)
    {
        this.count = count1;
        this.user_likes = user_likes1;
        this.can_like = can_like1;
        this.can_publish = can_publish1;
    }

}

class reposts
{
    public int count, user_reposted;
    public reposts(int count1, int user_reposted1)
    {
        this.count = count1;
        this.user_reposted = user_reposted1;
    }
}

class photo
{
    public  double pid, owner_id, created;
    public int aid, user_id, width, height;
    public String src, src_big, src_small, src_xbig, src_xxbig, text, access_key;
    public photo( double pid1, int aid1, double owner_id1, int user_id1, int width1, int height1, double created1, String src1, String src_big1, String src_small1, String src_xbig1, String src_xxbig1, String text1, String access_key1)
    {
        this.pid = pid1;
        this.aid = aid1;
        this.owner_id = owner_id1;
        this.user_id = user_id1;
        this.width = width1;
        this.height = height1;
        this.created = created1;
        this.src = src1;
        this.src_big = src_big1;
        this.src_small = src_small1;
        this.src_xbig = src_xbig1;
        this.src_xxbig = src_xxbig1;
        this.text = text1;
        this.access_key = access_key1;
    }
}

class resultat
{
    public int update_id;
    public message message;

    public resultat(int update_id2, message message2)
    {
        this.update_id = update_id2;
        this.message = message2;
    }
}

class message
{
    public int message_id;
    public from from;
    public chat chat;
    public int date;
    public String text;

    public message(int message_id2, from from2, chat chat2, int date2, String text2)
    {
        this.message_id = message_id2;
        this.from = from2;
        this.chat = chat2;
        this.date = date2;
        this.text = text2;
    }

}
class resultatik
{
    public int message_id;
    public from from;
    public chat chat;
    public int date;
    public String text;

    public resultatik(int a, from s, chat d, int f, String g)
    {
        this.message_id = a;
        this.from = s;
        this.chat = d;
        this.date = f;
        this.text = g;
    }
}

class from
{
    public int id;
    public boolean is_bot;
    public String first_name;
    public String username;

    public from(int id2, boolean is_bot2, String first_name2, String username2)
    {
        this.id = id2;
        this.is_bot = is_bot2;
        this.first_name = first_name2;
        this.username = username2;
    }



}

class chat
{
    public int id;
    public String first_name;
    public String last_name;
    public String type;

    public chat(int id2, String first_name2, String last_name2, String type2)
    {
        this.id = id2;
        this.first_name = first_name2;
        this.last_name = last_name2;
        this.type = type2;
    }
}

class TelegramGetEntry
{
    public boolean ok;
    public ArrayList<resultat> result;
    public TelegramGetEntry(boolean ok1, ArrayList<resultat> result1)
    {
        this.ok = ok1;
        this.result = result1;
    }

}


// когда-то пробовали

        /*//System.out.println(jr.response.get(1));
        //
        String b = jr.response.get(1).toString();
        System.out.println(b);
        Gson gson1 = new Gson();
        //b1 = Jsoup.parse(b);
        //String inf = gson1.toJson(b, Respons.class);
        Respons bbb = gson1.fromJson(b, Respons.class);
        System.out.println(bbb.text);*/

