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
        List<List<Object>> dummyList = dummyService.createDummyList(20);
        ExportUtils.exportYeni(Arrays.asList("name", "description"), dummyList, response);
    }
}