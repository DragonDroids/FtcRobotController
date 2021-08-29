package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise6 extends LinearOpMode {
    _ProgrammingBoard5 board = new _ProgrammingBoard5();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Touch Pressed", board.isTouchSensorPressed() ? "Pressed" : "Not Pressed");
//            6.5.1
//            telemetry.addData("Touch Released", board.isTouchSensorReleased());
        }
    }
}
