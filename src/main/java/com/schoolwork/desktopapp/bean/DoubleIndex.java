package com.schoolwork.desktopapp.bean;

public class DoubleIndex {
    private String leftIndex;    //左表的的位置
    private String rightIndex;   //右表的位置

    public String getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(String leftIndex) {
        this.leftIndex = leftIndex;
    }

    public String getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(String rightIndex) {
        this.rightIndex = rightIndex;
    }

    public DoubleIndex() {
    }

    @Override
    public String toString() {
        return "DoubleIndex{" +
                "leftIndex=" + leftIndex +
                ", rightIndex=" + rightIndex +
                '}';
    }
}
