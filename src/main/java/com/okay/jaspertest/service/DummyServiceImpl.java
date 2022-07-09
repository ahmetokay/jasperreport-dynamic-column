package com.okay.jaspertest.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyServiceImpl implements DummyService {

    @Override
    public List<String> createDummyHeaderList(int size) {
        List<String> dataList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            dataList.add("header" + i);
        }

        return dataList;
    }

    @Override
    public List<List<Object>> createDummyList(int headerCount, int size) {
        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Object> rowList = new ArrayList<>();
            for (int j = 0; j < headerCount; j++) {
                rowList.add("column_" + j + "_" + i);
            }
            dataList.add(rowList);
        }

        return dataList;
    }
}