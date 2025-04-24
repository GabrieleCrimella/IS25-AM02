package it.polimi.ingsw.is25am02.view.tui.utils;

import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JsonMessageManager {
    private final JsonObject messages;

    public JsonMessageManager(String filePath) throws Exception {
        //JsonElement root = JsonParser.parseReader(new FileReader(filePath));
        //JsonObject jsonObject= root.getAsJsonObject();
        //this.messages = jsonObject.getAsJsonObject("messages");
        Gson gson = new Gson();
        this.messages = gson.fromJson(new FileReader(filePath), JsonObject.class);
    }

    private String getMessage(String[] keys) {
        JsonElement current = messages;
        for (String key : keys) {
            if (!current.getAsJsonObject().has(key)) {
                return "[Not found message: " + String.join(".", keys) + "]";
            }
            current = current.getAsJsonObject().get(key);
        }
        return current.getAsString();
    }

    public String getMessageWithParams(String keys, Map<String, String> params) {
        String[] keysArray = keys.split("\\.");
        String msg = getMessage(keysArray);
        if (msg == null) return null;

        if(params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return msg;
    }
}
