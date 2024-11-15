package com.allancleiton.waremap.entities.enums;

import com.allancleiton.waremap.entities.Separation;

public class SeparationSet<T extends Separation, U extends Separation, V extends Separation>{
	private final T forklift;
    private final U floor;
    private final V cold;
    
    public SeparationSet(T forklift, U floor, V cold) {
        this.forklift = forklift;
        this.floor = floor;
        this.cold = cold;
    }
    
    public T getForklift() { return forklift; }
    public U getFloor() { return floor; }
    public V getCold() { return cold; }
}