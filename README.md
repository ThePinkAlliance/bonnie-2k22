# Bonnie 2k22

Bonnie is a research drivetrain mainly for tasks related to swerve drive development however I plan on using it to do some testing with photon visions new apriltag pipelines.

## Development Practices

Testing will be required for critical systems that are testable, eg photovision apriltags, subsystems, and commands.

Development across multiple developers will be handled with each developer having a branch starting with their desired name followed with two hyphens then the feature name here's an example name `samuelv--field-centric`.

_**please note that a feature needs to meet one of these requirements to have it's own branch**_.

- Changes majority of the functionality of a subsystem or command.
- Integrates functionality between a subsystem with another.
- Creation of a new subsystem.
- Changes made during a competition.

## Getting Started

Cloning the project requires a special flag because of the included libraries (core, swerve-lib), In order to do this use the `--recurse-submodule` flag when cloning.

```shell
git clone https://github.com/ThePinkAlliance/greg-bot-2k22.git --recurse-submodules
```

## Roadmap

Most of the research that will be done using the bonnie drivetrain will be layed out in the roadmap section however this is subject to change.

- [ ] Field centric research.
  - The current setup for field centric driving works until the drivetrain is horizonal, figuring this out is a must.
- [ ] Apritag research with photonvision.
  - Find the margin of error with distance estimation with apriltags.
  - Make distance estimation accurate for competition use.
  - Research pose interpolation using the distance from the apriltags and the velocity/acceleration of the robot.
- [ ] Development of some custom pathweaver executors.
