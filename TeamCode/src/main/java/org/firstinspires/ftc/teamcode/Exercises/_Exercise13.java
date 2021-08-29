package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(group = "Linear OpMode")
public class _Exercise13 extends LinearOpMode {
    String[] words = {"Zeroth", "First", "Second", "Third", "Fourth", "Fifth", "Infinity"};
    int wordsIndex;
    double DELAY_SECS = 0.5;

    double nextTime;

    @Override
    public void runOpMode() {
        wordsIndex = 0;

        waitForStart();

        while (opModeIsActive()) {
            if (nextTime < getRuntime()) {
                
            }
        }
    }
}