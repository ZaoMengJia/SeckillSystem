package com.zaomengjia.auth.constant;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 11:12
 */
public enum AuthorityGroup {
    USER("USER"),
    ADMIN("ADMIN")
    ;

    public final String raw;
    AuthorityGroup(String raw) {
        this.raw = raw;
    }
}
