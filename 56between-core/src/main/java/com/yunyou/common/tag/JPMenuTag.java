package com.yunyou.common.tag;

import com.yunyou.common.config.Global;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
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
public class JPMenuTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    /**
     * 菜单Map
     */
    protected Menu menu;
    /**
     * 左侧一级菜单图标为空时，默认显示的图标，不显示默认图标时使用空字符即可。
     */
    protected static final String DEFAULT_ICON = "fa-th-list";
    /**
     * 菜单输出的位置
     */
    protected String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

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
        if ("top".equals(position)) {
            sb.append(getTopMenu(menu, 0, UserUtils.getMenuList(), -1));
        } else {
            sb.append(getLeftMenu(menu, 0, UserUtils.getMenuList(), -1));
        }
        return sb;
    }

    private static String getTopMenu(Menu menuItem, int level, List<Menu> menuList, int first) {
        if (!menuItem.hasPermisson()) {
            return "";
        }
        StringBuilder menuString = new StringBuilder();
        // 如果是父节点且显示
        if ((menuItem.getHref() == null || "".equals(menuItem.getHref().trim())) && "1".equals(menuItem.getIsShow())) {
            // 如果是功能菜单
            if (level == 0) {
                int flag = 0;
                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getTopMenu(child, level + 1, menuList, ++flag));
                    }
                }
            }
            // 不是功能菜单
            if (level == 1) {
                if (first == 1) {
                    menuString.append("<li role=\"presentation\" class='active'>\n");
                } else {
                    menuString.append("<li>\n");
                }
                menuString.append("<a href=\"#").append(menuItem.getId()).append("\" role=\"tab\" data-toggle=\"tab\">");
                menuString.append("<i class=\"fa ").append(menuItem.getIcon()).append("\"></i><span style=\"padding-left:3px;\">").append(menuItem.getName()).append("</span>\n");
                menuString.append("</a>");
                menuString.append("</li>");
            }
        }
        return menuString.toString();
    }

    private static String getLeftMenu(Menu menuItem, int level, List<Menu> menuList, int first) {
        if (!menuItem.hasPermisson()) {
            return "";
        }
        StringBuilder menuString = new StringBuilder();
        String href;
        // level 为1是top菜单
        if (level > 1) {
            ServletContext context = SpringContextHolder.getBean(ServletContext.class);
            // 如果是子节点
            if (menuItem.getHref() != null && menuItem.getHref().length() > 0) {
                if (menuItem.getHref().startsWith("http://") || menuItem.getHref().startsWith("https://")) {
                    // 如果是互联网资源
                    href = menuItem.getHref();
                } else if (menuItem.getHref().endsWith(".html")) {
                    // 如果是静态资源并且不是ckfinder.html，直接访问不加adminPath
                    href = context.getContextPath() + menuItem.getHref();
                } else {
                    href = context.getContextPath() + Global.getAdminPath() + menuItem.getHref();
                }
                String icon = menuItem.getIcon();
                if (level == 2) {
                    if (StringUtils.isBlank(icon)) {
                        icon = DEFAULT_ICON;
                    }
                }
                if (menuItem.getTarget() != null && !"".equals(menuItem.getTarget())) {
                    menuString.append("<li><a class=\"J_menuItem\"  href=\"").append(href).append("\" target=\"").append(menuItem.getTarget()).append("\" ").append("><i class=\"fa ").append(icon).append("\"></i><span>").append(menuItem.getName()).append("</span></a></li>\n");
                } else {
                    menuString.append("<li><a class=\"J_menuItem\" href=\"").append(href).append("\"><i class=\"fa ").append(icon).append("\"></i><span>").append(menuItem.getName()).append("</span></a></li>\n");
                }
            }
        }
        if ((menuItem.getHref() == null || "".equals(menuItem.getHref().trim())) && "1".equals(menuItem.getIsShow())) {// 如果是父节点且显示
            // 如果是功能菜单
            if (level == 0) {
                int flag = 0;
                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getLeftMenu(child, level + 1, menuList, ++flag));
                    }
                }
            }
            if (level == 1) {
                if (first == 1) {
                    menuString.append("<div class=\"tab-pane fade active in\" id=\"").append(menuItem.getId()).append("\">");
                } else {
                    menuString.append("<div class=\"tab-pane fade\" id=\"").append(menuItem.getId()).append("\">");
                }

                menuString.append("<ul class=\"nav nav-pills nav-stacked\">");
                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getLeftMenu(child, level + 1, menuList, -1));
                    }
                }
                menuString.append("</ul>");
                menuString.append("</div>");
            }
            // 不是功能菜单
            if (level > 1) {
                String icon = menuItem.getIcon();
                if (level == 2) {
                    if (StringUtils.isBlank(icon)) {
                        icon = DEFAULT_ICON;
                    }
                }
                menuString.append("<li>\n");
                menuString.append("<a href=\"#\" class=\"dropdown-toggle\"><i class=\"fa ").append(icon).append("\"></i><span>").append(menuItem.getName()).append("</span><i class=\"fa fa-chevron-circle-right drop-icon\"></i></a>\n");
                menuString.append("<ul class=\"submenu\" style=\"display: none;\">\n");
                for (Menu child : menuList) {
                    if (child.getParentId().equals(menuItem.getId()) && "1".equals(child.getIsShow())) {
                        menuString.append(getLeftMenu(child, level + 1, menuList, -1));
                    }
                }
                menuString.append("</ul>\n");
                menuString.append("</li>\n");
            }
        }
        return menuString.toString();
    }

}
