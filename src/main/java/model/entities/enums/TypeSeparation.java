package model.entities.enums;

public enum TypeSeparation {
    FORKLIFT(0),
    COLD(1),
    FLOOR(2);

    private int value;

    private TypeSeparation(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getValue(int position) {

        return switch (position) {
            case 0 -> "FORKLIFT";
            case 1 -> "COLD";
            case 2 -> "FLOOR";
            default -> throw new IllegalArgumentException("Error. em TypeSeparation. value: " + position);
        };
    }


}