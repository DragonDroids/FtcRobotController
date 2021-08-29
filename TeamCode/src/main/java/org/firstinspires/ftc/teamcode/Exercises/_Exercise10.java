package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(group = "Linear OpMode")
public class _Exercise10 extends LinearOpMode {
    _ProgrammingBoard10 board = new _ProgrammingBoard10();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Amuont Red", board.getAmountRed());
            telemetry.addData("Amuont Blue", board.getAmountBlue());
            telemetry.addData("Distance (CM)", board.getDistance(DistanceUnit.CM));
            telemetry.addData("Distance (IN)", board.getDistance(DistanceUnit.INCH));

            if (board.getDistance(DistanceUnit.CM) < 10) {
                board.setMotorSpeed(0.0);
            } else {
                board.setMotorSpeed(0.5);
            }
        }
    }
}