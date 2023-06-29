import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Iterator;
import java.util.List;

public class Light extends BaseUnit {

    public Light(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
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
            } else if (this.unit.getX() < enemyX && !this.getMoveRight().isEmpty()) {
                return this.getMoveRight().get(0);
            } else if (this.unit.getX() > enemyX && !this.getMoveLeft().isEmpty()) {
                return this.getMoveLeft().get(0);
            } else if (this.unit.getY() < enemyY && !this.getMoveDown().isEmpty()) {
                return this.getMoveDown().get(0);
            } else if (this.unit.getY() > enemyY && !this.getMoveUp().isEmpty()) {
                return this.getMoveUp().get(0);
            } else if (!(this.waitAction().isEmpty())) {
                return this.waitAction().get(0);
            }
        }
        return new UnitAction(UnitAction.TYPE_NONE);
    }

    public UnitAction getNextAggressiveLightUnitAction() {
        Iterator<UnitAction> iter = this.actions.iterator();
        UnitAction action = null;

        while (iter.hasNext()) {
            action = iter.next();

            // Check if there are any enemies within attack range
            if (isEnemyWithinAttackRange()) {
                // Attack the enemy
                if (!this.getAttack().isEmpty()) {
                    return this.getAttack().get(0);
                }
            } else {
                // Move towards the nearest enemy
                Unit nearestEnemy = getNearestEnemy();
                if (nearestEnemy != null) {
                    int enemyX = nearestEnemy.getX();
                    int enemyY = nearestEnemy.getY();

                    if (this.unit.getX() < enemyX && !this.getMoveRight().isEmpty()) {
                        return this.getMoveRight().get(0);
                    } else if (this.unit.getX() > enemyX && !this.getMoveLeft().isEmpty()) {
                        return this.getMoveLeft().get(0);
                    } else if (this.unit.getY() < enemyY && !this.getMoveDown().isEmpty()) {
                        return this.getMoveDown().get(0);
                    } else if (this.unit.getY() > enemyY && !this.getMoveUp().isEmpty()) {
                        return this.getMoveUp().get(0);
                    }
                }
            }

            if (!this.waitAction().isEmpty()) {
                return this.waitAction().get(0);
            }
        }

        return new UnitAction(UnitAction.TYPE_NONE);
    }

    private boolean isEnemyWithinAttackRange() {
        List<Unit> units = this.pgs.getUnits();
        for (Unit unit : units) {
            if (unit.getPlayer() != this.player && getDistance(unit) <= this.unit.getAttackRange()) {
                return true;
            }
        }
        return false;
    }

    private Unit getNearestEnemy() {
        List<Unit> units = this.pgs.getUnits();
        Unit nearestEnemy = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Unit unit : units) {
            if (unit.getPlayer() != this.player) {
                double distance = getDistance(unit);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestEnemy = unit;
                }
            }
        }

        return nearestEnemy;
    }

    private double getDistance(Unit enemy) {
        return Math.sqrt(Math.pow(this.unit.getX() - enemy.getX(), 2) + Math.pow(this.unit.getY() - enemy.getY(), 2));
    }

}

