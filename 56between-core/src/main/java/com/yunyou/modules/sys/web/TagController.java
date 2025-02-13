package com.yunyou.modules.sys.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 标签Controller
 *
 * @author yunyou
 * @version 2016-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {

    /**
     * 树结构选择标签（treeselect.tag）
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "treeSelect")
    public String treeSelect(HttpServletRequest request, Model model) {
        // 树结构数据URL
        model.addAttribute("url", request.getParameter("url"));
        // 排除的编号ID
        model.addAttribute("extId", request.getParameter("extId"));
        // 指定默认选中的ID
        model.addAttribute("selectIds", request.getParameter("selectIds"));
        // 是否可复选
        model.addAttribute("checked", request.getParameter("checked"));
        // 是否显示查找
        model.addAttribute("allowSearch", request.getParameter("allowSearch"));
        // 查找值
        model.addAttribute("text", request.getParameter("text"));
        // 是否读取全部数据，不进行权限过滤
        model.addAttribute("isAll", request.getParameter("isAll"));
        return "modules/common/tagTreeselect";
    }

    /**
     * 图标选择标签（iconselect.tag）
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "iconselect")
    public String iconSelect(HttpServletRequest request, Model model) {
        model.addAttribute("value", request.getParameter("value"));
        return "modules/common/tagIconselect";
    }

    /**
     * 列表选择标签（popSelect.tag）
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "popSelect")
    public String popSelect(String url, String queryParams, String queryParamValues, String fieldLabels, String fieldKeys, String searchLabels, String searchKeys, String inputSearchKey, String inputSearchValue, String isMultiSelected, Model model) {
        try {
            // url中带多个参数用“|”分隔，先对url解码，再替换成“&”
            url = URLDecoder.decode(url, "UTF-8");
            url = url.replace('|', '&');

            fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
            fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
            searchLabels = URLDecoder.decode(searchLabels, "UTF-8");
            searchKeys = URLDecoder.decode(searchKeys, "UTF-8");

            if (StringUtils.isNotEmpty(inputSearchKey)) {
                inputSearchKey = URLDecoder.decode(inputSearchKey, "UTF-8");
                inputSearchValue = URLDecoder.decode(inputSearchValue, "UTF-8");
            }
            if (StringUtils.isNotEmpty(queryParams)) {
                queryParams = URLDecoder.decode(queryParams, "UTF-8");
                queryParamValues = URLDecoder.decode(queryParamValues, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("popSelect插件解析参数编码格式错误", e);
        }
        model.addAttribute("isMultiSelected", isMultiSelected);
        model.addAttribute("fieldLabels", fieldLabels.split("\\|"));
        model.addAttribute("fieldKeys", fieldKeys.split("\\|"));
        model.addAttribute("url", url);
        model.addAttribute("searchLabels", searchLabels.split("\\|"));
        model.addAttribute("searchKeys", searchKeys.split("\\|"));
        if (StringUtils.isNotEmpty(queryParams)) {
            queryParams += StringUtils.isNotEmpty(inputSearchKey) ? "|" + inputSearchKey : "";
            model.addAttribute("queryParams", queryParams.split("\\|", -1));
        }
        if (StringUtils.isNotEmpty(queryParamValues)) {
            queryParamValues += StringUtils.isNotEmpty(inputSearchValue) ? "|" + inputSearchValue : "";
            model.addAttribute("queryParamValues", queryParamValues.split("\\|", -1));
        }
        return "modules/common/gridSelect";
    }

    /**
     * 列表选择标签（grid.tag）
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "grid")
    public String gridSelect(String url, String fieldLabels, String fieldKeys, String searchLabels, String searchKeys,
                             String queryParams, String queryParamValues, String isMultiSelected, Model model) {
        try {
            // url中带多个参数用“|”分隔，先对url解码，再替换成“&”
            url = URLDecoder.decode(url, "UTF-8");
            url = url.replace('|', '&');

            fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
            fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
            searchLabels = URLDecoder.decode(searchLabels, "UTF-8");
            searchKeys = URLDecoder.decode(searchKeys, "UTF-8");

            if (StringUtils.isNotEmpty(queryParams)) {
                queryParams = URLDecoder.decode(queryParams, "UTF-8");
                queryParamValues = URLDecoder.decode(queryParamValues, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("grid插件解析参数编码格式错误", e);
        }
        model.addAttribute("isMultiSelected", isMultiSelected);
        model.addAttribute("fieldLabels", fieldLabels.split("\\|"));
        model.addAttribute("fieldKeys", fieldKeys.split("\\|"));
        model.addAttribute("url", url);
        model.addAttribute("searchLabels", searchLabels.split("\\|"));
        model.addAttribute("searchKeys", searchKeys.split("\\|"));
        if (StringUtils.isNotEmpty(queryParams)) {
            model.addAttribute("queryParams", queryParams.split("\\|", -1));
        }
        if (StringUtils.isNotEmpty(queryParamValues)) {
            model.addAttribute("queryParamValues", queryParamValues.split("\\|", -1));
        }
        return "modules/common/gridSelect";
    }

}
