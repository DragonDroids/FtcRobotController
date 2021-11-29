package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;

import java.util.concurrent.atomic.AtomicReference;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // System.setProperty("sun.java2d.opengl", "true");

        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        char positionR = 'R';
        String POSITION_KEY = "RCL";
        int position = POSITION_KEY.indexOf(positionR) - 1;
        double angleWidth = 23;
        double shippingAngleTurnCalc = 0;
        double shippingDistCalc = 0.0;
        if (position == 1) {
            shippingAngleTurnCalc = -79.408;
            shippingDistCalc = 18.0;
        } else if (position == 0) {
            shippingAngleTurnCalc = -50.0;
            shippingDistCalc = 14.0;
        } else {
            shippingAngleTurnCalc = -20.0;
            shippingDistCalc = 11.0;
        }
        double shippingAngleTurn = shippingAngleTurnCalc;
        double shippingDist = shippingDistCalc;

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
                        drive.trajectorySequenceBuilder(new Pose2d(-35, -61.75, Math.toRadians(180)))
                                // Move away from wall so we don't crash when turning
                                .strafeRight(5)
                                // Turn to the right angle so that we are facing the Team Element
                                .turn(Math.toRadians(-90 + position * angleWidth))
                                // Move towards Team Element
                                .forward(12 + Math.abs(position) * 2)
                                // To Do: Add pickup of Team Element
                                .turn(Math.toRadians(shippingAngleTurn))
                                // Move towards Shipping Hub
                                .forward(shippingDist)
                                .build()
                )
                .start();
    }
}
