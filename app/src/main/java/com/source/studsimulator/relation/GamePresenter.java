package com.source.studsimulator.relation;

import com.source.studsimulator.R;
import com.source.studsimulator.model.ActionObjects;
import com.source.studsimulator.model.GameLogic.PlayerStatsEnum;
import com.source.studsimulator.model.entity.ContainsRandomAction;
import com.source.studsimulator.model.entity.Food;
import com.source.studsimulator.model.entity.Hobby;
import com.source.studsimulator.model.entity.RandomAction;
import com.source.studsimulator.model.entity.Study;
import com.source.studsimulator.model.entity.Work;
import com.source.studsimulator.ui.StudSimulatorApplication;
import com.source.studsimulator.ui.entity.PlayerStats;
import com.source.studsimulator.ui.entity.ViewState;

public class GamePresenter implements GameContract.Presenter {

    public class PlayerInformation {
        private String course;
        private String studyStage;
        private String programmingSkill;
        private String englishSkill;

        public PlayerInformation(String course, String studyStage, String programmingSkill, String englishSkill) {
            this.course = course;
            this.studyStage = studyStage;
            this.programmingSkill = programmingSkill;
            this.englishSkill = englishSkill;
        }

        public String getCourse() {
            return course;
        }

        public String getStudyStage() {
            return studyStage;
        }

        public String getProgrammingSkill() {
            return programmingSkill;
        }

        public String getEnglishSkill() {
            return englishSkill;
        }
    }

    private GameContract.Model model;
    private GameContract.View view;
    private ViewState weekLiveChoicesStaff;

    public GamePresenter(GameContract.View newView, GameContract.Model newModel) {
        view = newView;
        model = newModel;
        weekLiveChoicesStaff = new ViewState();
    }

    @Override
    public void viewCreated() {
        updatePlayerStats();
        view.updateWeek(model.getWeek());
        view.updateEnergyLevel(model.getEnergyLevel());
    }

    @Override
    public void clickOnNewWeekButton(int energy) {
        view.cleanRandomActionsMessages();
        applyLiveChoices(weekLiveChoicesStaff);
        model.newWeek(energy);
        updatePlayerStats();
        view.updateWeek(model.getWeek());
        view.updateEnergyLevel(model.getEnergyLevel());
        view.updateWeekInformation(new PlayerInformation(
                String.valueOf(model.getWeek() / 52 + 1),
                getStudyStage(model.getWeek()),
                getProgrammingSkillString(model.getParameter(PlayerStatsEnum.PROGRAMMING_SKILL)),
                getEnglishSkillString(model.getParameter(PlayerStatsEnum.ENGLISH_SKILL))));
    }

    private void applyLiveChoices(ViewState weekLiveChoicesStaff) {
        for (Food foodItem : weekLiveChoicesStaff.getFoodList()) {
            model.eat(foodItem);
            applyRandomAction(foodItem);
        }

        for (Study studyItem : weekLiveChoicesStaff.getStudyList()) {
            model.study(studyItem);
            applyRandomAction(studyItem);
        }

        for (Work workItem : weekLiveChoicesStaff.getWorkList()) {
            model.work(workItem);
            applyRandomAction(workItem);
        }

        for (Hobby hobbyItem : weekLiveChoicesStaff.getHobbyList()) {
            model.hobby(hobbyItem);
            applyRandomAction(hobbyItem);
        }

        model.normalizeCharacteristics();
      
        if (model.getParameter(PlayerStatsEnum.SATIETY) == 0 ||
                model.getParameter(PlayerStatsEnum.HEALTH) == 0) {
            view.printDeadMessage();
        }

        // birthday
        if (model.getWeek() % 52 == 1) {
            applyRandomAction(ActionObjects.getAction(6));
        }
        // money from parents
        applyRandomAction(ActionObjects.getAction(3));
    }

