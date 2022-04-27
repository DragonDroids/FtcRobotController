package org.firstinspires.ftc.teamcode.debug;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.opencv.core.Mat;

/*
    Dragon Droids Team #19643
    TestTensorFlow
*/

@Config

@Autonomous(group = "drive",name="turningStuff")
public class TestTurning extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap);



        if (isStopRequested()) return;

        waitForStart();

        Pose2d last = new Pose2d();


        while (opModeIsActive()) {
            TrajectorySequence t1 = drive.trajectorySequenceBuilder(last)
                    .turn(Math.toRadians(90))
                    .build();

            drive.followTrajectorySequence(t1);

            last = t1.end();

            telemetry.addData("Turn", drive.getAngle());
            telemetry.update();
            sleep(1000);
        }
    }
}