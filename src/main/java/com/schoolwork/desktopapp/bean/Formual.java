package com.schoolwork.desktopapp.bean;

import java.util.Arrays;
import java.util.List;

public class Formual {
    private List<OpValue> arrRes;
    private String[] pointStr;

    public List<OpValue> getArrRes() {
        return arrRes;
    }

    public void setArrRes(List<OpValue> arrRes) {
        this.arrRes = arrRes;
    }

    public String[] getPointStr() {
        return pointStr;
    }

    public void setPointStr(String[] pointStr) {
        this.pointStr = pointStr;
    }

    @Override
    public String toString() {
        return "Formual{" +
                "arrRes=" + arrRes +
                ", pointStr=" + Arrays.toString(pointStr) +
                '}';
    }
}
