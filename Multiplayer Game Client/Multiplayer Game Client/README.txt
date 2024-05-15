=============
README
=============

- This is a small Java Swing project I undertook to teach myself net code and multithreading concepts.
- It is meant to be run on a dedicated server (project files included here) but the client will run without the server.
- For convenience, the server and client have been packaged into executable .jar files separately.
- Will need JRE to run the jar files, JDK 22.0.1 was used for the project: https://www.oracle.com/java/technologies/downloads/#jdk21-windows

- The login screen will prompt for a username. That username is used to communicate game packets between clients during the game session.

=============
CONTROLS:
=============
- WASD for movement
- Left click shoots fireballs at the mouse position on the screen
- From inside a game lobby, pressing ESC key will open the game menu, allowing the user to return to the main menu
- Left click navigates the menus and respawns

=============
NOTES:
=============
- There is a 0.5 second internal cooldown on casting another fireball to prevent spamming and make the game a bit more competitive in multiplayer.
- Projectiles are designed to collide with one another before detonating, so shooting a fireball is a valid tactic for avoiding getting hit if you cannot move out of the way.
- Fireballs leave behind a burning patch which does damage over time, more rapidly while standing in the fire.
- Player health is 100, but the blood droplets that indicate health only show 10 health increments, rounded down. They are meant to give you a rough idea of your current health. Ex: 29 health = 2 droplets, 30 health = 3 droplets. This means if you have less than 10 health, but more than 0, you will be alive but it may appear you have no health remaining.

=============
KNOWN ISSUES:
=============
- Projectiles should have used ConcurrentHashMaps instead of ArrayLists. It's a challenge to make this evident given the small scale of the game.
- I was learning as I went and was aware the server should be authoritative for many of the checks that are client-side (i.e., collisions, final determination of damage dealt, position, etc.) but decided to see if I could get it working before worrying about that. With enough latency, this can lead to a player being dead on one client's screen while they are alive on another. If this causes a desync in player state and/or position, pressing ESC and exiting to main menu, then returning to game lobby will fix it.
- Also due to lack of server authority, the method for preventing players spawning inside one another and getting stuck that way can also cause players to teleport to nearby locations when running into each other (if there is enough latency).