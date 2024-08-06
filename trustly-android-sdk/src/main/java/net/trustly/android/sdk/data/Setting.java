package net.trustly.android.sdk.data;

public class Setting {

    public LightBoxSetting lightBox;

    public LightBoxSetting getLightBox() {
        return lightBox;
    }

    public class LightBoxSetting {

        public String context;

        public String getContext() {
            return context;
        }
    }

}