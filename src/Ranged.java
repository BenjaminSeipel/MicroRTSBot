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
        Iterator<UnitAction> iter = this.actions.iterator();
        UnitAction action = null;

        while (iter.hasNext()) {
            action = iter.next();
            int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
            if (positionEnemy == null) {
                positionEnemy = getEnemyUnitPosition(UNIT_WORKER);
            }

            int enemyX = positionEnemy[0];
            int enemyY = positionEnemy[1];

            if (!this.getAttack().isEmpty()) {
                return this.getAttack().get(0);
            } else {
                moveToPosition(enemyX, enemyY);
            }

        }

        return new UnitAction(UnitAction.TYPE_NONE);
    }

}
