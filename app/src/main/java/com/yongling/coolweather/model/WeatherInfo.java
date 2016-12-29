package com.yongling.coolweather.model;

/**
 * Created by yongling on 2016/7/19.
 */
public class WeatherInfo {

    private String city;
    private String conditiontxt;
    private String conditiontxt2;
    private String conditiontxt3;
    private String condition;
    private String condition2;
    private String condition3;
    private String tmpnow;
    private String tmpmax;
    private String tmpmin;
    private String tmpmax2;
    private String tmpmin2;
    private String tmpmax3;
    private String tmpmin3;
    private String updatetime;


    public WeatherInfo() {
    }

    public String getCity() {
        return city;
    }

    public String getCondition() {
        return condition;
    }

    public String getTmpnow() {
        return tmpnow;
    }

    public String getTmpmax() {
        return tmpmax;
    }

    public String getTmpmin() {
        return tmpmin;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setTmpmax(String tmpmax) {
        this.tmpmax = tmpmax;
    }

    public void setTmpnow(String tmpnow) {
        this.tmpnow = tmpnow;
    }

    public void setTmpmin(String tmpmin) {
        this.tmpmin = tmpmin;
    }

    public String getCondition2() {
        return condition2;
    }

    public void setCondition2(String condition2) {
        this.condition2 = condition2;
    }

    public String getCondition3() {
        return condition3;
    }

    public void setCondition3(String condition3) {
        this.condition3 = condition3;
    }

    public String getTmpmax2() {
        return tmpmax2;
    }

    public void setTmpmax2(String tmpmax2) {
        this.tmpmax2 = tmpmax2;
    }

    public String getTmpmin2() {
        return tmpmin2;
    }

    public void setTmpmin2(String tmpmin2) {
        this.tmpmin2 = tmpmin2;
    }

    public String getTmpmax3() {
        return tmpmax3;
    }

    public void setTmpmax3(String tmpmax3) {
        this.tmpmax3 = tmpmax3;
    }

    public String getTmpmin3() {
        return tmpmin3;
    }

    public void setTmpmin3(String tmpmin3) {
        this.tmpmin3 = tmpmin3;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getConditiontxt() {
        return conditiontxt;
    }

    public void setConditiontxt(String conditiontxt) {
        this.conditiontxt = conditiontxt;
    }

    public String getConditiontxt2() {
        return conditiontxt2;
    }

    public void setConditiontxt2(String conditiontxt2) {
        this.conditiontxt2 = conditiontxt2;
    }

    public String getConditiontxt3() {
        return conditiontxt3;
    }

    public void setConditiontxt3(String conditiontxt3) {
        this.conditiontxt3 = conditiontxt3;
    }
}
