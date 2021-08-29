package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise3 extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();

        while (opModeIsActive()) {
            double speedForward = -gamepad1.left_stick_y / 2.0;
            telemetry.addData("Left stick y", gamepad1.left_stick_y);
            telemetry.addData("Speed Forward", speedForward);
            telemetry.addData("Right stick y", gamepad1.right_stick_y);
            telemetry.addData("B", gamepad1.b);
            telemetry.addData("Difference", gamepad1.left_stick_y - gamepad1.right_stick_y);
            telemetry.addData("Sum", gamepad1.left_trigger + gamepad1.right_trigger);
        }
    }
}
