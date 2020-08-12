package org.example.dijkstradatabaseproject.dijkstradatabaseprojectdao;

import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.Edge;
import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.GraphHandler;
import org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWorker {

    private static final Logger log = LoggerFactory.getLogger(DataBaseWorker.class);
    private static final String url = "jdbc:postgresql://localhost:5432/dijkstra";
    private static final String userName = "postgres";
    private static final String password = "postgres";

    public void workWithDatabase() {

        String firstQuery = "select * from location";
        String secondQuery = "select to_id, cost from route where from_id = (?)";
        String thirdQuery = "select * from problem";
        String fourthQuery = "insert into solution (problem_id, cost) values (?, ?)";

        List<Node> nodes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, userName, password)) {

            log.info("Connecting to Database");

            try (Statement locationStatement = connection.createStatement()) {

                ResultSet firstResultSet = locationStatement.executeQuery(firstQuery);
                while (firstResultSet.next()) {
                    int index = firstResultSet.getInt(1);
                    String name = firstResultSet.getString(2);
                    Node node = new Node(name, index);
                    nodes.add(node);
                }

                log.info("All nodes from database were added to ArrayList");
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(secondQuery)) {

                preparedStatement.setInt(1, 1);
                ResultSet secondResultSet = preparedStatement.executeQuery();
                nodes.get(0).adjacency = new Edge[2];
                while (secondResultSet.next()) {
                    int toIndex = secondResultSet.getInt("to_id");
                    int cost = secondResultSet.getInt("cost");
                    if (nodes.get(0).adjacency[0] == null) {
                        nodes.get(0).adjacency[0] = new Edge(toIndex, cost);
                    } else {
                        nodes.get(0).adjacency[1] = new Edge(toIndex, cost);
                    }
                }

                log.info("Routes from firstCity were created");

                preparedStatement.setInt(1, 2);
                ResultSet thirdResultSet = preparedStatement.executeQuery();
                nodes.get(1).adjacency = new Edge[3];
                while (thirdResultSet.next()) {
                    int toIndex = thirdResultSet.getInt("to_id");
                    int cost = thirdResultSet.getInt("cost");
                    if (nodes.get(1).adjacency[0] == null) {
                        nodes.get(1).adjacency[0] = new Edge(toIndex, cost);
                    } else if (nodes.get(1).adjacency[1] == null) {
                        nodes.get(1).adjacency[1] = new Edge(toIndex, cost);
                    } else {
                        nodes.get(1).adjacency[2] = new Edge(toIndex, cost);
                    }
                }

                log.info("Routes from secondCity were created");

                preparedStatement.setInt(1, 3);
                ResultSet fourthResultSet = preparedStatement.executeQuery();
                nodes.get(2).adjacency = new Edge[3];
                while (fourthResultSet.next()) {
                    int toIndex = fourthResultSet.getInt("to_id");
                    int cost = fourthResultSet.getInt("cost");
                    if (nodes.get(2).adjacency[0] == null) {
                        nodes.get(2).adjacency[0] = new Edge(toIndex, cost);
                    } else if (nodes.get(2).adjacency[1] == null) {
                        nodes.get(2).adjacency[1] = new Edge(toIndex, cost);
                    } else {
                        nodes.get(2).adjacency[2] = new Edge(toIndex, cost);
                    }
                }

                log.info("Routes from thirdCity were created");

                preparedStatement.setInt(1, 4);
                ResultSet fifthResultSet = preparedStatement.executeQuery();
                nodes.get(3).adjacency = new Edge[2];
                while (fifthResultSet.next()) {
                    int toIndex = fifthResultSet.getInt("to_id");
                    int cost = fifthResultSet.getInt("cost");
                    if (nodes.get(3).adjacency[0] == null) {
                        nodes.get(3).adjacency[0] = new Edge(toIndex, cost);
                    } else {
                        nodes.get(3).adjacency[1] = new Edge(toIndex, cost);
                    }
                }

                log.info("Routes from fourthCity were created");
            }

            int solutionOfProblem = 0;
            Node startPoint = null;
            Node endPoint = null;

            try (Statement problemStatement = connection.createStatement()) {

                ResultSet sixthResultSet = problemStatement.executeQuery(thirdQuery);
                while (sixthResultSet.next()) {
                    int fromCityIndex = sixthResultSet.getInt("from_id");
                    int toCityIndex = sixthResultSet.getInt("to_id");
                    for (Node n : nodes) {
                        if (fromCityIndex == n.index) {
                            startPoint = n;
                        }
                        if (toCityIndex == n.index) {
                            endPoint = n;
                        }
                    }
                    GraphHandler.computePaths(nodes, startPoint);
                    solutionOfProblem = endPoint.minDistance;
                }

                log.info("Route between " + startPoint.index + " and " + endPoint.index + " cities");
            }

            try (PreparedStatement solutionPreparedStatement = connection.prepareStatement(fourthQuery)) {

                solutionPreparedStatement.setInt(1, 1);
                solutionPreparedStatement.setInt(2, solutionOfProblem);
                solutionPreparedStatement.execute();

                log.info("The cheapest path between cities is " + solutionOfProblem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
