package org.firstinspires.ftc.teamcode.debug;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

/*
    Dragon Droids Team #19643
    TestTensorFlow
*/

@Config

@Autonomous(group = "drive")
public class RoadRunnerTurnTuning extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap, false);

        drive.setPoseEstimate(new Pose2d(-30,60,Math.toRadians(270)));

        double avg = 0;

        double[] errors = {
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        };

        double TURN_RATIO;

        if (isStopRequested()) return;

        waitForStart();

        for (int i = 0; i < errors.length; i++) {
            double angleToTurn = 20 + Math.round(Math.random() * 250);
            telemetry.addData("AngleToTurn" + i, angleToTurn);
            drive.imu.initialize(drive.parameters);
            sleep(5);

            TrajectorySequence trajectorySequence = drive.trajectorySequenceBuilder(new Pose2d(-30, 60,Math.toRadians(drive.getAngle() + 270)))
                    .turn(Math.toRadians(angleToTurn))
                    .waitSeconds(0.05)
                    .build();

            double initialIMU = drive.getAngle();
            drive.followTrajectorySequence(trajectorySequence);
            double lastIMU = drive.getAngle();

            double error = (lastIMU - initialIMU) - angleToTurn;

            errors[i] = (lastIMU - initialIMU) / angleToTurn;
            telemetry.addData("Degree Change " + i, error);
        }

        for (int i = 0; i < errors.length; i++) {
            telemetry.addData("Error " + i, errors[i]);
            avg += errors[i] / errors.length;
        }

        telemetry.addData("Average", avg);

        TURN_RATIO = avg;

        drive.imu.initialize(drive.parameters);
        sleep(5);

        double START_DEG = drive.getAngle();

        telemetry.addData("Start", 0);
        TrajectorySequence trajectorySequence = drive.trajectorySequenceBuilder(new Pose2d(-30, 60, Math.toRadians(drive.getAngle() + 270)))
                .turn(Math.toRadians(90 / (TURN_RATIO)))
                .build();

        telemetry.addData("DegWithTurnRatio", 90 / (TURN_RATIO));

        drive.followTrajectorySequence(trajectorySequence);

        telemetry.addData("End", drive.getAngle() - START_DEG);
        telemetry.update();

        while (!isStopRequested()) sleep(30);
    }
}