# Relay race

### Description

It's a simple console Spring Boot application which simulates a relay race.

The teams start the race at the same time and each runner wait for the previous runner in the team to finish before they can start. Each runner randomly take between 9.0 and 10.5 seconds to complete their part of the race.

The finishing time and position of each team printed out the results as soon as each team completes their race to simulate a live event.

Sample result:<br />
1st Team 6 - 36.851 seconds<br />
2nd Team 3 - 38.618 seconds<br />
3rd Team 4 - 38.942 seconds<br />
4th Team 1 - 39.469 seconds<br />
5th Team 5 - 39.672 seconds<br />
6th Team 2 - 40.989 seconds<br />

### Usage

The default simulation (6 teams and 4 runners) automatically starts with the app. This can be reconfigured in `AppStartupRunner` class.