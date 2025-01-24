package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.util.Constants;

public class DriveMecanum {

    private Servo servoE, servoD;
    private DcMotor FR, FL, BR, BL;
    private LinearOpMode opMode;
    private ElapsedTime accTime;
    private double acc = 0, x = 0, y = 0, turn = 0, slowFactor = 0;

    public DriveMecanum(LinearOpMode opMode){

        this.opMode = opMode;

        FR = opMode.hardwareMap.get(DcMotor.class, "FD");
        FL = opMode.hardwareMap.get(DcMotor.class, "FE");
        BR = opMode.hardwareMap.get(DcMotor.class, "BD");
        BL = opMode.hardwareMap.get(DcMotor.class, "BL");

        FL.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        FR.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);

        //resetEnc();

        DcMotor[] motors = {FL, FR, BR, BL};

        for(DcMotor m : motors){
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        accTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        accTime.startTime();

        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        setSlowFactor(gamepad1.right_trigger);
    }

    public void periodic(){
        updateAcceleration(Math.abs(x) < 0.1 && Math.abs(y) < 0.1 && Math.abs(turn) < 0.1);

        //setDownEncoderServo(true);

        double vel = slowFactor * Constants.DTMecanum.SPEED * acc;

        FL.setPower(((y+x) + turn) * vel);
        FR.setPower(((y-x) - turn) * vel);
        BL.setPower(((y-x) + turn) * vel);
        BR.setPower(((y+x) - turn) * vel);
    }

    private void updateAcceleration(boolean release){

        if(release){
            acc = 0;
            accTime.reset();
            return;
        }

        acc = Math.min(1, accTime.time() / Constants.DTMecanum.ACCELERATION);
        acc = Math.round(acc * 1000.0) / 1000.0;
    }

    /*public void setDownEncoderServo(boolean act){
        servoE.setPosition(act ? 0 : 1);
        servoD.setPosition(act ? 0 : 1);
    }

    public void resetEnc(){
        for(DcMotor m : motors){
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }*/

    public void setSlowFactor(double slowFactor){
        this.slowFactor = 1 - slowFactor / 1.5;
    }



}
