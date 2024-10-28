package com.allancleitonppma.gmail.WareMap.core;

public class Separations<T, U, V>{
	private final T forklift;
    private final U floor;
    private final V cold;
    
    public Separations(T forklift, U floor, V cold) {
        this.forklift = forklift;
        this.floor = floor;
        this.cold = cold;
    }
    
    public T getForklift() { return forklift; }
    public U getFloor() { return floor; }
    public V getCold() { return cold; }
}
