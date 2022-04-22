package io.github.isaac.model;


import io.github.isaac.TextUtil;

public class DataModel {
    private String id;
    private String valueSimpleCn;
    private String valueTraditionalCn;
    private String valueEnglish;

    public DataModel(String id, String simpleCN, String traditionalCN , String english) {
        this.id = id;
        this.valueSimpleCn = simpleCN;
        this.valueTraditionalCn = traditionalCN;
        this.valueEnglish = english;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValueSimpleCn() {
        return valueSimpleCn;
    }

    public void setValueSimpleCn(String valueSimpleCn) {
        this.valueSimpleCn = valueSimpleCn;
    }

    public String getValueTraditionalCn() {
        return valueTraditionalCn;
    }

    public void setValueTraditionalCn(String valueTraditionalCn) {
        this.valueTraditionalCn = valueTraditionalCn;
    }

    public String getValueEnglish() {
        return valueEnglish;
    }

    public void setValueEnglish(String valueEnglish) {
        this.valueEnglish = valueEnglish;
    }

    public String getValueByLanguages(SupportedLanguages languages) {
        switch (languages) {
            case Chinese_Simplified:
                return getValueSimpleCn();
            case Chinese_Traditional:
                return getValueTraditionalCn();
            case English:
            case DefaultLocal:
                return getValueEnglish();
            default:
                return "";
        }
    }

    public boolean isDataIllegal() {
        return TextUtil.isEmpty(id)
                || TextUtil.isEmpty(valueSimpleCn)
                || TextUtil.isEmpty(valueTraditionalCn)
                || TextUtil.isEmpty(valueEnglish);
    }
}
