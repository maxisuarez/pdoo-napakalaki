// begin Player.java
package napakalaki;

import GUI.Dice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// Representación de un jugador
public class Player {
    
    static final int MAXLEVEL = 10;
    private String name;
    private int level;
    private boolean dead = true;
    private boolean canISteal = true;
    private Player enemy;
    private BadConsequence pendingBadConsequence;
    private ArrayList<Treasure> visibleTreasures = new ArrayList();
    private ArrayList<Treasure> hiddenTreasures = new ArrayList();

    public Player(String name) {
        this.name = name;
        this.level = 1;
    }
    
    public Player (Player p) {
        this.name = p.name;
        this.level = p.level;
        this.dead = p.dead;
        this.canISteal = p.canISteal;
        this.enemy = p.enemy;
        this.pendingBadConsequence = p.pendingBadConsequence;
        this.visibleTreasures = p.visibleTreasures;
        this.hiddenTreasures = p.hiddenTreasures;
    }
    
    public String getName() {
        return name;
    }
    
    private void bringToLife() {
        dead = false;
    }
    
    protected int getCombatLevel() {
        int bonus_total = level;
        for (Treasure treasure: visibleTreasures)
            bonus_total += treasure.getBonus();
        return bonus_total;
    }
    
    private void incrementLevels(int l) {
        level += l;
        if (level > 10)
            level = 10;
    }
    
    private void decrementLevels(int l) {
        level -= l;
        if (level < 1)
            level = 1;
    }
    
    private void setPendingBadConsequence(BadConsequence b) {
        pendingBadConsequence = b;
    }
    
    private void applyPrize(Monster m) {
        incrementLevels(m.getLevelsGained());
        int nTreasures = m.getTreasuresGained();
        if (nTreasures > 0) {
            CardDealer dealer = CardDealer.getInstance();
            for (int i=1; i<=nTreasures; i+=1)
               hiddenTreasures.add(dealer.nextTreasure());
        }
    }
    
    private void applyBadConsequence(Monster m) {
        BadConsequence badConsequence = m.getBadConsequence();
        decrementLevels(badConsequence.getLevels());
        BadConsequence pendingBad = badConsequence.adjustToFitTreasureLists(
                visibleTreasures, hiddenTreasures);
        setPendingBadConsequence(pendingBad);
    }
    
    private boolean canMakeTreasureVisible(Treasure t) {
        boolean result = true;
        Treasure treasure;
        Iterator <Treasure> i = visibleTreasures.iterator();
        switch (t.getType()) {
            case BOTHHANDS:
                while (result && i.hasNext()) {
                    treasure = i.next();
                    if (treasure.getType() == TreasureKind.BOTHHANDS ||
                            treasure.getType() == TreasureKind.ONEHAND)
                        result = false;
                }
                break;
                
            case ONEHAND:
                int numOneHand = 0;
                while (result && i.hasNext()) {
                    treasure = i.next();
                    if (treasure.getType() == TreasureKind.BOTHHANDS)
                        result = false;
                    else if (treasure.getType() == TreasureKind.ONEHAND) {
                        numOneHand += 1;
                        if (numOneHand == 2)
                            result = false;
                    }
                }
                break;
                
            default:
                while (result && i.hasNext()) 
                    if (i.next().getType() == t.getType())
                        result = false;
        }
        return result;
    }
    
    private int howManyVisibleTreasures(TreasureKind tKind) {
        int n = 0;
        for (Treasure treasure : visibleTreasures)
            if (treasure.getType() == tKind)
                n++;
        return n;
    }
    
    private void dieIfNoTreasures() {
        if (visibleTreasures.isEmpty() && hiddenTreasures.isEmpty())
            dead = true;
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public ArrayList<Treasure> getVisibleTreasures() {
        return visibleTreasures;
    }
    
    public ArrayList<Treasure> getHiddenTreasures() {
        return hiddenTreasures;
    }
    
    public CombatResult combat(Monster m) {
        int myLevel = getCombatLevel();
        int monsterLevel = getOponentLevel(m);
        CombatResult cr;
        if (!canISteal) {
            if (Dice.getInstance().nextNumber() < 3) {
                monsterLevel += enemy.getCombatLevel();
            }
        }
        if (myLevel > monsterLevel) {
            applyPrize(m);
            if (level > MAXLEVEL)
                cr = CombatResult.WINGAME;
            else
                cr = CombatResult.WIN;
        } else {
            applyBadConsequence(m);
            if (shouldConvert())
                cr = CombatResult.LOSEANDCONVERT;
            else
                cr = CombatResult.LOSE;
        }
        return cr;
    }
    
    public void makeTreasureVisible(Treasure t) {
        if (canMakeTreasureVisible(t)) {
            visibleTreasures.add(t);
            hiddenTreasures.remove(t);
        }
    }
    
    public void discardVisibleTreasure(Treasure t) {
        visibleTreasures.remove(t);
        if (pendingBadConsequence != null &&
                !pendingBadConsequence.isEmpty())
            pendingBadConsequence.substractVisibleTreasure(t);
        dieIfNoTreasures();
    }
    
    public void discardHiddenTreasure(Treasure t) {
        hiddenTreasures.remove(t);
        if (pendingBadConsequence != null &&
                !pendingBadConsequence.isEmpty())
            pendingBadConsequence.substractHiddenTreasure(t);
        dieIfNoTreasures();
    }
    
    public boolean validState() {
       return pendingBadConsequence.isEmpty() && hiddenTreasures.size() <= 4;
    }
    
    public void initTreasures() {
        CardDealer dealer = CardDealer.getInstance();
        Dice dice = Dice.getInstance();
        bringToLife();
        Treasure treasure = dealer.nextTreasure();
        hiddenTreasures.add(treasure);
        int number = dice.nextNumber();
        if (number > 1) {
            treasure = dealer.nextTreasure();
            hiddenTreasures.add(treasure);
        }
        if (number == 6) {
            treasure = dealer.nextTreasure();
            hiddenTreasures.add(treasure);
        }
    }
    
    public int getLevels() {
        return level;
    }
    
    
    public Treasure stealTreasure() {
        Treasure treasure = null;
        if (canISteal() && enemy.canYouGiveMeATreasure()) {
            treasure = enemy.giveMeATreasure();
            hiddenTreasures.add(treasure);
            haveStolen();
        }
        return treasure;
    }
    
    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }
    
    protected Treasure giveMeATreasure() {
        Treasure t = hiddenTreasures.get(new Random().nextInt(
            hiddenTreasures.size()));
        hiddenTreasures.remove(t);
        return t;
    }
    
    public boolean canISteal() {
        return canISteal;
    }
    
    protected boolean canYouGiveMeATreasure() {
        return !hiddenTreasures.isEmpty();
    }
    
    private void haveStolen() {
        canISteal = false;
    }
    
    public void discardAllTreasures() {
        Iterator <Treasure> i = visibleTreasures.iterator();
        while (i.hasNext())
            discardVisibleTreasure(i.next());
        i = hiddenTreasures.iterator();
        while (i.hasNext())
            discardHiddenTreasure(i.next());
    }
    
    protected boolean shouldConvert() {
        boolean result = false;
        if (Dice.getInstance().nextNumber() == 6)
            result = true;
        return result;
    }
    
    protected int getOponentLevel(Monster m) {
        return m.getCombatLevel();
    }
    
    protected Player getEnemy() {
        return enemy;
    }
    
    // Métodos para la vista:
    public String getEnemyName() {
        return enemy.getName();
    }
    
    public BadConsequence getPendingBadConsequence() {
        return pendingBadConsequence;
    }
}

// end Player.java