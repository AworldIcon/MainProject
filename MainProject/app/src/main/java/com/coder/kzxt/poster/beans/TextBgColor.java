package com.coder.kzxt.poster.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class TextBgColor {

    private List<TextBgColor.ColorBean> posterBgColor;

    public List<ColorBean> getPosterBgColor() {
        return posterBgColor;
    }

    public void setPosterBgColor(List<ColorBean> posterBgColor) {
        this.posterBgColor = posterBgColor;
    }

    public static class ColorBean {
        private int id;
        private String color;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

}
