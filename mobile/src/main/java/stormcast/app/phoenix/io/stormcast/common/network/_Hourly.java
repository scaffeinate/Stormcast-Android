package stormcast.app.phoenix.io.stormcast.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class _Hourly {
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("data")
    @Expose

    private List<_HourlyData> data = new ArrayList<_HourlyData>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<_HourlyData> getData() {
        return data;
    }

    public void setData(List<_HourlyData> data) {
        this.data = data;
    }

}
