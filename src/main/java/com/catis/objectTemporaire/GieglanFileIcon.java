package com.catis.objectTemporaire;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GieglanFileIcon that = (GieglanFileIcon) o;
        return Objects.equals(extension, that.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(extension);
    }
}
