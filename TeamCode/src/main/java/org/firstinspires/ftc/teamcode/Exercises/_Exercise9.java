package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise9 extends LinearOpMode {
    _ProgrammingBoard9 board = new _ProgrammingBoard9();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double potAngle = board.getPotAngle();
            telemetry.addData("Pot Angle", potAngle);
            board.setServoPosition(potAngle);
        }
    }
}