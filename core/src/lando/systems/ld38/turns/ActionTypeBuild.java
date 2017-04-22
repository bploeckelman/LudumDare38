package lando.systems.ld38.turns;

/**
 * Created by Brian on 4/22/2017
 */
public class ActionTypeBuild extends ActionType {

    BuildType buildType;

    public ActionTypeBuild(TurnAction turnAction, BuildType buildType) {
        super(turnAction);
        this.buildType = buildType;
    }

    @Override
    public void doAction() {
        // ...
    }

}
