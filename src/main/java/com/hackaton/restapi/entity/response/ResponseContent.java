package com.hackaton.restapi.entity.response;

public class ResponseContent {
    private boolean success;
    private String message;
    private Object data;
    private Object meta;
    private Object links;

    public ResponseContent() {
    }

    public ResponseContent(boolean success,
            String message,
            Object data,
            Object meta,
            Object links) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = meta;
        this.links = links;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getMeta() {
        return this.meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public Object getLinks() {
        return this.links;
    }

    public void setLinks(Object links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "{" +
                " success='" + isSuccess() + "'" +
                ", message='" + getMessage() + "'" +
                ", data='" + getData() + "'" +
                ", meta='" + getMeta() + "'" +
                ", links='" + getLinks() + "'" +
                "}";
    }

}