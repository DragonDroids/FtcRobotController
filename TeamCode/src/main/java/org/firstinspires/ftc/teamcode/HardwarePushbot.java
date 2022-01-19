/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class HardwarePushbot
{
    /* Public OpMode members. */
    public DcMotor  frontLeftDrive   = null;
    public DcMotor  frontRightDrive  = null;
    public DcMotor  backLeftDrive   = null;
    public DcMotor  backRightDrive  = null;
    public DcMotor  carousel = null;
    public DcMotor  armLift = null;
    public DigitalChannel carSw = null;
    public BNO055IMU imu = null;
    public Servo armGripper = null;
    public DcMotor  motorTest = null;

    public String frontLeftName = "frontLeft";
    public String frontRightName = "frontRight";
    public String backLeftName = "backLeft";
    public String backRightName = "backRight";
    public String carouselName = "carousel";
    public String armLiftName = "armLift";
    public String carSwName = "carSw";
    public String armGripperName = "armClamp";
    public String motorTestName = "motorTest";

    public double speed;
    public double leftPower;
    public double rightPower;
    public boolean move = false;

    private final double maxSpeed = 1;
    private final double minSpeed = 0.5;
    private final boolean variableSpeed = true;

    public double tickPerRev = 537.7;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;

    /* Constructor */
    public HardwarePushbot(){}

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // IMU Params
        BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();

        imuParameters.mode                = BNO055IMU.SensorMode.IMU;
        imuParameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuParameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuParameters.loggingEnabled      = false;

        // Define and Initialize Motors
        frontLeftDrive  = hwMap.get(DcMotor.class, frontLeftName);
        frontRightDrive = hwMap.get(DcMotor.class, frontRightName);
        backLeftDrive  = hwMap.get(DcMotor.class, backLeftName);
        backRightDrive = hwMap.get(DcMotor.class, backRightName);
        carousel = hwMap.get(DcMotor.class, carouselName);
        armLift = hwMap.get(DcMotor.class, armLiftName);
        carSw = hwMap.get(DigitalChannel.class, carSwName);
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(imuParameters);
        armGripper = hwMap.get(Servo.class, armGripperName);
        motorTest = hwMap.get(DcMotor.class, motorTestName);

        // Set all motors to zero power
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        // Set all motors to run using encoders.
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLift.setTargetPosition(0);
        armLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        carSw.setMode(DigitalChannel.Mode.INPUT);

        // Set reverse for NeverRest Motors
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set's the drivetrain's motors to float when the power is set to 0
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Set's the motor to brake when the motor's power is set to 0
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        frontLeftDrive.setZeroPowerBehavior(behavior);
        frontRightDrive.setZeroPowerBehavior(behavior);
        backLeftDrive.setZeroPowerBehavior(behavior);
        backRightDrive.setZeroPowerBehavior(behavior);
    }

    public void setMoveMotors() {
        frontRightDrive.setPower(rightPower);
        frontLeftDrive.setPower(leftPower);
        backRightDrive.setPower(rightPower);
        backLeftDrive.setPower(leftPower);
    }

    public void debug(boolean deb, Telemetry tel, Gamepad gamepad1, Gamepad gamepad2) {
        if (deb) {
            tel.addData("Front Left", frontLeftDrive.getCurrentPosition() / tickPerRev);
            tel.addData("Front Right", frontRightDrive.getCurrentPosition() / tickPerRev);
            tel.addData("Back Left", backLeftDrive.getCurrentPosition() / tickPerRev);
            tel.addData("Back Right", backRightDrive.getCurrentPosition() / tickPerRev);
            tel.addData("Move Speed", speed);
            tel.addData("Viper Slide Position: ", armLift.getCurrentPosition());
            tel.addData("Current Robot Angle: ", getAngle());
            tel.update();
        }
    }

    public void getMovementSpeed(Gamepad gamepad) {
        if (variableSpeed) {
            speed = minSpeed + (gamepad.right_trigger * (maxSpeed - minSpeed));
        } else {
            speed = gamepad.right_bumper ? maxSpeed : minSpeed;
        }
    }

    public double deadzone(double input, double dead) {
        if (input < dead && input > -dead) {
            return 0.0;
        } else {
            return input;
        }
    }

    public void resetArm() {
        armLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armLift.setTargetPosition((int) Math.round((10 / 360) * 537.7));
        armLift.setPower(0.4);
    }

    public double getAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public boolean isMoving(double err) {
        return deadzone(frontLeftDrive.getTargetPosition() - frontLeftDrive.getCurrentPosition(), err) != 0 &&
                deadzone(frontRightDrive.getTargetPosition() - frontRightDrive.getCurrentPosition(), err) != 0 &&
                deadzone(backLeftDrive.getTargetPosition() - backLeftDrive.getCurrentPosition(), err) != 0 &&
                deadzone(backRightDrive.getTargetPosition() - backRightDrive.getCurrentPosition(), err) != 0;
    }
}