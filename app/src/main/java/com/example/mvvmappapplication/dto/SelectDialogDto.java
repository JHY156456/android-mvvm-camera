package com.example.mvvmappapplication.dto;

/**
 * Some descriptions here
 *
 * @author kck
 * @version 1.0
 * @since 2016-12-13
 */
public class SelectDialogDto {
    String id;
    String value;
    boolean isSelected;
    boolean isEnabled;
    boolean visible;

    public SelectDialogDto() {
        this.visible = true;
    }

    public SelectDialogDto(String id, String value) {
        this.id = id;
        this.value = value;
        this.isEnabled = true;
        this.visible = true;
    }

    public SelectDialogDto(String id, String value, boolean isSelected) {
        this.id = id;
        this.value = value;
        this.isSelected = isSelected;
        this.isEnabled = true;
        this.visible = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public String toString() {
        return "SelectDialogDto{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", isSelected=" + isSelected +
                ", isEnabled=" + isEnabled +
                ", isVisible=" + visible +
                '}';
    }
}
