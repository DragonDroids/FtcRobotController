package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

@Autonomous(name="TESTMOTOR")

public class RevCoreMotorSpeedTest extends LinearOpMode {
    private HardwarePushbot drive = new HardwarePushbot();
    public void runOpMode() throws InterruptedException {

        drive.init(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

//        drive.motorTest.setPower(1.0);
//
//        sleep(5000);
//
//        drive.motorTest.setPower(-1.0);
//
//        sleep(5000);
//
//        drive.motorTest.setPower(1.0);
//
//        sleep(5000);
//
//        drive.motorTest.setPower(0.0);

        while (!isStopRequested()) {
            drive.motorTest.setPower(1);
        }

        telemetry.addData("STRING BOI", "done");
    }
}
