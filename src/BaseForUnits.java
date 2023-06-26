import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.List;

public class BaseForUnits {
    int MAXIMUM_WORKERS = 5;
    int MAXIMUM_WORKES_IN_RESSOURCE_ZONE = 2;
    int MAX_AMOUNT_OF_BARRACK = 1;
    Unit unit;
    List<UnitAction> actions;
    PhysicalGameState pgs;
    int player;

    public BaseForUnits(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        this.pgs = pgs;
        this.unit = unit;
        this.actions = actions;
        this.player = player;
    }


}
