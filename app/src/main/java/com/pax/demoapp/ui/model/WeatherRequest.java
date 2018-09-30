package com.pax.demoapp.ui.model;

/**
 * @author ligq
 * @date 2018/9/30
 */

public class WeatherRequest {
    private String cityname;
    private String dtype;
    private String format;

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