    @Override
    public void clickOnFoodButton(Food food) {
        weekLiveChoicesStaff.addFood(food);
        model.changeEnergyLevel(-food.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void unclickOnFoodButton(Food food) {
        weekLiveChoicesStaff.removeFood(food);
        model.changeEnergyLevel(food.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void clickOnStudyButton(Study study) {
        weekLiveChoicesStaff.addStudy(study);
        model.changeEnergyLevel(-study.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void unclickOnStudyButton(Study study) {
        weekLiveChoicesStaff.removeStudy(study);
        model.changeEnergyLevel(study.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void activateWorkButton(Work work) {
        weekLiveChoicesStaff.addWork(work);
        model.changeEnergyLevel(-work.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void clickOnWorkButton( int number, Work.TYPE_OF_WORK type) {
        Work work = (Work) ActionObjects.getWorkList().get(0);;
        switch (type){
            case SUMMER:
                work = (Work) ActionObjects.getSummerWorkList().get(number);
                break;
            case SIDE:
            work = (Work) ActionObjects.getSideJobList().get(number);
                break;
            case FULL_TIME:
            work = (Work) ActionObjects.getWorkList().get(number);
                break;
        }
        if (work.getProgrammingSkillRequired() > model.getParameter(PlayerStatsEnum.PROGRAMMING_SKILL)){
            view.notAvailableMessage("Не достаточный навык программирования");
            System.out.println("прога");
            return;
        }
        if (work.getEnglishSkillRequired() > model.getParameter(PlayerStatsEnum.ENGLISH_SKILL)){
            view.notAvailableMessage("Не достаточный навык английского");
            System.out.println("англ");
            return;
        }
        if (work.getEnergyNeeded() > model.getEnergyLevel()){
            view.notAvailableMessage("Не достаточно энергии");
            System.out.println("энергия");
            return;
        }
        System.out.println("кнопка" + number + "ща запустится");
        weekLiveChoicesStaff.addWork(work);
        model.changeEnergyLevel(-work.getEnergyNeeded());
        changeEnergyLevel();
        view.activateStudyButton(number, type);
    }

    @Override
    public void deactivateWorkButton(Work work) {
        weekLiveChoicesStaff.removeWork(work);
        model.changeEnergyLevel(work.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void clickOnHobbyButton(Hobby hobby) {
        weekLiveChoicesStaff.addHobby(hobby);
        model.changeEnergyLevel(-hobby.getEnergyNeeded());
        changeEnergyLevel();
    }

    @Override
    public void unclickOnHobbyButton(Hobby hobby) {
        weekLiveChoicesStaff.removeHobby(hobby);
        model.changeEnergyLevel(hobby.getEnergyNeeded());
        changeEnergyLevel();
    }

    private void changeEnergyLevel() {
        view.updateEnergyLevel(model.getEnergyLevel());
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
            return StudSimulatorApplication.getContext().getString(R.string.begginer_prog);
        } else if (param < 40) {
            return StudSimulatorApplication.getContext().getString(R.string.lub_prog);
        } else if (param < 80) {
            return StudSimulatorApplication.getContext().getString(R.string.jun_prog);
        } else if (param < 140) {
            return StudSimulatorApplication.getContext().getString(R.string.mid_prog);
        } else if (param < 220) {
            return StudSimulatorApplication.getContext().getString(R.string.senior_prog);
        } else if (param < 300) {
            return StudSimulatorApplication.getContext().getString(R.string.lead_prog);
        } else {
            return StudSimulatorApplication.getContext().getString(R.string.gena_prog);
        }
    }

    private String getEnglishSkillString(int param) {
        if (param < 20) {
            return StudSimulatorApplication.getContext().getString(R.string.a1);
        } else if (param < 40) {
            return StudSimulatorApplication.getContext().getString(R.string.a2);
        } else if (param < 60) {
            return StudSimulatorApplication.getContext().getString(R.string.b1);
        } else if (param < 80) {
            return StudSimulatorApplication.getContext().getString(R.string.b2);
        } else if (param < 100) {
            return StudSimulatorApplication.getContext().getString(R.string.c1);
        } else if (param < 120) {
            return StudSimulatorApplication.getContext().getString(R.string.c2);
        } else {
            return StudSimulatorApplication.getContext().getString(R.string.d13);
        }
    }

    private String getStudyStage(int week) {
        week %= 52 + 1;
        if (week <= 16 || (week > 22 && week <= 36)) {
            return StudSimulatorApplication.getContext().getString(R.string.semestr);
        } else if ((week > 16 && week <= 20) || (week > 36 && week <= 40)) {
            return StudSimulatorApplication.getContext().getString(R.string.session);
        } else {
            return StudSimulatorApplication.getContext().getString(R.string.holidays);
        }
    }

    private void applyRandomAction(ContainsRandomAction item) {
        if (item.getRandomAction() != null) {
            if (item.getRandomAction().isActive()) {
                model.applyRandomAction(item.getRandomAction());
                view.writeRandomActionMessage(item.getRandomAction().getMessage());
            }
        }
    }

    private void applyRandomAction(RandomAction action) {
        if (action != null) {
            if (action.isActive()) {
                model.applyRandomAction(action);
                view.writeRandomActionMessage(action.getMessage());
            }
        }
    }
}
