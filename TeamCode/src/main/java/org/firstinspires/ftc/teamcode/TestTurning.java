package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

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

        drive.setPoseEstimate(new Pose2d(-30,60,Math.toRadians(270)));

        if (isStopRequested()) return;

        waitForStart();

        TrajectorySequence trajectorySequence = drive.trajectorySequenceBuilder(new Pose2d(-30, 60, Math.toRadians(270)))
                .turn(Math.toRadians(90 * drive.turnRatio))
                .turn(Math.toRadians(180 * drive.turnRatio))
                .turn(Math.toRadians(63 * drive.turnRatio))
                .build();

        drive.followTrajectorySequence(trajectorySequence);

        telemetry.addData("Turn Error", drive.getAngle());

        telemetry.update();

        while (!isStopRequested()) sleep(30);
    }
}