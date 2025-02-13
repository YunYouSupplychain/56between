package com.yunyou.common.tag;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.modules.sys.entity.Menu;
import com.yunyou.modules.sys.utils.UserUtils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：菜单标签
 * <p>
 *
 * @version 1.0
 */
public class AniMenuTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    protected Menu menu;// 菜单Map

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int doStartTag() throws JspTagException {
        return EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            JspWriter out = this.pageContext.getOut();
            String menu = (String) this.pageContext.getSession().getAttribute("menu");
            if (menu != null) {
                out.print(menu);
            } else {
                menu = end().toString();
                out.print(menu);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    public StringBuffer end() {
        StringBuffer sb = new StringBuffer();
        sb.append(getChildOfTree(menu, 0, UserUtils.getMenuList()));
        return sb;

    }

    private static String getChildOfTree(Menu menuItem, int level, List<Menu> menuList) {
        if (!menuItem.hasPermisson()) {
            return "";
        }
        StringBuilder menuString = new StringBuilder();
        String href;
        // level 为0是功能菜单
        if (level > 0) {
            ServletContext context = SpringContextHolder.getBean(ServletContext.class);
            // 如果是子节点
            if (menuItem.getHref() != null && menuItem.getHref().length() > 0) {
                if (menuItem.getHref().startsWith("http://") || menuItem.getHref().startsWith("https://")) {
                    // 如果是互联网资源
                    href = menuItem.getHref();
                } else if (menuItem.getHref().endsWith(".html") && !menuItem.getHref().endsWith("ckfinder.html")) {
                    // 如果是静态资源并且不是ckfinder.html，直接访问不加adminPath
                    href = context.getContextPath() + menuItem.getHref();
                } else {
                    href = context.getContextPath() + Global.getAdminPath() + menuItem.getHref();
                }
                if (menuItem.getTarget() != null && !"".equals(menuItem.getTarget())) {
                    menuString.append("<li><a class=\"J_menuItem\"  href=\"").append(href).append("\" target=\"").append(menuItem.getTarget()).append("\" ").append("><i class=\"fa ").append(menuItem.getIcon()).append("\"></i>&nbsp;&nbsp;").append(menuItem.getName()).append("</a></li>\n");
                } else {
                    menuString.append("<li><a class=\"J_menuItem\" href=\"").append(href).append("\"><i class=\"fa ").append(menuItem.getIcon()).append("\"></i>&nbsp;&nbsp;").append(menuItem.getName()).append("</a></li>\n");
                }
            }
        }
        // 如果是父节点且显示
        if ((menuItem.getHref() == null || "".equals(menuItem.getHref().trim())) && "1".equals(menuItem.getIsShow())) {
            // 如果是功能菜单
            if (level == 0) {
                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getChildOfTree(child, level + 1, menuList));
                    }
                }
            }
            // 不是功能菜单
            if (level > 0) {
                menuString.append("<li class=\"panel\">\n");
                menuString.append("<a  data-toggle=\"collapse\" data-parent=\"#").append(menuItem.getParentId()).append("\" class=\"collapsed\" href=\"#").append(menuItem.getId()).append("\"><i class=\"fa ").append(menuItem.getIcon()).append("\"></i>&nbsp;&nbsp;").append(menuItem.getName()).append("<span class=\"pull-right fa fa-angle-toggle\"></span></a>\n");
                menuString.append("<ul id=\"").append(menuItem.getId()).append("\"").append(" class=\"nav collapse\">\n");

                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getChildOfTree(child, level + 1, menuList));
                    }
                }
                menuString.append("</ul>\n");
                menuString.append("</li>\n");
            }
        }
        return menuString.toString();
    }

}
