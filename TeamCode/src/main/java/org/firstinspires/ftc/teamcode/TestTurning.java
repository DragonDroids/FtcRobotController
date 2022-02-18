package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.opencv.core.Mat;

/*
    Dragon Droids Team #19643
    TestTensorFlow
*/

@Config

@Autonomous(group = "drive")
public class TestTurning extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RoadRunnerAutoHardware drive = new RoadRunnerAutoHardware(hardwareMap);

        TensorFlow tensorflow = new TensorFlow(hardwareMap);
        char detection = 'N';
        while (!opModeIsActive()) {
            detection = tensorflow.detectPosition(telemetry);
            telemetry.addData("Pos", detection);
            telemetry.update();
            sleep(30);
        }

        if (isStopRequested()) return;

        waitForStart();

        telemetry.addData("Pos", detection);
        telemetry.update();

        while (!isStopRequested()) sleep(30);
    }
}