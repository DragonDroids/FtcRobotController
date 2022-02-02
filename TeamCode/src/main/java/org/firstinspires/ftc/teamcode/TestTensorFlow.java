package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

/*
    Dragon Droids Team #19643
    TestTensorFlow
*/

@Config

@Autonomous(group = "drive")
public class TestTensorFlow extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RoadRunnerAutoHardware drive = new RoadRunnerAutoHardware(hardwareMap);
        TensorFlow tensorFlow = new TensorFlow(hardwareMap);

        // Detect positions and store in variable
        char positionDetected = 'N';
        while (positionDetected == 'N' && (opModeIsActive() || !isStopRequested())) {
            positionDetected = tensorFlow.detectPosition(telemetry, true);
            sleep(10);
        }

        telemetry.update();

        int index = "RCL".indexOf(positionDetected);

        if (isStopRequested()) return;

        waitForStart();

        drive.setPoseEstimate(new Pose2d(-30,60,Math.toRadians(270)));

        // Turns and moves to Hub
        TrajectorySequence trajectorySequence0 = drive.trajectorySequenceBuilder(new Pose2d(-30,60,Math.toRadians(270)))
                // Gives space from wall
                .forward(6)
                // Turns to face Alliance Shipping Hub
                .turn(-Math.toRadians(140))
                // Backs into Hub
                .back(25)
                .build();

        // Moves Closer To Hub
        TrajectorySequence trajectorySequence1 = drive.trajectorySequenceBuilder(trajectorySequence0.end())
                // Backs into Hub more
                .back(5)
                .build();

        TrajectorySequence trajectorySequence2 = drive.trajectorySequenceBuilder(trajectorySequence1.end())
                .forward(10)
                .build();

        TrajectorySequence trajectorySequence3 = drive.trajectorySequenceBuilder(trajectorySequence2.end())
                .forward(22)
                .turn(Math.toRadians(50))
                .forward(13)
                .build();

        TrajectorySequence trajectorySequence4 = drive.trajectorySequenceBuilder(trajectorySequence3.end())
                .back(25)
                .turn(Math.toRadians(10))
                .back(25)
                .splineTo(new Vector2d(12,46.5), 0)
                .build();

        TrajectorySequence trajectorySequence5 = drive.trajectorySequenceBuilder(trajectorySequence4.end())
                .back(65)
                .build();

        drive.followTrajectorySequence(trajectorySequence0);

        drive.armLift.setTargetPosition(drive.armPositions[index]);
        drive.armLift.setPower(0.75);
        while (drive.armLift.isBusy() && opModeIsActive()) {}
        drive.followTrajectorySequence(trajectorySequence1);
        if (drive.armPositions[index] == -575) {
            drive.armClamp.setPosition(0.0);
        } else if(drive.armPositions[index] != 0) {
            drive.armClamp.setPosition(1.0);
        }

        sleep(500);

        drive.armClamp.setPosition(0.5);

        drive.followTrajectorySequence(trajectorySequence2);

        drive.armLift.setTargetPosition(0);
        drive.armLift.setPower(0.75);
        while (drive.armLift.isBusy() && opModeIsActive()) {}

        drive.followTrajectorySequence(trajectorySequence3);

        drive.carouselSpinner.setPower(0.65);

        sleep(2100);

        drive.carouselSpinner.setPower(0.0);

        drive.followTrajectorySequence(trajectorySequence4);

        DriveConstants.MAX_ACCEL = 110;
        drive.followTrajectorySequence(trajectorySequence5);
        DriveConstants.MAX_ACCEL = 90;

        telemetry.addData("Run", "Done!");
    }
}