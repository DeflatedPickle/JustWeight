package com.deflatedpickle.justweight.api;

public interface ICarryWeight {
    void setMax(int value);
    int getMax();

    void incCurrent(int value);
    void decCurrent(int value);
    void setCurrent(int value);
    int getCurrent();
}
