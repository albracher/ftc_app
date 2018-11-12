package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import java.util.*;

//*In theory* this should also be compatible with tank drive.

@TeleOp(name ="WestCoastDrive", group ="TeleOp")
public class WestCoastDrive extends LinearOpMode{

    public static final double ARM_SPEED = 1;
    public static final double SPEED = 0.75;
    //public static final double ARM_SPEED = 0.5;
    public static final double INTAKE_SPEED = 0.5;

    /* Declare OpMode members. */
    HardwareConfig robot           = new HardwareConfig();   //Configs hardware


    @Override
    public void runOpMode () throws InterruptedException
    {

        robot.init(hardwareMap);
        //loads hardwareMap

        double drive;
        double turn;
        double leftValue;
        double rightValue;
        double armPower;
        boolean runIntake = false;
        boolean reverseIntake = false;
        boolean slowIntake = false;
        double SPEED = 0.5;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Shock drone going live!");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "ASSUMING DIRECT CONTROL");
        telemetry.update();

        while(opModeIsActive())
        {
            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            drive = -gamepad1.left_stick_y;
            turn  =  gamepad1.right_stick_x;

            // Combine drive and turn for blended motion.
            leftValue  = -(drive + turn);
            rightValue = -(drive - turn);

            //apply acceleration curve for additional driver control
            leftValue *= Math.abs(leftValue);
            rightValue *= Math.abs(rightValue);

            //applies speed limiter
            leftValue *= SPEED;
            rightValue *= SPEED;



            //right trigger raises, left trigger lowers
            //both gamepads can control the arm
            //gamepad2 can use left stick for fine arm control
            armPower = (((gamepad1.right_trigger+gamepad2.right_trigger)+(0.1*-gamepad2.left_stick_y))-(gamepad1.left_trigger+gamepad2.left_trigger));

            armPower *= ARM_SPEED;

            if(gamepad1.a == true){
                runIntake = true;
                reverseIntake = false;
                slowIntake = false;
            }
            if(gamepad1.x == true) {
                runIntake = false;
                reverseIntake = true;
                slowIntake = false;
            }
            if(gamepad1.b == true) {
                runIntake = false;
                reverseIntake = false;
                slowIntake = false;
            }
            if(gamepad1.a == true){
                runIntake = true;
                reverseIntake = false;
                slowIntake = false;
            }
            if(gamepad1.y == true){
                runIntake = false;
                reverseIntake = false;
                slowIntake = true;
            }
            if(gamepad1.dpad_up == true){
                SPEED = 1;
            }
            if(gamepad1.dpad_down==true) {
                SPEED = 0.3;
            }
            if(runIntake){
                robot.intakeL.setPower(INTAKE_SPEED);
                robot.intakeR.setPower(INTAKE_SPEED);
            } else if (reverseIntake) {
                robot.intakeL.setPower(-0.1);
                robot.intakeR.setPower(-0.1);
            } else if (slowIntake){
                robot.intakeL.setPower(0.1);
                robot.intakeR.setPower(0.1);
            } else {
                robot.intakeL.setPower(0);
                robot.intakeR.setPower(0);
            }

            //sets maxes for each value
            leftValue = Range.clip(leftValue, -SPEED, SPEED);
            rightValue = Range.clip(rightValue, -SPEED, SPEED);
            armPower = Range.clip(armPower, -ARM_SPEED, ARM_SPEED);



            robot.motorFL.setPower(leftValue);
            robot.motorFR.setPower(rightValue);
            robot.motorRL.setPower(leftValue);
            robot.motorRR.setPower(rightValue);
            robot.armL.setPower(armPower);
            robot.armR.setPower(armPower);

            telemetry.addData("Status", "Left: "+ leftValue+"        Right: "+ rightValue+"\n" +
                    "Power: "+ drive +"        Turn: "+turn+"\n"+
            "Arm Power: "+armPower+"     Intake Power: "+INTAKE_SPEED);
            telemetry.update();
    }
}




                    //idle();
}