/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import NapakalakiGame.Treasure;

/**
 *
 * @author Ocete
 */
public class TreasureView extends javax.swing.JPanel {

    private Treasure treasureModel;
    private boolean selected = false;
    
    public TreasureView() {
        initComponents();
    }
    
    public boolean isSelected () {
        return selected;
    }
    
    public Treasure getTreasure() {
        return treasureModel;
    }
    
    public void setTreasure(Treasure t) {
        treasureModel = t;
        this.name.setText("Nombre: " + treasureModel.getName());
        this.bonus.setText("Bonus: " + 
                Integer.toString(treasureModel.getBonus()));
        String s = "Tipo: ";
        switch (treasureModel.getType()) {
            case BOTHHANDS: 
                s += "BOTHHANDS";
                break;
            case ONEHAND: 
                s += "ONEHAND";
                break;
            case SHOE: 
                s += "SHOE";
                break;
            case HELMET: 
                s += "HELMET";
                break;
            default:
                s += "ARMOR";
        }
        this.treasureKind.setText(s);
        setBackground(Color.ORANGE);
        setOpaque(false);
        repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        name = new javax.swing.JLabel();
        bonus = new javax.swing.JLabel();
        treasureKind = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        name.setText("Name");

        bonus.setText("Bonus");

        treasureKind.setText("Type");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bonus)
                        .addGap(29, 29, 29)
                        .addComponent(treasureKind)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(name)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(treasureKind)
                    .addComponent(bonus, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        selected = !selected;
        setOpaque(selected);
        repaint();
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bonus;
    private javax.swing.JLabel name;
    private javax.swing.JLabel treasureKind;
    // End of variables declaration//GEN-END:variables
}
