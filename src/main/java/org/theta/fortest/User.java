package org.theta.fortest;

import org.theta.joobase.annotations.JooCell;

@JooCell
public class User {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}