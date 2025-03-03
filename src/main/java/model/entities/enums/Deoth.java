package model.entities.enums;

public enum Deoth {
    A(0),
    B(1),
    C(2);

    private int value;

    private Deoth(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public char getValue(int position) {

        switch (position) {
            case 0: {
                return 'A';
            }
            case 1: {
                return 'B';
            }
            case 2: {
                return 'C';
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + position);
        }
    }


}