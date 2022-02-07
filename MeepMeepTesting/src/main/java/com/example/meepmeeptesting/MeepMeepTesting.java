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
        // Declare a MeepMeep instance
        // With a field size of 800 pixels

        MeepMeep mm = new MeepMeep(800)
                // Set field image
                .setBackground(MeepMeep.Background.FIELD_FREIGHT_FRENZY)
                // Set theme
                .setTheme(new ColorSchemeBlueDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width 7, 35
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-30, 60, Math.toRadians(270)))
                                // Gives space from wall
                                .forward(6)
                                // Turns to face Alliance Shipping Hub
                                .turn(-Math.toRadians(147.5))
                                // Backs into Hub
                                .back(25)
                                // Backs into Hub more
                                .back(5)
                                .forward(10)
                                .forward(22)
                                .turn(Math.toRadians(57.5))
                                .forward(13)
                                .back(25)
                                .back(25)
                                .splineTo(new Vector2d(12,46.5), 0)
                                .back(65)
                                .build()
                )
                .start();
    }
}