package model.config;

public enum TypeSeparation {

    IN_STATE("inState"),
    OUT_STATE("outState"),
    DEFAULT("default");

    private String type;

    private TypeSeparation(String value) {
        this.type = value;
    }

    public String getType() {
        return this.type;
    }

}