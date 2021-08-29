package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(group = "Linear OpMode")
public class _Exercise11 extends LinearOpMode {
    _ProgrammingBoard11 board = new _ProgrammingBoard11();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Our Heading (DEGREES)", board.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Our Heading (RADIANS)", board.getHeading(AngleUnit.RADIANS));
            if (board.getHeading(AngleUnit.DEGREES) == 0) {
                board.setMotorSpeed(0.0);
            } else if (board.getHeading(AngleUnit.DEGREES) > 0) {
                board.setMotorSpeed(1.0);
            } else {
                board.setMotorSpeed(-1.0);
            }
        }
    }
}