package com.github.klee0kai.wiki.provide.identifiers;

import java.util.Objects;

public class ScreenId {

    public String tag = "";

    public ScreenId(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenId screenId = (ScreenId) o;
        return Objects.equals(tag, screenId.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }



}
