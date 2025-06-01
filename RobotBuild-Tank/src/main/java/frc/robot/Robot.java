// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

/**
 * The methods in this class are called automatically corresponding to each
 * mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the
 * package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  // Keshav edits: Creating new variables for SparkMax motors
  private SparkMax leftLeader;
  private SparkMax leftFollower;
  private SparkMax rightLeader;
  private SparkMax rightFollower;

  // Creating joysticks for drive control
  private Joystick leftJoystick;
  private Joystick rightJoystick;

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Keshav Edits:
    // Initialize Motors:
    // Set followers to leaeders
    leftLeader = new SparkMax(1, SparkLowLevel.MotorType.kBrushed);
    leftFollower = new SparkMax(2, SparkLowLevel.MotorType.kBrushed);
    rightLeader = new SparkMax(3, SparkLowLevel.MotorType.kBrushed);
    rightFollower = new SparkMax(4, SparkLowLevel.MotorType.kBrushed);


    // Persist parameters to retain configuration in the event of a power cycle
    // Configure left leader (not inverted)
    SparkMaxConfig leftConfig = new SparkMaxConfig();
    leftConfig
        .smartCurrentLimit(40)
        .idleMode(IdleMode.kBrake);
    leftLeader.configure(leftConfig, SparkBase.ResetMode.kResetSafeParameters,
        SparkBase.PersistMode.kPersistParameters);

    // Configure right leader (inverted)
    SparkMaxConfig rightConfig = new SparkMaxConfig();
    rightConfig
        .smartCurrentLimit(40)
        .idleMode(IdleMode.kBrake)
        .inverted(true); // Only the right side is inverted
    rightLeader.configure(rightConfig, SparkBase.ResetMode.kResetSafeParameters,
        SparkBase.PersistMode.kPersistParameters);

    // Configure followers to follow their respective leaders
    SparkMaxConfig followerConfig = new SparkMaxConfig();
    followerConfig
        .smartCurrentLimit(40)
        .idleMode(IdleMode.kBrake);

    // Left follower follows left leader
    followerConfig.follow(leftLeader.getDeviceId());
    leftFollower.configure(followerConfig, SparkBase.ResetMode.kResetSafeParameters,
        SparkBase.PersistMode.kPersistParameters);

    // Right follower follows right leader
    followerConfig.follow(rightLeader.getDeviceId());
    rightFollower.configure(followerConfig, SparkBase.ResetMode.kResetSafeParameters,
        SparkBase.PersistMode.kPersistParameters);

    // Set up Joysticks!
    leftJoystick = new Joystick(0); // Left joystick on port 0
    rightJoystick = new Joystick(1); // Right joystick on port 1

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items
   * like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different
   * autonomous modes using the dashboard. The sendable chooser code works with
   * the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the
   * chooser code and
   * uncomment the getString line to get the auto name from the text box below the
   * Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure
   * below with additional strings. If using the SendableChooser make sure to add
   * them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Get joystick inputs
    double leftSpeed = -leftJoystick.getY(); // Left joystick Y-axis (inverted)
    double rightSpeed = -rightJoystick.getY(); // Right joystick Y-axis (inverted)

    // Apply deadband to avoid unintentional movement
    if (Math.abs(leftSpeed) < 0.1)
      leftSpeed = 0;
    if (Math.abs(rightSpeed) < 0.1)
      rightSpeed = 0;

    // Set motor speeds
    leftLeader.set(leftSpeed);
    rightLeader.set(rightSpeed);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }
}
