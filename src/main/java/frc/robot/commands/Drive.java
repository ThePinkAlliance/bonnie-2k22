// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import com.ThePinkAlliance.core.joystick.JoystickAxis;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Base;

public class Drive extends CommandBase {

  Base m_base;

  JoystickAxis x;
  JoystickAxis y;
  JoystickAxis rot;

  SlewRateLimiter leftStickLimiter = new SlewRateLimiter(10);
  SlewRateLimiter rotStickLimiter = new SlewRateLimiter(10);

  /** Creates a new Drive. */
  public Drive(Base m_base, JoystickAxis x, JoystickAxis y, JoystickAxis rot) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_base = m_base;

    this.x = x;
    this.y = y;
    this.rot = rot;

    addRequirements(m_base);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = leftStickLimiter.calculate(this.x.get() * Constants.MAX_VELOCITY_METERS_PER_SECOND);
    double y = leftStickLimiter.calculate(this.y.getInverted() * Constants.MAX_VELOCITY_METERS_PER_SECOND);
    double rot = rotStickLimiter.calculate(this.rot.get() * Constants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);

    ChassisSpeeds speeds = new ChassisSpeeds(x, y, rot);
    SmartDashboard.putString("chassisSpeeds", speeds.toString());

    m_base.drive(speeds);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_base.drive(new ChassisSpeeds());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
