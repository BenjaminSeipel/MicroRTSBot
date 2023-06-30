import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Ranged extends BaseUnit {
    public Ranged(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    public UnitAction getNextUnitAction() {
        return attackEnemy();
    }

}
