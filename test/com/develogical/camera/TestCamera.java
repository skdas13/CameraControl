package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(value = JMock.class)
public class TestCamera {

    private Mockery context = new Mockery();

    private Sensor sensor = context.mock(Sensor.class);
    private MemoryCard memoryCard = context.mock(MemoryCard.class);

    private Camera camera = new Camera(sensor, memoryCard);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        powerOnCamera();
    }

    private void powerOnCamera() {
        context.checking(new Expectations() {{
            oneOf(sensor).powerUp();
        }});

        camera.powerOn();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        powerOffCamera();
    }

    private void powerOffCamera() {
        context.checking(new Expectations() {{
            oneOf(sensor).powerDown();
        }});

        camera.powerOff();
    }

    @Test
    public void pressingTheShutterWhenPowerIsOffDoesNothing() {
        context.checking(new Expectations() {{
        }});

        camera.pressShutter();
    }

    @Test
    public void pressingTheShutterWhenPowerIsOnCopiesDataFromSensorToMemoryCard() {
        final byte[] data = new byte[]{};

        powerOnCamera();

        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(data));
            oneOf(memoryCard).write(with(same(data)));
        }});

        camera.pressShutter();
    }

    @Test
    public void switchingCameraOffDoesNotPowerDownSensorIfCopyingToMemoryCard() {
        final byte[] data = new byte[]{};

        powerOnCamera();

        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(data));
            oneOf(memoryCard).write(with(same(data)));
        }});

        camera.pressShutter();

        context.checking(new Expectations() {{
            never(sensor).powerDown();
        }});

        camera.powerOff();
    }

    @Test
    public void switchingCameraOffPowersDownSensorIfCopyingToMemoryCardCompleted() {
        final byte[] data = new byte[]{};

        powerOnCamera();

        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(data));
            oneOf(memoryCard).write(with(same(data)));
        }});

        camera.pressShutter();
        camera.writeComplete();

        context.checking(new Expectations() {{
            oneOf(sensor).powerDown();
        }});

        camera.powerOff();
    }
}
