package data;

import model.AbstractCar;
import model.PassengerCar;
import model.Truck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBHelper {
    public void saveObjects(List<AbstractCar> cars) {
        {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:objects.db");
                createTableIfNeeded(connection);
                connection.createStatement().execute("DELETE FROM objects;");
                cars.forEach(car -> {
                    String sql = "INSERT INTO objects values ('";
                    sql += car.getId();
                    sql += "', ";
                    sql += car.getX();
                    sql += ", ";
                    sql += car.getY();
                    sql += ", ";
                    sql += car.getBirthTime();
                    sql += ", ";
                    sql += car.getLifetime();
                    sql += ", ";
                    if (car instanceof Truck) {
                        sql += "'truck'";
                    } else {
                        sql += "'car'";
                    }
                    sql += ");";

                    try {
                        connection.createStatement().execute(sql);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<AbstractCar> loadObjects() {
        ArrayList<AbstractCar> cars = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:objects.db");
            ResultSet result = connection.createStatement().executeQuery("SELECT * FROM objects;");

            while (result.next()) {
                if (result.getString("type").equals("truck")) {
                    cars.add(new Truck(
                            UUID.fromString(result.getString("id")),
                            result.getFloat("x"),
                            result.getFloat("y"),
                            result.getLong("birthTime"),
                            result.getLong("lifetime")
                    ));
                } else {
                    cars.add(new PassengerCar(
                            UUID.fromString(result.getString("id")),
                            result.getFloat("x"),
                            result.getFloat("y"),
                            result.getLong("birthTime"),
                            result.getLong("lifetime")
                    ));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cars;
    }

    private void createTableIfNeeded(Connection connection) throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS objects\n" +
                "(\n" +
                "    id        TEXT PRIMARY KEY,\n" +
                "    x         REAL    NOT NULL,\n" +
                "    y         REAL    NOT NULL,\n" +
                "    birthTime INTEGER NOT NULL,\n" +
                "    lifetime  INTEGER NOT NULL,\n" +
                "    type       TEXT    NOT NULL\n" +
                ");");
    }
}