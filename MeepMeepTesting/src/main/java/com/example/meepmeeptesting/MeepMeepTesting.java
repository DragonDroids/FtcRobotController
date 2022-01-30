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
                        drive.trajectorySequenceBuilder(new Pose2d(-30,-60,Math.toRadians(90)))
                                .forward(6)
                                .turn(Math.toRadians(140))
                                .back(25)
                                .back(5)
                                .forward(10)
                                .turn(-Math.toRadians(50))
                                .forward(38)
                                .turn(Math.toRadians(90))
                                .forward(14)
                                .back(7)
                                .turn(-Math.toRadians(90))
                                .back(100)
                                .build()
                )
                .start();
    }
}