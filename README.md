## LOGIC / DESIGN

This game is designed to demonstrate an A* pathfinding algorithm. To run the game, simply execute the
included .jar file.
The main screen is an editor for the level, which allows the user to set the start, and end positions
using right click, and change each tile's type by cycling through the types with left click. 
The toolbar includes an option to reset all the tiles back to being open.
Once started, the simulation screen is shown, which allows the user to either autoplay at a specific
rate, or increment through the algorithm step-by-step.
The magenta tile is the tile that is currently being examined by the algorithm, red tiles are those that 
are currently in the open list, and when a candidate path is found, it is denoted by a yellow line.

The heuristic used is a custom algorithm, which interpolates between the manhattan distance, and the
worst possible distance between the current node, and the end node. The worst possible distance is
calculated through an algorithm I devised, based on a zig-zagging path, that is still passable. 
Based on the total number of obstacle nodes that are present, the heuristic is weighted more towards
the worst case. The less obstacles there are, the more the heuristic behaves as a simple manhattan distance.
In either case, the heuristic "distance" value is then multiplied by the mean average cost of all 
the nodes on the board.


## HOW TO COMPILE / RUN

To run game, simply execute the .jar file from the releases page.

To compile game from source into .jar file, ensure you have java jdk installed, 
then open up your command-line interface.
navigate to the "source" folder included in this zip file, and run the following 2 commands:


javac *.java

jar cvfm game.jar MANIFEST.MF *.class util/*.class


the .jar file may then be executed from the command line by: java -jar game.jar

## BUGS

In certain cases, the pathfinder may need to recalculate the same areas repeatedly, due to it finding a 
shorter possible path to an area that has lots of already-computed nodes. Though it doesn't 
appear to ever get caught in a loop in my testing, this often leads to further optimization of an 
already-found path slower.
