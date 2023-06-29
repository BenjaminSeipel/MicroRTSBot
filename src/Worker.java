import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Random;
import java.util.Iterator;
import java.util.List;

public class Worker extends BaseUnit {

    public Worker(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    public UnitAction getNextUnitAction() {
        Iterator iter = this.actions.iterator();
        UnitAction action = null;

        while (iter.hasNext()) {
            action = (UnitAction) iter.next();
            int[] positionOfBase = getUnitPosition(UNIT_BASE);
            int amountOfBarracks = this.getAmountOfBarracks();

            if (this.isUnitInRessurceZone()) {
                Unit closestRessource = this.getClosestRessource();
                if (!this.getHarvest().isEmpty()) {
                    return this.getHarvest().get(0);
                } else if (this.isOnTheWayBackToBase()) {
                    if (this.player == 0) {
                        if (unit.getX() < positionOfBase[0] && !this.getMoveRight().isEmpty()) {
                            return this.getMoveRight().get(0);
                        } else if (unit.getY() < positionOfBase[1] && !this.getMoveDown().isEmpty()) {
                            return this.getMoveDown().get(0);
                        } else if (!this.getReturn().isEmpty()) {
                            return this.getReturn().get(0);
                        }
                    } else {
                        if (unit.getX() > positionOfBase[0] && !this.getMoveLeft().isEmpty()) {
                            return this.getMoveLeft().get(0);
                        } else if (unit.getY() > positionOfBase[1] && !this.getMoveUp().isEmpty()) {
                            return this.getMoveUp().get(0);
                        } else if (!this.getReturn().isEmpty()) {
                            return this.getReturn().get(0);
                        }
                    }
                } else {
                    if (this.player == 0) {
                        if (unit.getX() > closestRessource.getX() && !this.getMoveLeft().isEmpty()) {
                            return this.getMoveLeft().get(0);
                        } else if (unit.getY() > closestRessource.getY() && !this.getMoveUp().isEmpty()) {
                            return this.getMoveUp().get(0);
                        }
                    } else {
                        if (unit.getX() > closestRessource.getX() && !this.getMoveLeft().isEmpty()) {
                            return this.getMoveLeft().get(0);
                        } else if (unit.getY() > closestRessource.getY() && !this.getMoveUp().isEmpty()) {
                            return this.getMoveUp().get(0);
                        } else if (unit.getX() < closestRessource.getX() && !this.getMoveRight().isEmpty()) {
                            return this.getMoveRight().get(0);
                        } else if (unit.getY() < closestRessource.getY() && !this.getMoveDown().isEmpty()) {
                            return this.getMoveDown().get(0);
                        }
                    }
                }
            } else if (amountOfBarracks < this.MAX_AMOUNT_OF_BARRACK) {
                if (!(this.getProduce(UNIT_BARRACK).isEmpty())) {
                    return this.getProduce(UNIT_BARRACK).get(0);
                }
            } else {
                int[] positionEnemy = getEnemyUnitPosition(UNIT_BASE);
                if (positionEnemy == null) {
                    positionEnemy = getEnemyUnitPosition(UNIT_WORKER);
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

            }

            if (!(this.waitAction().isEmpty())) {
                return this.waitAction().get(0);
            }
        }


        return null;
    }

    public int getAmountOfBarracks() {
        List<Unit> allUnitsInGame = this.pgs.getUnits();
        Iterator iter = allUnitsInGame.iterator();
        int counter = 0;
        Unit unit;
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getPlayer() == this.player && unit.getType().name == UNIT_BARRACK) {
                counter++;
            }
        }
        return counter;
    }

    public boolean isUnitInRessurceZone() {
        int[] positionOfBase = this.getUnitPosition(UNIT_BASE);
        Unit closestResource = this.getClosestRessource();

        int XBorderLeft = (positionOfBase[0] > closestResource.getX()) ? closestResource.getX() : positionOfBase[0];
        int XBorderRight = (positionOfBase[0] < closestResource.getX()) ? closestResource.getX() : positionOfBase[0];
        int YBorderTop = (positionOfBase[1] > closestResource.getY()) ? closestResource.getY() : positionOfBase[1];
        int YBorderBottom = (positionOfBase[1] < closestResource.getY()) ? closestResource.getY() : positionOfBase[1];
        if (this.unit.getX() >= XBorderLeft && this.unit.getX() <= XBorderRight
                && this.unit.getY() >= YBorderTop && this.unit.getY() <= YBorderBottom) {
            return true;
        }
        // Just for one player so far !!!!!!!!!!!
        return false;
    }

    public Unit getClosestRessource() {
        int[] positionOfBase = this.getUnitPosition(UNIT_BASE);
        Unit unit = null;
        int diffX = this.pgs.getWidth();
        int diffY = this.pgs.getHeight();

        List<Unit> allUnits = this.pgs.getUnits();
        Iterator iter = allUnits.iterator();

        while (iter.hasNext()) {
            Unit u = (Unit) iter.next();
            if (u.getType().name == UNIT_RESOURCE) {
                int newX = positionOfBase[0] - u.getX();
                int newY = positionOfBase[1] - u.getY();
                if (newX < 0) {
                    newX *= -1;
                }
                if (newY < 0) {
                    newY *= -1;
                }
                if (unit == null) {
                    unit = u;
                    diffX = newX;
                    diffY = newY;
                } else {
                    if (newX <= diffX || newY <= diffY) {
                        unit = u;
                        diffX = newX;
                        diffY = newY;
                    }
                }
            }
        }

        return unit;
    }

    public List<UnitAction> getHarvest() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_HARVEST))
                .toList());
    }

    public List<UnitAction> getReturn() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_RETURN))
                .toList());
    }

    public boolean isOnTheWayBackToBase() {
        return this.unit.getResources() == 1;
    }


}
