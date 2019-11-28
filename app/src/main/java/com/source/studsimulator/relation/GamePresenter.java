package com.source.studsimulator.relation;

import com.source.studsimulator.model.GameLogic.PlayerStatsEnum;
import com.source.studsimulator.model.entity.Food;
import com.source.studsimulator.model.entity.Hobby;
import com.source.studsimulator.model.entity.Study;
import com.source.studsimulator.model.entity.Work;
import com.source.studsimulator.ui.entity.PlayerStats;

public class GamePresenter implements GameContract.Presenter {

    private GameContract.Model model;
    private GameContract.View view;

    public GamePresenter(GameContract.View newView, GameContract.Model newModel) {
        view = newView;
        model = newModel;
    }

    @Override
    public void viewCreated() {
        updatePlayerStats();
        view.updateWeek(model.getWeek());
        view.updateEnergyLevel(model.getEnergyLevel());
    }

    @Override
    public void clickOnNewWeekButton() {
        model.newWeek();
        updatePlayerStats();
        view.updateWeek(model.getWeek());
        view.updateEnergyLevel(model.getEnergyLevel());
    }

    @Override
    public void clickOnFoodButton(Food food) {
        model.eat(food);
    }

    @Override
    public void clickOnLearnButton(Study study) {
        model.learn(study);
    }

    @Override
    public void clickOnWorkButton(Work work) {
        model.work(work);
    }

    @Override
    public void clickOnHobbyButton(Hobby hobby) {
        //
    }

    private void changeEnergyLevel() {
        view.updateEnergyLevel(model.getEnergyLevel());
    }

    private int getParameter(PlayerStatsEnum characteristic) {
        return model.getParameter(characteristic);
    }

    private void updatePlayerStats() {
        PlayerStats playerStats = new
                PlayerStats(
                model.getParameter(PlayerStatsEnum.EDUCATION_LEVEL),
                model.getParameter(PlayerStatsEnum.HEALTH),
                model.getParameter(PlayerStatsEnum.SATIETY),
                String.valueOf(model.getParameter(PlayerStatsEnum.MONEY)),
                getProgrammingSkillString(model.getParameter(PlayerStatsEnum.PROGRAMMING_SKILL)),
                getEnglishSkillString(model.getParameter(PlayerStatsEnum.ENGLISH_SKILL)));
        view.refreshPlayerStats(playerStats);
    }

    private String getProgrammingSkillString(int param) {
        if (param < 20) {
            return "Begginer";
        } else if (param < 40) {
            return "Lubitel";
        } else if (param < 80) {
            return "Junior";
        } else if (param < 140) {
            return "Middle";
        } else if (param < 220) {
            return "Senior";
        } else if (param < 300) {
            return "Lead";
        } else {
            return "Korotkevich";
        }
    }

    private String getEnglishSkillString(int param) {
        if (param < 20) {
            return "A1";
        } else if (param < 40) {
            return "A2";
        } else if (param < 60) {
            return "B1";
        } else if (param < 80) {
            return "B2";
        } else if (param < 100) {
            return "C1";
        } else if (param < 120) {
            return "C2";
        } else {
            return "D13";
        }
    }
}
