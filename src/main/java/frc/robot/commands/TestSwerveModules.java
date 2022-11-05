// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Base;

enum Stage {
  STAGE_1("Testing front left & Back right pods"),
  STAGE_2("Testing front right & Back left pods"),
  STAGE_3("Testing the left pods."),
  STAGE_4("Testing the right pods."),
  STOP("Stopped!");

  private String title;

  Stage(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}

public class TestSwerveModules extends CommandBase {
  Base base;
  Stage currentStage = Stage.STAGE_1;
  Watchdog watchdog;

  double targetAngle = 90;

  /** Creates a new DebugSwerveModules. */
  public TestSwerveModules(Base base) {
    // Use addRequirements() here to declare subsystem dependencies.

    this.base = base;
    this.watchdog = new Watchdog(5, () -> {
      cleanupModules();

      switch (currentStage) {
        case STAGE_1:
          currentStage = Stage.STAGE_2;
          watchdog.reset();
          break;
        case STAGE_2:
          currentStage = Stage.STAGE_3;
          watchdog.reset();
          break;
        case STAGE_3:
          currentStage = Stage.STAGE_4;
          watchdog.reset();
          break;
        case STAGE_4:
          currentStage = Stage.STOP;
          break;
        default:
          break;
      }
    });

    addRequirements(base);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    watchdog.enable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SwerveModuleState[] states = base.getModuleStates();

    switch (currentStage) {
      case STAGE_1:
        states[0].angle = new Rotation2d(targetAngle);
        states[3].angle = new Rotation2d(targetAngle);

        break;
      case STAGE_2:
        states[1].angle = new Rotation2d(targetAngle);
        states[2].angle = new Rotation2d(targetAngle);
        break;
      case STAGE_3:
        states[0].angle = new Rotation2d(targetAngle);
        states[2].angle = new Rotation2d(targetAngle);
        break;
      case STAGE_4:
        states[1].angle = new Rotation2d(targetAngle);
        states[3].angle = new Rotation2d(targetAngle);
        break;
      default:
        break;
    }

    base.setStates(states);

    SmartDashboard.putString("Swerve Test Mode Status", currentStage.getTitle());
  }

  private void cleanupModules() {
    SwerveModuleState[] states = base.getModuleStates();

    for (int i = 0; i < states.length; i++) {
      SwerveModuleState state = states[i];

      state.angle = new Rotation2d();
      state.speedMetersPerSecond = 0;

      states[i] = state;
    }

    base.setStates(states);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    watchdog.disable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return currentStage == Stage.STOP;
  }
}
