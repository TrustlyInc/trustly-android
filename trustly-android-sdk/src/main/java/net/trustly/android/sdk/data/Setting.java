package net.trustly.android.sdk.data;

import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("settings")
    public LightBoxSetting setting;

    public LightBoxSetting getSetting() {
        return setting;
    }

    public void setSetting(LightBoxSetting setting) {
        this.setting = setting;
    }

    public class LightBoxSetting {

        @SerializedName("lightbox")
        public LightBoxContext lightbox;

        public LightBoxContext getLightbox() {
            return lightbox;
        }

        public void setLightbox(LightBoxContext lightbox) {
            this.lightbox = lightbox;
        }

        public class LightBoxContext {

            @SerializedName("context")
            public String context;

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

        }

    }

}