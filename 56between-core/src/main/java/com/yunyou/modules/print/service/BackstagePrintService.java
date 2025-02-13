package com.yunyou.modules.print.service;

import com.yunyou.common.utils.PDFUtils;
import com.yunyou.core.service.BaseService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 后台打印接口service
 *
 * @author zyf
 * @version 2019-10-09
 */
@Service
@Transactional(readOnly = true)
public class BackstagePrintService extends BaseService {

    public List<String> getImageList(String jasperName, JRDataSource jrDataSource) {
        URL url = getClass().getClassLoader().getResource("/jasper/" + jasperName + ".jasper");
        if (url == null) {
            logger.error(jasperName + ".jasper不存在");
            return Lists.newArrayList();
        }

        File reportFile = new File(url.getFile());
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), Maps.newHashMap(), jrDataSource);
            return PDFUtils.icePdfToImage(JasperExportManager.exportReportToPdf(jasperPrint));
        } catch (JRException e) {
            logger.error("打印面单异常", e);
            return Lists.newArrayList();
        }
    }
}