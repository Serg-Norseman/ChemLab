/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chemlab.database;

import bslib.common.AuxUtils;
import chemlab.refbooks.CompoundRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
public class CLDB
{
    private static final int DB_VERSION = 1;
    
    private static CLDB fInstance;

    public static final CLDB getInstance()
    {
        try {
            if (fInstance == null) {
                fInstance = new CLDB();
            }

            return fInstance;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private Connection fConnection = null;
    private Statement fStatement = null;

    private CLDB() throws ClassNotFoundException
    {
        Class.forName("org.sqlite.JDBC");
        this.connect();
    }

    private void connect()
    {
        this.fConnection = null;

        try {
            String dbName = AuxUtils.getAppPath() + "/chemlab.db";

            // create a database connection
            this.fConnection = DriverManager.getConnection("jdbc:sqlite:"+dbName);

            fStatement = this.fConnection.createStatement();
            fStatement.setQueryTimeout(30);  // set timeout to 30 sec.

            // check if database is new
            boolean dbIsNew = false;
            int dbVer = 0;
            try {
                ResultSet rs = fStatement.executeQuery("select * from parameters where name = 'db:Version'");
                if (rs.next()) {
                    dbVer = AuxUtils.parseInt(rs.getString("value"), 0);
                } else {
                    dbVer = 0;
                }
            } catch (SQLException ex) {
                dbIsNew = true;
            }
            
            if (dbIsNew) {
                fStatement.executeUpdate("create table parameters (name string primary key, value string)");
                fStatement.executeUpdate("insert into parameters values('db:Version', '" + DB_VERSION + "')");

                fStatement.executeUpdate("create table compounds (formula string primary key, mass numeric)");
                fStatement.executeUpdate("create table compound_states (formula string primary key, state integer, color integer, gibbs_free_energy numeric, heat_formation numeric, specific_heat numeric, std_entropy numeric, density numeric)");
            } else {
                
            }
            
            //statement.executeUpdate("drop table if exists person");
            //statement.executeUpdate("create table person (id integer, name string)");
            //statement.executeUpdate("insert into person values(1, 'leo')");
            //statement.executeUpdate("insert into person values(2, 'yui')");
            /*ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }*/
        } catch (SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());

            this.fConnection = null;
            this.fStatement = null;
        }
    }

    public void closeConnection()
    {
        try {
            if (this.fConnection != null) this.fConnection.close();
        } catch (Exception ignore) {
        }
    }

    public void execUpdate(String instruction) throws SQLException
    {
        fStatement.executeUpdate(instruction);
    }

    public ResultSet execQuery(String instruction) throws SQLException
    {
        return fStatement.executeQuery(instruction);
    }

    public CompoundRecord getCompound(String formula)
    {
        try {
            CompoundRecord compRec = new CompoundRecord(formula, this);
            return compRec;
        } catch (SQLException e) {
            return null;
        }
    }
}
