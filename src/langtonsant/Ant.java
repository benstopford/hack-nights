package langtonsant;

public class Ant {
    public static void main(String[] args) throws InterruptedException {
        new Ant();
    }

    int iteration = 0;
    int printRadius = 50;


    boolean black = true;
    boolean white = false;

    int north = 0, east = 1, south = 2, west = 3;
    int[] right = new int[]{east, south, west, north};
    int[] left = new int[]{west, north, east, south};
    char[] ant = new char[]{'^', '>', 'V', '<'};


    public Ant() throws InterruptedException {

        boolean[][] space = new boolean[1000][1000];
        int x = space.length / 2, y = space[0].length / 2;
        int direction = 0;

        while (true) {
            if (space[x][y] == white)
                direction = left[direction];
            else
                direction = right[direction];

            space[x][y] = !space[x][y];

            if (direction == north)
                y++;

            if (direction == east)
                x++;

            if (direction == south)
                y--;

            if (direction == west)
                x--;

            printSpaceCenteredOnAnt(space, x, y, direction);
        }

    }

    private void printSpaceCenteredOnAnt(boolean[][] space, int posX, int posY, int dir) throws InterruptedException {
        Thread.sleep(1);
        if (iteration++ % 100 == 0) {
            System.out.println("--------------------");
            String out = "";
            for (int y = (posY + printRadius / 2) - 1; y >= (posY - printRadius / 2); y--) {
                for (int x = (posX - printRadius / 2); x < (posX + printRadius / 2); x++) {
                    if (x == posX & y == posY) {
                        out += ant[dir];
                        continue;
                    }
                    if (space[x][y])
                        out += 'O';
                    else
                        out += '_';
                }
                out += "\n";
            }
            System.out.printf(out);
            System.out.println("iterations:"+iteration);
        }
    }
}