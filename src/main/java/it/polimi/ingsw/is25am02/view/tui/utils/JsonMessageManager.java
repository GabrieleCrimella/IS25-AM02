package it.polimi.ingsw.is25am02.view.tui.utils;

import com.google.gson.*;

import java.io.*;
import java.util.Map;

public class JsonMessageManager {
    private final JsonObject messages;

    public JsonMessageManager(String resourcePath) throws Exception {
        Gson gson = new Gson();

        // Usa getResourceAsStream invece di FileReader
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Risorsa non trovata: " + resourcePath);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            this.messages = gson.fromJson(reader, JsonObject.class);
        }
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
