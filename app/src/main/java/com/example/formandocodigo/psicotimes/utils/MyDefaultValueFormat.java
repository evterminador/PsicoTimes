package com.example.formandocodigo.psicotimes.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by FormandoCodigo on 03/01/2018.
 */

public class MyDefaultValueFormat implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyDefaultValueFormat() {
        mFormat = new DecimalFormat("###,###,##0");
    }


    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + "";
    }
}
