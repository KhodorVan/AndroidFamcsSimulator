package com.source.studsimulator.model;


import com.source.studsimulator.relation.MainContract;

public class GameLogic implements MainContract.Model {
    private Student student;
    private double dollarsCost = 2;

    public enum ECharacteristics {
        EDUCATION_LEVEL, HEALTH, SATIETY, MONEY, DOLLARS
    }

    public GameLogic() {
        student = new Student();
    }

    // тут и далее показаны примерный план работы каждого метода
    // по красоте далее нужно будет сделать енамы в controller(а может даже во вью)
    // и передавать их сюда



    @Override
    public void work() {
        student.substractCharacteristic(5, ECharacteristics.HEALTH);
        student.addMoney(15);
    }

    // обсудим, нужно ли нам это
    @Override
    public void sleep() {
        student.addCharacteristic(50, ECharacteristics.HEALTH);
        student.substractCharacteristic(20, ECharacteristics.SATIETY);
    }

    @Override
    public void eat(Food food) {
        student.addCharacteristic(food.getSatiety(), ECharacteristics.SATIETY);
        student.addCharacteristic(10, ECharacteristics.HEALTH);
        pay(food);
    }

    @Override
    public void pay(Price price) {
        student.substractMoney(price.getPrice());
    }

    @Override
    public void learn() {
        student.addCharacteristic(15, ECharacteristics.EDUCATION_LEVEL);
        student.substractCharacteristic(5, ECharacteristics.HEALTH);
        student.substractCharacteristic(5, ECharacteristics.SATIETY);
    }

    @Override
    public int getParameter(GameLogic.ECharacteristics characteristic){
        return student.getParameter(characteristic);
    }
}