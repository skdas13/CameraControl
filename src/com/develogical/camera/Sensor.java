package com.develogical.camera;

public interface Sensor {
    byte[] readData();
    void powerUp();
    void powerDown();
}
