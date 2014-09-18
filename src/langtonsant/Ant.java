package langtonsant;

public class Ant {
    public static void main(String[] args) throws InterruptedException {
        new Ant();
    }

    boolean black = true;
    boolean white = false;

    int north = 0, east = 1, south = 2, west = 3;
    int[] right = new int[]{east, south, west, north};
    int[] left = new int[]{west, north, east, south};
    char[] ant = new char[]{'^','>','V','<'};


    public Ant() throws InterruptedException {

        boolean[][] board = new boolean[20][20];
        int x = 10, y = 10;
        int direction = 0;

        while (true) {
            if (board[x][y] == white)
                direction = left[direction];
            else
                direction = right[direction];

            board[x][y] = !board[x][y];

            if (direction == north)
                y++;

            if (direction == east)
                x++;

            if (direction == south)
                y--;

            if (direction == west)
                x--;

            System.out.println(board[x][y]);
            printBoard(board, x, y, direction);
            Thread.sleep(100);
        }

    }

    private void printBoard(boolean[][] board, int posX, int posY, int dir) {
        System.out.println("--------------------");
        System.out.printf("x:%s,y:%s,dir:%s\n", posX, posY, dir);
        String out = "";
        for (int y = board.length-1; y >= 0; y--) {
            for (int x = 0; x < board[y].length; x++) {
                if(x==posX&y==posY) {out+=ant[dir]; continue;}
                if (board[x][y])
                    out += "X";
                else
                    out += "~";
            }
            out += "\n";
        }
        System.out.printf(out);
    }
}