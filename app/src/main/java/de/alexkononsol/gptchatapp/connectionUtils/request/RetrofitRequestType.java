package de.alexkononsol.gptchatapp.connectionUtils.request;

public enum RetrofitRequestType {
    REGISTRATION(1),
    LOGIN(2);

    public final int type;

    private RetrofitRequestType(int type) {
        this.type = type;
    }
}
