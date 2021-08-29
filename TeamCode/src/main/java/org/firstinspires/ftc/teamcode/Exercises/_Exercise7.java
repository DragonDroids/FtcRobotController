package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise7 extends LinearOpMode {
    _ProgrammingBoard7 board = new _ProgrammingBoard7();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double motorSpeed = squareInputWithSign(gamepad1.left_stick_y);

            board.setMotorSpeed(motorSpeed);

//            (7.7.1)
//            if (gamepad1.a) {
//                board.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            } else if (gamepad1.b) {
//                board.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//            }

            telemetry.addData("Motor Speed", motorSpeed);
            telemetry.addData("Motor Rotations", board.getMotorRotations());
        }
    }

    public double squareInputWithSign(double input) {
        double output = input * input;
        if (input < 0) {
            output *= -1;
        }
        return output;
    }
}
