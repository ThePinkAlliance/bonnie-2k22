// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ThePinkAlliance.core.joystick.Axis;
import com.ThePinkAlliance.core.joystick.Buttons;
import com.ThePinkAlliance.core.joystick.Joystick;
import com.ThePinkAlliance.core.joystick.JoystickAxis;
import com.ThePinkAlliance.core.limelight.Limelight;
import com.ThePinkAlliance.core.pathweaver.PathChooser;
import com.ThePinkAlliance.core.pathweaver.PathFactory;
import com.ThePinkAlliance.core.selectable.SelectableTrajectory;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.Drive;
import frc.robot.commands.TestSwerveModules;
import frc.robot.subsystems.Base;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final Joystick mainJS = new Joystick(0);

  private JoystickAxis x = new JoystickAxis(mainJS, Axis.LEFT_X);
  private JoystickAxis y = new JoystickAxis(mainJS, Axis.LEFT_Y);
  private JoystickAxis rot = new JoystickAxis(mainJS, Axis.RIGHT_X);

  private final PathChooser m_pathChooser = new PathChooser("drivers", 2, 0);

  // The robot's subsystems and commands are defined here...
  private final Base m_base = new Base();

  // Make sure to calibrate the limelight Crosshairs before using it.
  private final Limelight m_limelight = new Limelight(33.3, 50);

  private final SelectableTrajectory trajectory = new SelectableTrajectory(
      "straight",
      "straight");

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the dashboard for operators.
    RobotPreferences.cleanKeys();
    RobotPreferences.initialize();

    configureDashboard();
  }

  public void configureDashboard() {
    m_pathChooser.registerDefault(trajectory);
  }

  /**
   * This method will configure the robot for teleop when teleopInit is called.
   */
  public void configureTeleopInit() {
    // Configure the button bindings
    configureButtonBindings();
  }

  public void configureTestInit() {

  }

  public Command getTestCommand() {
    return new InstantCommand();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    m_base.setDefaultCommand(new Drive(m_base, x, y, rot));

    // NOTE: Lets see if this new command setup will work.
    mainJS.getButton(Buttons.A).and(mainJS.getButton(Buttons.B)).whenActive(new TestSwerveModules(m_base));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // Resolves the selected command that will run in autonomous
    return new PathFactory(
        m_base.getKinematics(),
        () -> m_base.getPose(),
        Constants.X_GAINS,
        Constants.Y_GAINS,
        Constants.THETA_GAINS,
        RobotPreferences.getVelocityPreference(),
        RobotPreferences.getAngularVelocityPreference())
        .buildController(
            m_pathChooser.get(),
            states -> {
              m_base.setStates(states);
            },
            m_base)
        .andThen(
            () -> {
              m_base.drive(new ChassisSpeeds());
            });
  }
}
