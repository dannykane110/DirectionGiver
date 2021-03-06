package jt.directiongiver000;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by lp123 on 2017/8/21.
 */

public class functionList
{
    public static boolean shopFilter(String shopClass, boolean[] userselect){
            switch(shopClass)
            {
                case "便利商店":
                    if(!userselect[0]) return false;
                    System.out.println("刪除便利商店");
                    break;
                case "異國料理":
                    if(!userselect[1]) return false;
                    break;
                case "火烤料理":
                    if(!userselect[2]) return false;
                    break;
                case "中式美食":
                    if(!userselect[3]) return false;
                    break;
                case "夜市小吃":
                    if(!userselect[4]) return false;
                    break;
                case "甜點冰品":
                    if(!userselect[5]) return false;
                    break;
                case "伴手禮":
                    if(!userselect[6]) return false;
                    break;
                case "地方特產":
                    if(!userselect[7]) return false;
                    break;
                case "素食":
                    if(!userselect[8]) return false;
                    break;
                case "其他":
                    if(!userselect[9]) return false;
                    break;
            }
        return true;
    }

    public static String getWeather(String location,String date) throws IOException {
        String result = null;
        String weatherUrl = "http://140.121.197.130:8100/DG/GetWeather?location="+java.net.URLEncoder.encode(location, "UTF-8") +"&date="+date;
        Connection con = Jsoup.connect(weatherUrl).timeout(10000);
        Connection.Response resp = con.execute();
        Document doc = null;
        if (resp.statusCode() == 200)
        {
            doc = con.get();
        }
        result = doc.select("body").html().toString();
        System.out.println(result);
        return result;
    }

