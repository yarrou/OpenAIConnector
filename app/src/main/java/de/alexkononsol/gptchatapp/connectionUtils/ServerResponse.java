package de.alexkononsol.gptchatapp.connectionUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class ServerResponse {
    private Object data;
    private int code;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ServerResponse() {
    }

    public ServerResponse(Object data, int code) {
        this.data = data;
        this.code = code;
    }
    private Object getObjectData(Type type){
        Gson gson = new Gson();
        Object object = gson.fromJson(data.toString(), type);
        return object;
    }

}
