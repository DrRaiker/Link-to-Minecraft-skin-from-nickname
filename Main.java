import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        try {
            String name = "DrRaiker";

            // Первый запрос для получения ID пользователя
            String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

                JSONObject json = new JSONObject(content.toString());
                String id = json.getString("id");

                // Второй запрос для получения информации о сессии пользователя
                url = "https://sessionserver.mojang.com/session/minecraft/profile/" + id;
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");

                status = con.getResponseCode();
                if (status == 200) {
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    con.disconnect();

                    json = new JSONObject(content.toString());
                    JSONArray properties = json.getJSONArray("properties");
                    String value = properties.getJSONObject(0).getString("value");

                    // Декодирование Base64
                    byte[] decodedBytes = Base64.getDecoder().decode(value);
                    String decodedString = new String(decodedBytes);

                    // Получение URL скина
                    json = new JSONObject(decodedString);
                    String skinUrl = json.getJSONObject("textures").getJSONObject("SKIN").getString("url");

                    System.out.println("URL скина: " + skinUrl);
                } else {
                    System.out.println("Не удалось получить информацию о сессии пользователя.");
                }
            } else {
                System.out.println("Не удалось получить ID пользователя.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
