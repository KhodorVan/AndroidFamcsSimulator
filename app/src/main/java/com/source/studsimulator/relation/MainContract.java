package com.source.studsimulator.relation;


import com.source.studsimulator.model.Food;
import com.source.studsimulator.model.GameLogic;
import com.source.studsimulator.model.Price;

public interface MainContract {
    public interface View {
        void refreshTextInformation();
        void refreshGradientInformation();
    }

    public interface Presenter {
        void clickOnEatButton();
        void clickOnLearnButton();
        void clickOnSleepButton();
        void clickOnWorkButton();
        int getParameter(GameLogic.ECharacteristics characteristic);
    }

    public interface Model {
        void eat(Food food);
        void pay(Price price);
        // как и с едой, добавить класс для методов обучения
        // и передавать его далее
        void learn();
        void sleep();
        // аналогично тут
        void work();
        int getParameter(GameLogic.ECharacteristics characteristic);
    }
}