package com.schoolwork.desktopapp.bean;

import java.util.Arrays;
import java.util.List;

public class Formual {
    private List<OpValue> arrRes;     //保存除了and 和 or 其他的内容
    private String[] pointStr;       //保存逆波兰式

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
