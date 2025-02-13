package com.yunyou.modules.sys.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.Encodes;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Annex;
import com.yunyou.modules.sys.service.AnnexService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("${adminPath}/sys/annex")
public class AnnexController extends BaseController {
    @Autowired
    private AnnexService annexService;

    @ResponseBody
    @RequestMapping("data")
    public Map<String, Object> data(Annex annex, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(annexService.findPage(new Page<>(request, response), annex));
    }

    @ResponseBody
    @RequestMapping("upload")
    public AjaxJson upload(Integer type, String pkId, MultipartFile[] files) {
        AjaxJson j = new AjaxJson();
        try {
            for (MultipartFile file : files) {
                annexService.upload(type, pkId, file);
            }
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("附件上传异常，" + e.getMessage());
            if (logger.isErrorEnabled()) {
                logger.error("附件上传异常", e);
            }
        }
        return j;
    }

    @ResponseBody
    @RequestMapping("download")
    public AjaxJson download(String id, HttpServletResponse response) throws IOException {
        AjaxJson j = new AjaxJson();

        Annex annex = annexService.get(id);
        if (annex == null) {
            j.setSuccess(false);
            j.setMsg("附件不存在");
            return j;
        }

        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(annex.getFileName()));

        try (FileInputStream inputStream = new FileInputStream(annex.getPath() + File.separator + id + annex.getFileName())) {
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (FileNotFoundException e) {
            j.setSuccess(false);
            j.setMsg("附件不存在");
        }
        return j;
    }

    @ResponseBody
    @RequestMapping("downloadAll")
    public AjaxJson downloadAll(String ids, HttpServletResponse response) throws IOException {
        AjaxJson j = new AjaxJson();

        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode("附件.zip"));
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());

        String[] idArr = ids.split(",");
        for (String id : idArr) {
            Annex annex = annexService.get(id);
            if (annex == null) {
                continue;
            }
            try (FileInputStream inputStream = new FileInputStream(annex.getPath() + File.separator + id + annex.getFileName())) {
                ZipEntry entry = new ZipEntry(annex.getFileName());
                out.putNextEntry(entry);
                IOUtils.copy(inputStream, out);
            } catch (FileNotFoundException ignored) {
            }
        }
        out.close();
        return j;
    }

    @ResponseBody
    @RequestMapping("delete")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            try {
                annexService.delete(idArr[i]);
            } catch (Exception e) {
                errMsg.append("第").append(i).append("行失败，").append(e.getMessage()).append("<br>");
                if (logger.isErrorEnabled()) {
                    logger.error("删除附件记录失败", e);
                }
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg.toString());
        }
        return j;
    }
}
