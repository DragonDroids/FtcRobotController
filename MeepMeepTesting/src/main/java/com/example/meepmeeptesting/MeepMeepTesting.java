package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // System.setProperty("sun.java2d.opengl", "true");

        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        char positionR = 'C';
        String POSITION_KEY = "RCL";
        int position = POSITION_KEY.indexOf(positionR) - 1;
        double angleWidth = 23;
        MeepMeep mm = new MeepMeep(800)
                // Set field image
                .setBackground(MeepMeep.Background.FIELD_FREIGHT_FRENZY)
                // Set theme
                .setTheme(new ColorSchemeRedDark())
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35, -61.75, Math.toRadians(180)))
                                .strafeRight(5)
                                .turn(Math.toRadians(-90 + position * angleWidth))
                                .forward(12 + Math.abs(position) * 2)
                                .build()
                )
                .start();
    }
}
