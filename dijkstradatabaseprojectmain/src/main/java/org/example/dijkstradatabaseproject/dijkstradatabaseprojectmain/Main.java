package org.example.dijkstradatabaseproject.dijkstradatabaseprojectmain;

import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.DataBaseConnector;
import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.Edge;
import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.GraphHandler;
import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DataBaseConnector dbConnector = new DataBaseConnector();
        log.info("Connecting to Database");

        String firstQuery = "select * from location";
        String secondQuery = "select to_id, cost from route where from_id = (?)";
        String thirdQuery = "select * from problem";
        String fourthQuery = "insert into solution (problem_id, cost) values (?, ?)";

        try {
            Statement statement = dbConnector.getConnection().createStatement();
            ResultSet firstResultSet = statement.executeQuery(firstQuery);

            List<Node> nodes = new ArrayList<>();

            while(firstResultSet.next()) {
                int index = firstResultSet.getInt(1);
                String name = firstResultSet.getString(2);
                Node node = new Node(name, index);
                nodes.add(node);
            }

            log.info("All nodes from database were added to ArrayList");

            PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(secondQuery);
            preparedStatement.setInt(1,1);
            ResultSet secondResultSet = preparedStatement.executeQuery();
            nodes.get(0).adjacency = new Edge[2];
            while(secondResultSet.next()) {
                int toIndex = secondResultSet.getInt("to_id");
                int cost = secondResultSet.getInt("cost");
                if(nodes.get(0).adjacency[0] == null) {
                    nodes.get(0).adjacency[0] = new Edge(toIndex, cost);
                } else {
                    nodes.get(0).adjacency[1] = new Edge(toIndex, cost);
                }
            }

            log.info("Routes from firstCity were created");

            preparedStatement.setInt(1,2);
            ResultSet thirdResultSet = preparedStatement.executeQuery();
            nodes.get(1).adjacency = new Edge[3];
            while(thirdResultSet.next()) {
                int toIndex = thirdResultSet.getInt("to_id");
                int cost = thirdResultSet.getInt("cost");
                if(nodes.get(1).adjacency[0] == null) {
                    nodes.get(1).adjacency[0] = new Edge(toIndex, cost);
                } else if (nodes.get(1).adjacency[1] == null){
                    nodes.get(1).adjacency[1] = new Edge(toIndex, cost);
                } else {
                    nodes.get(1).adjacency[2] = new Edge(toIndex, cost);
                }
            }

            log.info("Routes from secondCity were created");

            preparedStatement.setInt(1,3);
            ResultSet fourthResultSet = preparedStatement.executeQuery();
            nodes.get(2).adjacency = new Edge[3];
            while(fourthResultSet.next()) {
                int toIndex = fourthResultSet.getInt("to_id");
                int cost = fourthResultSet.getInt("cost");
                if(nodes.get(2).adjacency[0] == null) {
                    nodes.get(2).adjacency[0] = new Edge(toIndex, cost);
                } else if (nodes.get(2).adjacency[1] == null){
                    nodes.get(2).adjacency[1] = new Edge(toIndex, cost);
                } else {
                    nodes.get(2).adjacency[2] = new Edge(toIndex, cost);
                }
            }

            log.info("Routes from thirdCity were created");

            preparedStatement.setInt(1,4);
            ResultSet fifthResultSet = preparedStatement.executeQuery();
            nodes.get(3).adjacency = new Edge[2];
            while(fifthResultSet.next()) {
                int toIndex = fifthResultSet.getInt("to_id");
                int cost = fifthResultSet.getInt("cost");
                if(nodes.get(3).adjacency[0] == null) {
                    nodes.get(3).adjacency[0] = new Edge(toIndex, cost);
                } else {
                    nodes.get(3).adjacency[1] = new Edge(toIndex, cost);
                }
            }

            log.info("Routes from fourthCity were created");

            ResultSet sixthResultSet = statement.executeQuery(thirdQuery);
            int solutionOfProblem = 0;
            Node startPoint = null;
            Node endPoint = null;
            while(sixthResultSet.next()) {
                int fromCityIndex = sixthResultSet.getInt("from_id");
                int toCityIndex = sixthResultSet.getInt("to_id");
                for(Node n : nodes) {
                    if(fromCityIndex == n.index) {
                        startPoint = n;
                    }
                    if(toCityIndex == n.index) {
                        endPoint = n;
                    }
                }
                GraphHandler.computePaths(nodes, startPoint);
                solutionOfProblem = endPoint.minDistance;
            }

            log.info("Route between " + startPoint.index + " and " + endPoint.index + " cities");

            PreparedStatement solutionPreparedStatement = dbConnector.getConnection().prepareStatement(fourthQuery);
            solutionPreparedStatement.setInt(1,1);
            solutionPreparedStatement.setInt(2,solutionOfProblem);
            solutionPreparedStatement.execute();

            log.info("The cheapest path between cities is " + solutionOfProblem);

            statement.close();
            preparedStatement.close();
            solutionPreparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
