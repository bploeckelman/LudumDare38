package lando.systems.ld38.turns;

/**
 * Created by Brian on 4/22/2017
 */
public class ActionTypeMove extends ActionType {

    public int toCol;
    public int toRow;

    public ActionTypeMove(TurnAction turnAction, int toCol, int toRow) {
        super(turnAction);
        this.toCol = toCol;
        this.toRow = toRow;
    }

    @Override
    public void doAction() {
        turnAction.player.moveTo(toRow, toCol);
    }

}
