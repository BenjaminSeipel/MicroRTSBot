import rts.PhysicalGameState;
import rts.UnitAction;
import rts.units.Unit;

import java.util.Random;
import java.util.Iterator;
import java.util.List;

public class Worker extends BaseForUnits {

    public Worker(Unit unit, List<UnitAction> actions, PhysicalGameState pgs, int player) {
        super(unit, actions, pgs, player);
    }

    public UnitAction getNextUnitAction() {
        Iterator iter = this.actions.iterator();
        UnitAction action = null;

        while (iter.hasNext()) {
            action = (UnitAction) iter.next();
            int[] positionOfBase = getUnitPosition("Base");
            int amountOfBarracks = this.getAmountOfBarracks();

            if (this.isUnitInRessurceZone()) {
                Unit closestRessource = this.getClosestRessource();
                if (!this.getHarvest().isEmpty()) {
                    return this.getHarvest().get(0);
                } else if (this.isOnTheWayBackToBase()) {
                    if (this.player == 0) {
                        if (unit.getX() < positionOfBase[0] && !this.getMoveRigth().isEmpty()) {
                            return this.getMoveRigth().get(0);
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
                        } else if (unit.getX() < closestRessource.getX() && !this.getMoveRigth().isEmpty()) {
                            return this.getMoveRigth().get(0);
                        } else if (unit.getY() < closestRessource.getY() && !this.getMoveDown().isEmpty()) {
                            return this.getMoveDown().get(0);
                        }
                    }
                }
            } else if (amountOfBarracks < this.MAX_AMOUNT_OF_BARRACK) {
                if (!(this.buildBarrack().isEmpty())) {
                    return this.buildBarrack().get(0);
                }
            } else {
                int[] positionEnemy = getEnemyBasePosition();
                if (positionEnemy == null) {
                    positionEnemy = getEnemyUnitPosition("Worker");
                }
                Random rand = new Random();
                int takeX = rand.nextInt(2);
                int enemyX = positionEnemy[0];
                int enemyY = positionEnemy[1];

                if (!this.getAttack().isEmpty()) {
                    return this.getAttack().get(0);
                } else if (takeX == 1) {
                    if (this.unit.getX() < enemyX && !this.getMoveRigth().isEmpty()) {
                        return this.getMoveRigth().get(0);
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
            if (unit.getPlayer() == this.player && unit.getType().name == "Barracks") {
                counter++;
            }
        }
        return counter;
    }

    public boolean isUnitInRessurceZone() {
        int[] positionOfBase = this.getUnitPosition("Base");
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

    public int[] getEnemyBasePosition() {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == "Base" && unit.getPlayer() != this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            // need new base, old one is destroyed(if possible)
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }

    public int[] getEnemyUnitPosition(String unitName) {
        List<Unit> units = this.pgs.getUnits();
        Unit Base = null;
        Unit unit = null;
        Iterator iter = units.iterator();
        while (iter.hasNext()) {
            unit = (Unit) iter.next();
            if (unit.getType().name == unitName && unit.getPlayer() != this.player) {
                Base = unit;
                break;
            }
        }

        if (Base == null) {
            return null;
        }


        int[] position = new int[2];
        position[0] = Base.getX();
        position[1] = Base.getY();
        return position;
    }

    public Unit getClosestRessource() {
        int[] positionOfBase = this.getUnitPosition("Base");
        Unit unit = null;
        int diffX = this.pgs.getWidth();
        int diffY = this.pgs.getHeight();

        List<Unit> allUnits = this.pgs.getUnits();
        Iterator iter = allUnits.iterator();

        while (iter.hasNext()) {
            Unit u = (Unit) iter.next();
            if (u.getType().name == "Resource") {
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

    public List<UnitAction> getAttack() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_ATTACK_LOCATION))
                .toList());
    }

    public List<UnitAction> buildBarrack() {
        return (this.actions.stream()
                .filter(action -> (action.getType() == UnitAction.TYPE_PRODUCE && action.getUnitType().name == "Barracks"))
                .toList());
    }

    public boolean isOnTheWayBackToBase() {
        return this.unit.getResources() == 1;
    }


}
