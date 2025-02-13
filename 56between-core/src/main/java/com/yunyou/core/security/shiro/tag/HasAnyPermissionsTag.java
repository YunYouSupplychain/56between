package com.yunyou.core.security.shiro.tag;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

/**
 * Shiro HasAnyPermissions Tag
 *
 * @author yunyou
 * @since 2023/3/11 0:51
 */
public class HasAnyPermissionsTag extends PermissionTag {

    private static final long serialVersionUID = 1L;
    private static final String PERMISSION_NAMES_DELIMETER = ",";

    @Override
    protected boolean showTagBody(String permissionNames) {
        boolean hasAnyPermission = false;
        Subject subject = getSubject();
        if (subject != null) {
            // Iterate through permissions and check to see if the user has one of the permissions
            for (String permission : permissionNames.split(PERMISSION_NAMES_DELIMETER)) {
                if (subject.isPermitted(permission.trim())) {
                    hasAnyPermission = true;
                    break;
                }
            }
        }
        return hasAnyPermission;
    }

}
