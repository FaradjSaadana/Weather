
package fr.faradj.weatherapi.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentObservation {

    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("atmosphere")
    @Expose
    private Atmosphere atmosphere;
    @SerializedName("astronomy")
    @Expose
    private Astronomy astronomy;
    @SerializedName("condition")
    @Expose
    private Condition condition;
    @SerializedName("pubDate")
    @Expose
    private Integer pubDate;

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Integer getPubDate() {
        return pubDate;
    }

    public void setPubDate(Integer pubDate) {
        this.pubDate = pubDate;
    }

}
