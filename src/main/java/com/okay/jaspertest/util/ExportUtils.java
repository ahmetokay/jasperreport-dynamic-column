package com.okay.jaspertest.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.components.ComponentsExtensionsRegistryFactory;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportUtils {

    private static JasperDesign jasperDesign;

    private static final String FIELD_PREFIX = "field";
    private static final String PARAMETER_DATA = "tableData";

    private static List<Map<String, Object>> createTableDataFromList(List<List<Object>> dataList) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            HashMap<String, Object> rowMap = new HashMap<>();
            List<Object> columnList = dataList.get(i);
            for (int j = 0; j < columnList.size(); j++) {
                rowMap.put(FIELD_PREFIX + j, columnList.get(j));
            }
            data.add(rowMap);
        }

        return data;
    }

    public static void exportYeni(List<String> headerList, List<List<Object>> dataList, HttpServletResponse response) throws JRException {
        try {
            int columnCount = headerList.size();

            response.setContentType("application/x-download");

            ServletOutputStream outputStream = response.getOutputStream();

            jasperDesign = new JasperDesign();
            jasperDesign.setName("Rapor");
            jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            JRDesignParameter parameter = new JRDesignParameter();
            parameter.setValueClass(List.class);
            parameter.setName(PARAMETER_DATA);
            jasperDesign.addParameter(parameter);

            //the subdataset
            String datasetName = "tableDataset";
            JRDesignDataset subdataset = new JRDesignDataset(false);
            subdataset.setName(datasetName);

            // kolon sayısı kadar field ekleniyor
            for (int i = 0; i < columnCount; i++) {
                JRDesignField field = new JRDesignField();
                field.setValueClass(String.class);
                field.setName(FIELD_PREFIX + i);
                subdataset.addField(field);
            }
            jasperDesign.addDataset(subdataset);

            //the table element
            JRDesignComponentElement tableElement = new JRDesignComponentElement(jasperDesign);
            tableElement.setX(0);
            tableElement.setY(0);
            tableElement.setWidth(200);
            tableElement.setHeight(50);

            ComponentKey componentKey = new ComponentKey(ComponentsExtensionsRegistryFactory.NAMESPACE, "c", ComponentsExtensionsRegistryFactory.TABLE_COMPONENT_NAME);
            tableElement.setComponentKey(componentKey);

            StandardTable table = new StandardTable();

            //the table data source
            JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
            datasetRun.setDatasetName(datasetName);
            datasetRun.setDataSourceExpression(new JRDesignExpression("new net.sf.jasperreports.engine.data.JRMapCollectionDataSource($P{" + PARAMETER_DATA + "})"));
            table.setDatasetRun(datasetRun);

            // kolon ekleniyor
            for (int i = 0; i < columnCount; i++) {
                StandardColumn column = createColumn(100, 20, headerList.get(i), "$F{" + FIELD_PREFIX + i + "}");
                table.addColumn(column);
            }

            tableElement.setComponent(table);

            JRDesignBand title = new JRDesignBand();
            title.setHeight(50);
            title.addElement(tableElement);
            jasperDesign.setTitle(title);

            JasperReport report = JasperCompileManager.compileReport(jasperDesign);
            Map<String, Object> params = new HashMap<>();
            params.put(PARAMETER_DATA, createTableDataFromList(dataList));

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params);

            Exporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + "document.xlsx" + "\"");
            exporter.exportReport();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    private static StandardColumn createColumn(int width, int height, String headerText, String detailExpression) {
        StandardColumn column = new StandardColumn();
        column.setWidth(width);

        //column header
        DesignCell header = new DesignCell();
        header.setDefaultStyleProvider(jasperDesign);
        header.getLineBox().getPen().setLineWidth(1f);
        header.setHeight(height);

        JRDesignStaticText headerElement = new JRDesignStaticText(jasperDesign);
        headerElement.setX(0);
        headerElement.setY(0);
        headerElement.setBold(true);
        headerElement.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        headerElement.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        headerElement.setWidth(width);
        headerElement.setHeight(height);
        headerElement.setText(headerText);

        header.addElement(headerElement);
        column.setColumnHeader(header);

        //column detail
        DesignCell detail = new DesignCell();
        detail.setDefaultStyleProvider(jasperDesign);
        detail.getLineBox().getPen().setLineWidth(1f);
        detail.setHeight(height);

        JRDesignTextField detailElement = new JRDesignTextField(jasperDesign);
        detailElement.setX(0);
        detailElement.setY(0);
        detailElement.setWidth(width);
        detailElement.setHeight(height);
        detailElement.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        detailElement.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        detailElement.setExpression(new JRDesignExpression(detailExpression));

        detail.addElement(detailElement);
        column.setDetailCell(detail);

        return column;
    }
}