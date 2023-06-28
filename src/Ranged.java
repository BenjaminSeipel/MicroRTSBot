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
            int[] positionEnemy = getEnemyBasePosition();
            if (positionEnemy == null) {
                positionEnemy = getEnemyWorkerPosition();
            }
            Random rand = new Random();
            int takeX = rand.nextInt(2);
            int enemyX = positionEnemy[0];
            int enemyY = positionEnemy[1];

            if (!this.getAttack().isEmpty()) {
                return this.getAttack().get(0);
            } else if (takeX == 1) {
                if (this.unit.getX() < enemyX && !this.getMoveRight().isEmpty()) {
                    return this.getMoveRight().get(0);
                } else if (this.unit.getX() > enemyX && !this.getMoveLeft().isEmpty()) {
                    return this.getMoveLeft().get(0);
                }
            }

            if (this.unit.getY() < enemyY && !this.getMoveDown().isEmpty()) {
                return this.getMoveDown().get(0);
            } else if (this.unit.getY() > enemyY && !this.getMoveUp().isEmpty()) {
                return this.getMoveUp().get(0);
            }

            if (!(this.waitAction().isEmpty())) {
                return this.waitAction().get(0);
            }
        }

        return new UnitAction(UnitAction.TYPE_NONE);
    }

}
