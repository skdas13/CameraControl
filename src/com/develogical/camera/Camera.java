package com.develogical.camera;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;

    private volatile boolean isPowerOn = false;
    private volatile boolean isCopying = false;
    private volatile boolean isPoweringOff = false;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if (isPowerOn) {
            isCopying = true; // will be reset to false by memoryCard calling writeComplete()
            byte[] data = sensor.readData();
            memoryCard.write(data);
        }
    }

    public void powerOn() {
        sensor.powerUp();
        isPowerOn = true;
    }

    public void powerOff() {
        if (!isCopying) {
            sensor.powerDown();
            isPowerOn = false;
        } else {
            isPoweringOff = true;
        }
    }

    @Override
    public void writeComplete() {
        isCopying = false;

        if (isPoweringOff) {
            isPoweringOff = false;
            powerOff();
        }
    }
}
