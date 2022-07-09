package com.okay.jaspertest.controller;

import com.okay.jaspertest.service.DummyService;
import com.okay.jaspertest.util.ExportUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReportController {

    private final DummyService dummyService;

    @GetMapping(value = "/export")
    public void export(HttpServletResponse response) throws JRException {
        int headerCount = 5, rowCount = 30;
        List<String> dummyHeaderList = dummyService.createDummyHeaderList(headerCount);
        List<List<Object>> dummyDataList = dummyService.createDummyList(headerCount, rowCount);
        ExportUtils.exportYeni(dummyHeaderList, dummyDataList, response);
    }
}