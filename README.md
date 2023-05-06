
# Fruit Ninja

A PC-platform imitation of the popular mobile game Fruit Ninja.

# Project overview

In Halfbrick's original Fruit Ninja, a series of fruits are presented to the user, each of which appears on screen only momentarily and must be slashed before it falls out of sight. Several gamemodes exist, but common elements between them include fruits which grant points when slashed, bombs which impose a penalty when slashed, and special items which afford temporary power-up benefits.

Our rendition of Fruit Ninja replicates all of these mechanics, in addition to several new components of our own design. For example, our Fruit Ninja project incorporates a variety of selectable difficulties, where the original Fruit Ninja game has only one per gamemode.

# Quirks and features

## Core

Our project is written entirely in Java, and its graphics depend predominantly upon the ACM interface library with small contributions from the Swing library. A custom physics environment forms the foundation of the game, allowing for context-dependent control of simulated gravity, object velocity, and wind speed/direction. Using this feature, we were able to implement a series of fruits, each with its own sound effects, graphics, point values, and special abilities (where required).

## Fruits
These fruits include **apples**, **watermelons**, **coconuts**, **bombs**, and "**bornanas**". The former three simply play a generic slash sound effect and award a quantity of points when cut, while the latter two play unique sound clips and either subtract points/time (bombs) or double the positive point values of all other fruits for a period of ten seconds (bornanas).

## Score
Each fruit, with the aforementioned exception of bombs and bornanas, has its own point value. This is a number which, upon the slashing of the fruit by the user, is added to a score counter whose current value is displayed onscreen throughout the game. At the end of a game, the score is displayed to the user briefly on a game-over screen, then added to one of two RAM-persistent ArrayLists of scores (one corresponding to Arcade mode, the other to Classic mode). From the game's main menu, the user may choose to view leaderboards, which sort and display the ten largest scores currently in each ArrayList.

## Gamemodes
From the main menu, the user may select between two gamemodes: Classic or Arcade. Both of these modes are also found in the original Fruit Ninja game, and our implementations replicate them closely, with the exception of a few unique variations. In Classic mode, the user must slice as many fruits as possible, while avoiding bombs or dropped fruits. For each bomb sliced and each fruit dropped, a penalty is awarded. When three penalties are accrued, the game ends. In Arcade mode, no penalty counter exists; it is replaced by a timer, which begins at 120 seconds and counts down to the end of the game. Slicing a bomb will subtract five seconds from the timer, while slicing a bornana (a fruit exclusive to Arcade mode) will double the positive point value of all other fruits for ten seconds.

## Main menu
The main menu is an interface displayed at launch and between games. From this menu, the user may choose between the two aforementioned gamemodes, view the game's credits, or view the leaderboards.

## Music
Music plays during games, as well as on the main menu and credits screens. The title and artist of each song is shown below, alongside the portions of the game in which it appears:

Main menu and credits screen: **MapleStory Title Theme** by Wizet/Nexon

Arcade mode: **The Ultimate Showdown of Ultimate Destiny (instrumental)** by Lemon Demon

Classic mode (baby difficulty): **Lemon Candy** by Chara

Classic mode (normal difficulty): **Freeze** by Way To D

Classic mode (extreme difficulty): **Through the Fire and Flames** by DragonForce

# Usage

Binaries are provided in the repository, so no compilation is necessary. The game needs simply to be run, and the main menu screen will be presented to the user. Button selection is done using a single mouse click, while fruits are slashed using a click-drag motion.