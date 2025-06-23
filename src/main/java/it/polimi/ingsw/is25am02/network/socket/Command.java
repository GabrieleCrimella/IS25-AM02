package it.polimi.ingsw.is25am02.network.socket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class Command {
    private String command;
    private JsonObject params;

    public Command(String command, JsonObject params) {
        this.command = command;
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public JsonObject getParams() {
        return params;
    }

    public <T> T getParameter(String key, Class<T> clazz, Gson gson) {
        JsonElement elem = params.get(key);
        return (elem != null) ? gson.fromJson(elem, clazz) : null;
    }

    public <T> T getParameter(String key, Type type, Gson gson) {
        JsonElement elem = params.get(key);
        return (elem != null) ? gson.fromJson(elem, type) : null;
    }
}
