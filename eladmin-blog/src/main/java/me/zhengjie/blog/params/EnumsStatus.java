package me.zhengjie.blog.params;

public enum  EnumsStatus {
    ENABLE_STATUS_TRUE("T","上架"),
    ENABLE_STATUS_FALSE("F","上架");

    private String key;

    private String value;

    EnumsStatus(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public EnumsStatus setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public EnumsStatus setValue(String value) {
        this.value = value;
        return this;
    }
}
