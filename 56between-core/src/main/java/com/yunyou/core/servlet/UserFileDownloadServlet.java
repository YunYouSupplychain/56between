package com.yunyou.core.servlet;

import com.yunyou.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 查看CK上传的图片
 *
 * @author yunyou
 * @version 2017-06-25
 */
public class UserFileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fileOutputStream(req, resp);
    }

    private void fileOutputStream(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filepath = req.getRequestURI();
        int index = filepath.indexOf(Global.USER_FILES_DIR_NAME);
        if (index >= 0) {
            filepath = filepath.substring(index + Global.USER_FILES_DIR_NAME.length() + 1);
        }
        try {
            filepath = UriUtils.decode(filepath, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(String.format("解释文件路径失败，URL地址为%s", filepath), e1);
        }
        File file = new File(Global.getUserFilesBaseDir() + filepath);
        try {
            FileCopyUtils.copy(new FileInputStream(file), resp.getOutputStream());
            resp.setHeader("Content-Type", "application/octet-stream");
        } catch (FileNotFoundException e) {
            req.setAttribute("exception", new FileNotFoundException("请求的文件不存在"));
            req.getRequestDispatcher("/webpage/error/404.jsp").forward(req, resp);
        }
    }
}
