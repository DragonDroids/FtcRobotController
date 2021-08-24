package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class _ProgrammingBoard7 {
    private DigitalChannel touchSensor;
    private DcMotor motor;
    private double ticksPerRotation;

    public void init(HardwareMap hwMap) {
        touchSensor = hwMap.get(DigitalChannel.class, "touch_sensor");
        touchSensor.setMode(DigitalChannel.Mode.INPUT);
        motor = hwMap.get(DcMotor.class, "motor");
        ticksPerRotation = motor.getMotorType().getTicksPerRev();
    }

    public boolean isTouchSensorPressed() {
        return !touchSensor.getState();
    }

    public void setMotorSpeed(double speed) {
        motor.setPower(speed);
    }

    public double getMotorRotations() {
        return motor.getCurrentPosition() / ticksPerRotation;
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        motor.setZeroPowerBehavior(behavior);
    }
}
