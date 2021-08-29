package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise4 extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            double forwardSpeed = gamepad1.left_stick_y;
            double sideSpeed = gamepad1.left_stick_x;
            if (gamepad1.a) {
                telemetry.addData("Forward Speed", sideSpeed);
                telemetry.addData("Side Speed", forwardSpeed);
            } else {
                forwardSpeed *= 0.5;
                telemetry.addData("Forward Speed", forwardSpeed);
                telemetry.addData("Side Speed", sideSpeed);
            }
        }
    }
}
