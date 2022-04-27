package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.dev.TensorFlow;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.SaveJSON;
import org.json.JSONObject;

/*
    Dragon Droids Team #19643
    RoadRunnerAutoBL
*/

@Config

@Autonomous(name="Blue Carousel", group = "drive")
public class RoadRunnerAutoBL extends LinearOpMode {
    public int[] armPositions = {
            -320, // Bottom Position
            -360, // Middle Position
            -575, // High Position
    };

    /*
        Start TensorFlow
     */

    private static final String TFOD_MODEL_ASSET = "model_ycup.tflite";

    private static final String[] LABELS = {
            "ycup",
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final char[] POSITION_KEY = {
            'L',
            'C',
            'R',
            'N'
    };

    private static final String VUFORIA_KEY =
            "Ac5+EOX/////AAABmYZoDckxSUV7iTj4MtRwiJpvftO8hpoEtcxmCLDolUOn81SCtt0u8igYx5S9Gz9UpcHI66Vto0Wy1IhFoZ4J36MXjIwxdHCb/" +
                    "81+4+4DhbQ2tWq9Xisz4NvAu2l1IN8uj6qvmjmg02YfS7+REk+/NxVrS15d2fvFh7lE9RMlLtMwgkK9903e2mgxf48yL9IQMXoTfBJhY3" +
                    "X4cSKSzz4XGKDjqvIXnAd47NYB8TXuOwY0N8bL9+jPNPaw3E2SgOU2imUU6kCAQvrUPF24AI1FqtvlhZbeYLe/EQVJaC2fqcODw2Xp5px" +
                    "j1h4lS6tGXQqRZ1we0i4Wf/S+1/A4GDcb3B7hfpkRJP6AYvLxisBlP2qj";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    final boolean debugMode = false;

    /*
        End TensorFlow
     */

    @Override
    public void runOpMode() throws InterruptedException {
        // Blue Carousel
        Drive drive = new Drive(hardwareMap);

        TensorFlow tensorflow = new TensorFlow(hardwareMap);

        // Declare positions variables
        char positionDetected = 'L';
        while (!opModeIsActive()) {
            positionDetected = tensorflow.detectPosition(telemetry);
            positionDetected = positionDetected == 'N' ? 'L' : positionDetected;
            sleep(10);
            telemetry.addData("Pos Detected: ", positionDetected);
            telemetry.update();
        }

        int index = "RCL".indexOf(positionDetected);
        if (isStopRequested()) return;

        waitForStart();


        sleep(1000);

        drive.setPoseEstimate(new Pose2d(-30, 60, Math.toRadians(270)));

        // Turns and moves to Hub
        TrajectorySequence trajectorySequence0 = drive.trajectorySequenceBuilder(new Pose2d(-30, 60, Math.toRadians(270)))
                // Gives space from wall
                .forward(6)
                // Turns to face Alliance Shipping Hub
                .turn(-Math.toRadians(135))
                // Backs into Hub
                .back(22)
                .build();

        // Moves Closer To Hub
        TrajectorySequence trajectorySequence1 = drive.trajectorySequenceBuilder(trajectorySequence0.end())
                // Backs into Hub more
                .back(5)
                .build();

        TrajectorySequence trajectorySequence2 = drive.trajectorySequenceBuilder(trajectorySequence1.end())
                .forward(13)
                .build();

        TrajectorySequence trajectorySequence3 = drive.trajectorySequenceBuilder(trajectorySequence2.end())
                .forward(18)
                .turn(Math.toRadians(50))
                .forward(13)
                .build();

        drive.followTrajectorySequence(trajectorySequence0);

        drive.armLift.setTargetPosition(armPositions[index]);
        drive.armLift.setPower(0.75);
        while (drive.armLift.isBusy() && opModeIsActive()) {
        }
        drive.followTrajectorySequence(trajectorySequence1);
        if (armPositions[index] == -575) {
            drive.armClamp.setPosition(0.0);
        } else if (armPositions[index] != 0) {
            drive.armClamp.setPosition(1.0);
        }

        sleep(2000);

        drive.armClamp.setPosition(0.5);

        drive.followTrajectorySequence(trajectorySequence2);

        drive.armLift.setTargetPosition(0);
        drive.armLift.setPower(0.75);
        while (drive.armLift.isBusy() && opModeIsActive()) {
        }

        drive.followTrajectorySequence(trajectorySequence3);

        drive.carouselSpinner.setPower(0.65);

        sleep(2500);

        drive.carouselSpinner.setPower(0.0);

        TrajectorySequence trajectorySequence4 = drive.trajectorySequenceBuilder(trajectorySequence3.end())
                .back(10)
                .turn(-Math.toRadians(180 - drive.getAngle()))
                .forward(18)
                .turn(-Math.toRadians(90))
                .forward(25)
                .build();

        drive.followTrajectorySequence(trajectorySequence4);

        JSONObject position = new JSONObject();

        try {
            position.put("x", drive.getPoseEstimate().getX());
            position.put("y", drive.getPoseEstimate().getY());
            position.put("heading", drive.getPoseEstimate().getHeading());
            new SaveJSON("position.json", position.toString());
            telemetry.addData("Position", "Succeeded");
        } catch (Exception e) {
            telemetry.addData("Position", "Failed");
        }
        telemetry.update();

        telemetry.addData("Run", "Done!");
        telemetry.update();
    }
}