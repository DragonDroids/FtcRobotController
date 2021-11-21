package org.firstinspires.ftc.teamcode.libs;

public class RobotPosition {
    public int frontLeftTarget;
    public int frontRightTarget;
    public int backLeftTarget;
    public int backRightTarget;

    public RobotPosition (int frontLeftPos, int frontRightPos, int backLeftPos, int backRightPos) {
        frontLeftTarget = frontLeftPos;
        frontRightTarget = frontRightPos;
        backLeftTarget = backLeftPos;
        backRightTarget = backRightPos;
    }
}
