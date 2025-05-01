package com.ieuapp;

public class DataExchange {
    private static DataExchange instance;
    private Controller controller;
    private UserInputController userInputController;

    private DataExchange() {}

    public static DataExchange getInstance() {
        if (instance == null) {
            instance = new DataExchange();
        }
        return instance;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        if (this.controller == null)
            this.controller = controller;
    }

    public UserInputController getUserInputController() {
        return userInputControlle;
    }

    public void setUserInputController(UserInputController userInputController) {
        if (this.userInputController == null || userInputController == null)
            this.userInputController = userInputController;


    }
}
