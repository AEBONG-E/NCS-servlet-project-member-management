package com.member.servletprojectmembermanagement.dao;

import com.sun.xml.internal.bind.v2.TODO;

public class MemberDao {

    private static MemberDao instance;

    public MemberDao() {
    }

    public static MemberDao getInstance() {
        if (instance == null) {
            instance = new MemberDao();
        }
        return instance;
    }

    public boolean isExistId(String memberId) {
        return Boolean.getBoolean(memberId);
    }
}
