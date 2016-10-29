package com.ts.mobilelab.goggles4u.apis;

import org.json.JSONObject;

/**
 * Created by Abhishek on 15-Oct-16.
 */
public class WebData {

    private String result;
    private JSONObject sendJson;
    private JSONObject receiveJson;
    private int code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JSONObject getSendJson() {
        return sendJson;
    }

    public void setSendJson(JSONObject sendJson) {
        this.sendJson = sendJson;
    }

    public JSONObject getReceiveJson() {
        return receiveJson;
    }

    public void setReceiveJson(JSONObject receiveJson) {
        this.receiveJson = receiveJson;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
