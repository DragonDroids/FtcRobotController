package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class MeepMeepTesting {
    public static void main(String[] args) {
        //System.setProperty("sun.java2d.opengl", "true");

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

        // Declare a MeepMeep instance
        // With a field size of 800 pixels

        MeepMeep mm = new MeepMeep(800)
                // Set field image
                .setBackground(MeepMeep.Background.FIELD_FREIGHT_FRENZY)
                // Set theme
                .setTheme(new ColorSchemeBlueDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPos)
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
                                .build()
                )
                .start();
    }
}