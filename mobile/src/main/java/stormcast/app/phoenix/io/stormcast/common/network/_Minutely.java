package stormcast.app.phoenix.io.stormcast.common.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class _Minutely {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("data")
    @Expose
    private List<_MinutelyData> data = new ArrayList<_MinutelyData>();

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

    public List<_MinutelyData> getData() {
        return data;
    }

    public void setData(List<_MinutelyData> data) {
        this.data = data;
    }
}
