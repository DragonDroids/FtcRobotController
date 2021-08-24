package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise1 extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Hello", "Name");
        waitForStart();

        while (opModeIsActive()) {


        }
    }
}
