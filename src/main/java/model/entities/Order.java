package model.entities;

public class Order{
    public Integer note;
    public Integer packeges;
    public Integer qtdeBoxes;

    public Order(Integer note, Integer packages, Integer qtdeBoxes) {
        this.note = note;
        this.packeges = packages;
        this.qtdeBoxes = qtdeBoxes;
    }

    public Order(int note, int qtdeBoxes) {
        this.note = note;
        this.qtdeBoxes = qtdeBoxes;
    }

    public Order() {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("Produto: " + note + " Caixas: " + qtdeBoxes);

        return sb.toString();
    }

    public Integer note() {
        return this.note;
    }

    public Integer qtdeBoxes() {
        return this.qtdeBoxes;
    }

    public Integer packeges() {
        return this.packeges;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Integer getPackeges() {
        return packeges;
    }

    public void setPackeges(Integer packeges) {
        this.packeges = packeges;
    }

    public Integer getQtdeBoxes() {
        return qtdeBoxes;
    }

    public void setQtdeBoxes(Integer qtdeBoxes) {
        this.qtdeBoxes = qtdeBoxes;
    }


}