    public static Toilet[] getToilet(double userX,double userY) throws IOException {
                String toiletUrl = "http://140.121.197.130:8100/NearByServlet/GetWCServlet?longitude=" + userX + "&latitude=" + userY;
                Connection con = Jsoup.connect(toiletUrl).timeout(10000);
                Connection.Response resp = con.execute();
                Document doc = null;
                if (resp.statusCode() == 200) {
                    doc = con.get();
                }
                String result = doc.select("body").html().toString();

                Toilet[] tempToilet = null;
                try {
                    JSONArray jsonTotal = new JSONArray(result);
                    tempToilet = new Toilet[jsonTotal.length()];
                    if (jsonTotal.length() > 0)
                    {
                        for (int top = 0; top < jsonTotal.length(); top++)
                        {
                            JSONObject selectToilet = jsonTotal.getJSONObject(top);
                            String name = selectToilet.getString("NAME");
                            LatLng position = new LatLng(selectToilet.getDouble("PY"), selectToilet.getDouble("PX"));
                            String address = selectToilet.getString("ADDRESS");
                            String grade = selectToilet.getString("GRADE");
                            tempToilet[top] = new Toilet(name, position, address, grade);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(result);
                return tempToilet;
    }

    public static NPC[] getNPC(double userX,double userY) throws IOException {
        String npcUrl = "http://140.121.197.130:8100/NearByServlet/NPCServlet?longitude=" + userX + "&latitude=" + userY;
        Connection con = Jsoup.connect(npcUrl).timeout(10000);
        Connection.Response resp = con.execute();
        Document doc = null;
        if (resp.statusCode() == 200)
        {
            doc = con.get();
        }
        String result = doc.select("body").html().toString();

        NPC[] tempNPC = null;
        try {
            JSONArray jsonTotal = new JSONArray(result);
            tempNPC = new NPC[jsonTotal.length()];
            if (jsonTotal.length() > 0)
            {
                for (int top = 0; top < jsonTotal.length(); top++)
                {
                    JSONObject selectNPC = jsonTotal.getJSONObject(top);
                    String id = selectNPC.getString("NPC_ID");
                    String picURL = selectNPC.getString("NPC_PIC");
                    String name = selectNPC.getString("NPC_NAME");
                    LatLng position = new LatLng(selectNPC.getDouble("PY"), selectNPC.getDouble("PX"));
                    String mp3URL = selectNPC.getString("VOICE");
                    String intro = selectNPC.getString("NPC_INTRO");
                    NPCStory[] story = getNPCStory(id,"1");

                    //直接抓圖片存進去啦幹
                    byte[] data = functionList.getPic(picURL);
                    final Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                    tempNPC[top] = new NPC(id,name,position,picURL,mp3URL,bm,intro,story);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return tempNPC;
    }

    public static ConvenienceStore[] getConvenienceStore(double userX,double userY) throws IOException {
        String toiletUrl = "http://140.121.197.130:8100/NearByServlet/GetConServlet?longitude=" + userX + "&latitude=" + userY;
        Connection con = Jsoup.connect(toiletUrl).timeout(10000);
        Connection.Response resp = con.execute();
        Document doc = null;
        if (resp.statusCode() == 200) {
            doc = con.get();
        }
        String result = doc.select("body").html().toString();

        ConvenienceStore[] tempConvenienceStore = null;
        try {
            JSONArray jsonTotal = new JSONArray(result);
            tempConvenienceStore = new ConvenienceStore[jsonTotal.length()];
            if (jsonTotal.length() > 0)
            {
                for (int top = 0; top < jsonTotal.length(); top++)
                {
                    JSONObject selectConvenienceStore = jsonTotal.getJSONObject(top);
                    String name = selectConvenienceStore.getString("NAME");
                    LatLng position = new LatLng(selectConvenienceStore.getDouble("PY"), selectConvenienceStore.getDouble("PX"));
                    String address = selectConvenienceStore.getString("ADDRESS");
                    String ID = selectConvenienceStore.getString("ID");
                    tempConvenienceStore[top] = new ConvenienceStore(name, position, address,ID);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return tempConvenienceStore;
    }

    public static double GetJiaoDu(double lat1, double lng1, double lat2, double lng2)
    {
        double x1 = lng1;
        double y1 = lat1;
        double x2 = lng2;
        double y2 = lat2;
        double pi = Math.PI;
        double w1 = y1 / 180 * pi;
        double j1 = x1 / 180 * pi;
        double w2 = y2 / 180 * pi;
        double j2 = x2 / 180 * pi;
        double ret;
        if (j1 == j2)
        {
            if (w1 > w2) return 270;
            else if (w1 < w2) return 90;
            else return -1;
        }
        ret = 4 * Math.pow(Math.sin((w1 - w2) / 2), 2) - Math.pow(Math.sin((j1 - j2) / 2) * (Math.cos(w1) - Math.cos(w2)), 2);
        ret = Math.sqrt(ret);
        double temp = (Math.sin(Math.abs(j1 - j2) / 2) * (Math.cos(w1) + Math.cos(w2)));
        ret = ret / temp;
        ret = Math.atan(ret) / pi * 180;
        if (j1 > j2)
        {
            if (w1 > w2) ret += 180;
            else ret = 180 - ret;
        }
        else if (w1 > w2) ret = 360 - ret;
        return ret;
    }

    public static String getNearByJiaoDu(double JiaoDu)
    {
        if ((JiaoDu <= 10 ) || (JiaoDu > 350)) return "右邊";
        if ((JiaoDu > 10) && (JiaoDu <= 80)) return "右前方";
        if ((JiaoDu > 80) && (JiaoDu <= 100)) return "前面";
        if ((JiaoDu > 100) && (JiaoDu <= 170)) return "左前方";
        if ((JiaoDu > 170) && (JiaoDu <= 190)) return "左邊";
        if ((JiaoDu > 190) && (JiaoDu <= 260)) return "左後方";
        if ((JiaoDu > 260) && (JiaoDu <= 280)) return "後面";
        if ((JiaoDu > 280) && (JiaoDu <= 350)) return "右後方";
        return null;
    }
    public static String stringParser(String url) throws IOException
    {
        String url2 = new String();
        for (int j = 0; j < url.length(); j++)
        {
            if (url.substring(j, j + 1).matches("[\\u4e00-\\u9fa5]+"))
            {
                try
                {
                    url2 = url2 + URLEncoder.encode(url.substring(j, j + 1),"UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                url2 = url2 + url.substring(j, j + 1).toString();
            }
        }

        return url2;
    }
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double EARTH_RADIUS = 6378137;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static boolean checkServerStatus(){
        String result = null;
        String weatherUrl = "http://140.121.197.130:8100/DG/Check";
        Connection con = Jsoup.connect(weatherUrl).timeout(10000);
        Connection.Response resp = null;
        try
        {
            resp = con.execute();
            Document doc = null;
            if (resp.statusCode() == 200)
            {
                doc = con.get();
            }
            result = doc.select("body").html().toString();
            System.out.println(result);
            if(result.equals("1"))
                return true;
            return true;
        }
        catch (IOException e)
        {

        }
        return false;
    }

    public static byte[] getPic(String urlpath)
    {
        try
        {
            InputStream inputstream;
            URL url = null;
            url = new URL(urlpath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            inputstream = conn.getInputStream();
            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while((len = inputstream.read(buffer)) != -1){
                outputstream.write(buffer,0,len);
            }
            inputstream.close();
            outputstream.close();
            return outputstream.toByteArray();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static NPCStory[] getNPCStory(String ID,String STATUS) throws IOException {
        String npcUrl = "http://140.121.197.130:8100/NearByServlet/SpeechServlet?ID="+ID+"&STATUS="+STATUS;
        Connection con = Jsoup.connect(npcUrl).timeout(10000);
        System.out.println("連線 : "+ npcUrl);
        Connection.Response resp = con.execute();
        Document doc = null;
        if (resp.statusCode() == 200)
        {
            doc = con.get();
        }
        String result = doc.select("body").html().toString();

        NPCStory[] tempStory = null;
        try {
            JSONArray jsonTotal = new JSONArray(result);
            tempStory = new NPCStory[jsonTotal.length()];
            if (jsonTotal.length() > 0)
            {
                for (int top = 0; top < jsonTotal.length(); top++)
                {
                    JSONObject selectNPC = jsonTotal.getJSONObject(top);
                    String id = selectNPC.getString("NPC_ID");
                    String status = selectNPC.getString("STATUS");
                    String mp3URL = selectNPC.getString("VOICE");
                    String content = selectNPC.getString("CONTENT");
                    tempStory[top] = new NPCStory(id,mp3URL,content,status);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return tempStory;
    }

}
