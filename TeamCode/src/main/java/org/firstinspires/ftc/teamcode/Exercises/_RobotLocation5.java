package org.firstinspires.ftc.teamcode.Exercises;

public class _RobotLocation5 {
    double angleRadians;
    double x;
    double y;


    public _RobotLocation5(double angleDegrees, double x, double y) {
        this.angleRadians = Math.toRadians(angleDegrees);
        this.x = x;
        this.y = y;
    }

    public double getHeading() {
        double angle = this.angleRadians;
        while (angle < Math.PI) {
            angle -= 2 * Math.PI;
        }
        while (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        return Math.toDegrees(angle);
    }

    @Override
    public String toString() {
        return "_RobotLocation5: angle (" + angleRadians + ")";
    }

    public void turn(double angleChangeDegrees) {
        angleRadians += Math.toRadians(angleChangeDegrees);
    }

    public void setAngle(double angleDegrees) {
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    public double getX() {
        return x;
    }

    public void changeX(double change) {
        x += change;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void changeY(double change) {
        y += change;
    }

    public void setY(double y) {
        this.y = y;
    }
}
