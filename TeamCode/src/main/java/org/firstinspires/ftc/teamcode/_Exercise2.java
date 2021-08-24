package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise2 extends LinearOpMode {

    @Override
    public void runOpMode() {
        String myName = "Name";
        int grade = 100;

        telemetry.addData("Hello", grade);

        waitForStart();

        while (opModeIsActive()) {


        }
    }
}
