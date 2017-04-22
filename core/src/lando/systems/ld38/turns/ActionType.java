package lando.systems.ld38.turns;

/**
 * Created by Brian on 4/22/2017
 */
public abstract class ActionType {

    TurnAction turnAction;

    public ActionType(TurnAction turnAction) {
        this.turnAction = turnAction;
    }

    public abstract void doAction();

}
