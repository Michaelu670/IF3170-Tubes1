public class BotFight {
    public static void main(String[] args) {
        int turns = 28;
        boolean showMoves = true;

        // bot 1 : X, bot 2 : O
        Bot bot1 = new MinimaxBot();
        Bot bot2 = new LocalSearchBot();

        // choose whether bot 1 goes first or not
        boolean bot1Move = true;

        String bot1Name = bot1.getClass().getName();
        String bot2Name = bot2.getClass().getName();

        if(turns < 2 || turns > 28) {
            throw new Error();
        }

        State state = State.initialState(turns, true);

        turns *= 2;

        for (int i = 0; i < turns; i++) {
            int[] nextMove;
            try {
                nextMove = bot1Move ? bot1.move(state) : bot2.move(state);
                state = state.move(nextMove[0], nextMove[1]);
                
                if(showMoves) {
                    int[] score = state.getScore();
                    System.out.println("Turn " + (i + 1));
                    System.out.print(bot1Move ? bot1Name : bot2Name);
                    System.out.println(" move " + nextMove[0] + ", " + nextMove[1]);
                    System.out.println("Score : " + score[0] + " - " + score[1]);
                    state.printState();
                    System.out.println();
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            bot1Move = !bot1Move;
        }
        
        System.out.println("Game ends");
        int[] score = state.getScore();
        if(score[0] == score[1]) {
            System.out.println("Tied");
        } else {
            System.out.print("Winner : ");
            boolean bot1Win = score[0] > score[1];
            System.out.println(bot1Win ? bot1Name : bot2Name);
        }
        System.out.println("Score : " + score[0] + " - " + score[1]);
        state.printState();

    }
}
