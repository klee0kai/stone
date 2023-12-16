package com.github.klee0kai.wiki.provide.identifiers;

import java.util.Objects;

public class LoginId {

    public String accountName = "";

    public LoginId(String tag) {
        this.accountName = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginId screenId = (LoginId) o;
        return Objects.equals(accountName, screenId.accountName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName);
    }

}
