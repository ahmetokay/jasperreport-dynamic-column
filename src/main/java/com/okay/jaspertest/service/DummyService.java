package com.okay.jaspertest.service;

import java.util.List;

public interface DummyService {

    List<String> createDummyHeaderList(int size);

    List<List<Object>> createDummyList(int headerCount, int size);

}