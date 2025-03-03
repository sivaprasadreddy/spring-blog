package com.sivalabs.springblog.web.controllers;

import com.sivalabs.springblog.domain.models.SecurityUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

class UserContextUtils {
    public static Long getCurrentUserIdOrThrow() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser securityUser) {
            return securityUser.getId();
        }
        throw new AccessDeniedException("Access denied");
    }
}
