package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(group = "Linear OpMode")
public class _Exercise8 extends LinearOpMode {
    _ProgrammingBoard8 board = new _ProgrammingBoard8();

    @Override
    public void runOpMode() {
        board.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
//            (8.4.1)
//            if (gamepad1.a) {
//                board.setServoPosition(0.5);
//            } else if (gamepad1.b) {
//                board.setServoPosition(0.0);
//            } else {
//                board.setServoPosition(0.25);
//            }

            double triggerSpeed = gamepad1.left_trigger;

            board.setServoPosition(triggerSpeed);
        }
    }
}
