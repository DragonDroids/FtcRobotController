package org.firstinspires.ftc.teamcode.Exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Linear OpMode")
public class _Exercise5 extends LinearOpMode {
    _RobotLocation5 robotLocation = new _RobotLocation5(0, 0, 0);

    @Override
    public void runOpMode() {
        robotLocation.setAngle(0);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                robotLocation.turn(0.1);
            } else if (gamepad1.b) {
                robotLocation.turn(-0.1);
            }

            if (gamepad1.dpad_left) {
                robotLocation.changeX(0.1);
            } else if (gamepad1.dpad_right) {
                robotLocation.changeX(-0.1);
            }

            if (gamepad1.dpad_up) {
                robotLocation.changeY(0.1);
            } else if (gamepad1.dpad_down) {
                robotLocation.changeY(-0.1);
            }
            telemetry.addData("Location", robotLocation);
            telemetry.addData("Heading", robotLocation.getHeading());
        }
    }
}
