package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RoadRunnerAutoHardware;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.Arrays;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives in a DISTANCE-by-DISTANCE square indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin driving in a square.
 * You should observe the target position (green) and your pose estimate (blue) and adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleTankDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */
@Config
@Autonomous(group = "drive")
public class RoadRunnerAuto extends LinearOpMode {
    public static double DISTANCE = 48; // in

    @Override
    public void runOpMode() throws InterruptedException {
        RoadRunnerAutoHardware drive = new RoadRunnerAutoHardware(hardwareMap);

        // Declare positions variables
        char positionR = 'C';
        String robotPositionR = "RL";

        // Set path values
        String POSITION_KEY = "RCL";
        String[] ROBOT_POSITION_KEY = {
                "BL", "BR",
                "RL", "RR"
        };
        int position = POSITION_KEY.indexOf(positionR) - 1;
        int robotPosition = Arrays.asList(ROBOT_POSITION_KEY).indexOf(robotPositionR);
        double angleWidth = 21;
        double shippingAngleTurnCalc = 0;
        double shippingDistCalc = 0.0;

        Pose2d startPos;
        int s = 1;

        switch (robotPosition) {
            case 0:
                startPos = new Pose2d(-35, 61.75, Math.toRadians(270));
                s = -1;
                break;
            case 1:
                startPos = new Pose2d(11, 61.75, Math.toRadians(270));
                position *= -1;
                break;
            case 2:
                startPos = new Pose2d(-35, -61.75, Math.toRadians(90));
                break;
            case 3:
                startPos = new Pose2d(11, -61.75, Math.toRadians(90));
                s = -1;
                position *= -1;
                break;
            default:
                startPos = new Pose2d(0, 0, Math.toRadians(90));
                break;
        }

        int sign = s;

        switch (position) {
            case 1:
                shippingAngleTurnCalc = -79.408;
                shippingDistCalc = 18.0;
                break;
            case 0:
                shippingAngleTurnCalc = -50.0;
                shippingDistCalc = 14.0;
                break;
            default:
                shippingAngleTurnCalc = -20.0;
                shippingDistCalc = 11.0;
                break;
        }

        double shippingAngleTurn = shippingAngleTurnCalc;
        double shippingDist = shippingDistCalc;
        int pos = position;

        drive.setPoseEstimate(startPos);

        telemetry.addData("Ready", "to start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPos)
                    // Move away from wall so we don't crash when turning
                    .forward(5)
                    // Turn to the right angle so that we are facing the Team Element
                    .turn(sign * Math.toRadians(pos * angleWidth))
                    // Move towards Team Element
                    .forward(12 + Math.abs(pos) * 2)
                    // To Do: Add pickup of Team Element
                    .turn(sign * Math.toRadians(shippingAngleTurn))
                    // Move towards Shipping Hub
                    .forward(shippingDist)
                    .build();
            drive.followTrajectorySequence(trajSeq);
        }
    }
}
