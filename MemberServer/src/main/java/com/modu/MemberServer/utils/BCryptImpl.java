package com.modu.MemberServer.utils;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptImpl implements EncryptHelper {

    public BCryptImpl() { }

    @Override
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
