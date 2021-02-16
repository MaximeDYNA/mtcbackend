package com.catis.objectTemporaire;

public class GieglanFileIcon {
    public String extension;
    public String icon;

    public GieglanFileIcon() {
    }

    public GieglanFileIcon(String extension, String icon) {
        this.extension = extension;
        this.icon = icon;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
