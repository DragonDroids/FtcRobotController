/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file illustrates the concept of driving a path based on time.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backwards for 1 Second
 *   - Stop and close the claw.
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Pushbot: Auto Drive By Time", group="Linear Opmode")

public class MecanumAutonomous extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbot         robot   = new HardwarePushbot();
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        // Set what happens after power is set to 0.0
        robot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Move Forward
        moveMotors(0.0, 0.5, 0, 0.5);
        moveMotors(1.0, 0.0, 0, 0.5);
    }

    public void stopMotors() {
        robot.frontLeftDrive.setPower(0);
        robot.frontRightDrive.setPower(0);
        robot.backLeftDrive.setPower(0);
        robot.backRightDrive.setPower(0);
    }

    public void moveMotors(double x, double y, double rx, double distance) {
        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        //double positions = (robot.frontLeftDrive.getCurrentPosition() + robot.frontRightDrive.getCurrentPosition() + robot.backLeftDrive.getCurrentPosition() + robot.backRightDrive.getCurrentPosition()) / 4

        // Put powers in the range of -1 to 1 only if they aren't already
        // Not checking would cause us to always drive at full speed
        if (Math.abs(frontLeftPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(backRightPower) > 1) {
            // Find the largest power
            double max = 0;
            max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            max = Math.max(Math.abs(frontRightPower), max);
            max = Math.max(Math.abs(backRightPower), max);

            // Divide everything by max (it's positive so we don't need to worry
            // about signs)
            frontLeftPower /= max;
            backLeftPower /= max;
            frontRightPower /= max;
            backRightPower /= max;
        }

        // Set Powers
        robot.frontLeftDrive.setPower(frontLeftPower);
        robot.backLeftDrive.setPower(backLeftPower);
        robot.frontRightDrive.setPower(frontRightPower);
        robot.backRightDrive.setPower(backRightPower);

        while (opModeIsActive()) {
            if (distance > 5) {
                stopMotors();
                break;
            }
        }
    }
}
