package com.yunyou.plugins.jasperReport;

import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

/**
 * SpringMVC + IReport整合
 */
public class ApplicationIReportView extends JasperReportsMultiFormatView {
    private JasperReport jasperReport;

    public ApplicationIReportView() {
        super();
    }

    @Override
    protected JasperPrint fillReport(Map<String, Object> model) throws Exception {
        if (model.containsKey("url")) {
            setUrl(String.valueOf(model.get("url")));
            this.jasperReport = loadReport();
        }

        return super.fillReport(model);
    }

    @Override
    protected JasperReport getReport() {
        return this.jasperReport;
    }
}