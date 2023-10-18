public class BotFight {
    public static void main(String[] args) {
        int turns = 28;
        boolean showMoves = true;       // USE THIS TO CHANGE WHETHER MOVES IS PRINTED OR NOT

        // choose whether bot 1 goes first or not
        boolean bot1Move = true;        // USE THIS TO CHANGE WHICH BOT GOES FIRST

        char bot1Marker = bot1Move ? State.X_MARKER : State.O_MARKER;
        char bot2Marker = bot1Move ? State.O_MARKER : State.X_MARKER;

        Bot bot1 = new LocalSearchBot(bot1Marker);
        Bot bot2 = new MinimaxBot(bot2Marker);

        String bot1Name = bot1.getClass().getName();
        String bot2Name = bot2.getClass().getName();

        if(turns < 2 || turns > 28) {
            throw new Error();
        }

        turns *= 2;

        State state = State.initialState(turns, true);

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
            System.out.println(bot1Win == bot1Move ? bot1Name : bot2Name);
        }
        System.out.println("Score : " + score[0] + " - " + score[1]);
        state.printState();

    }
}
