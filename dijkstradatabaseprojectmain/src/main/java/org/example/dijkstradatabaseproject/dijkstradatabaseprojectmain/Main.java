package org.example.dijkstradatabaseproject.dijkstradatabaseprojectmain;

import org.example.dijkstradatabaseproject.dijkstradatabaseprojectdao.DataBaseWorker;

public class Main {
    public static void main(String[] args) {
        DataBaseWorker dataBaseWorker = new DataBaseWorker();
        dataBaseWorker.workWithDatabase();
    }
}
