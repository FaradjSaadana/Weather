
package fr.faradj.weatherapi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Atmosphere {

    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("pressure")
    @Expose
    private Double pressure;

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

}
