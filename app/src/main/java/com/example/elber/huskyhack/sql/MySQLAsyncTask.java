package com.example.elber.huskyhack.sql;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final class MySQLAsyncTask extends AsyncTask<String, Void, String> {
    private static final String url = "jdbc:mysql://t13-db.cydqanjpdtss.us-west-2.rds.amazonaws.com:3306/MockData";
    private static final String user = "root";
    private static final String pass = "elbertcheng"; // @TODO: Careful source control.

    private static String res;
    private Connection _connection;
    private AsyncResponse delegate = null;

    void setDelegate(AsyncResponse d) {
        delegate = d;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(url, user, pass);
            _connection = con;

            // Connect to SQLUtil Connection
            SQLUtil.setConnection(con);

            Log.d("Database Connection:", "Database Connection success " + params.toString());


            // Get all results directly
            try {
                getAllResults();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }
        return res;
    }
    @Override
    protected void onPostExecute(String result) {
        Log.d("Task:", "onPostExecute");
        this.res = result;
        delegate.processFinish(result);
    }

    public void getAllResults() throws SQLException {
        // get all locations
        PreparedStatement locationS = _connection.prepareStatement("SELECT * FROM Locations;");

        ResultSet ls = locationS.executeQuery();

        SQLUtil.storeLocations(ls);

        // get all ratings
        PreparedStatement ratingsS = _connection.prepareStatement("SELECT * FROM Rating;");

        ResultSet rs = ratingsS.executeQuery();

        SQLUtil.storeRatings(rs);

        // connect them
        SQLUtil.connectLR();
    }

    // Requests
    public String debug_getAllResults() throws SQLException {
        PreparedStatement ps = _connection.prepareStatement("SELECT * FROM canYouSeeThisAlan;");

        ResultSet rs = ps.executeQuery();

        String result = "";

        while (rs.next()) {
            result += rs.getString(1);
        }

        return result;
    }
}
