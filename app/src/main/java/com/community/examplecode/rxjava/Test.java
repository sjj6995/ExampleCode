package com.community.examplecode.rxjava;

public class Test {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColloge() {
        return colloge;
    }

    public void setColloge(String colloge) {
        this.colloge = colloge;
    }

    private String colloge;


    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", colloge='" + colloge + '\'' +
                '}';
    }
}
