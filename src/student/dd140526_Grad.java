package student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.CityOperations;

public class dd140526_Grad implements CityOperations {

	@Override
	public int deleteCity(String... arg0) {
		Statement st = null;
		int deleted = 0;
		for (int i = 0; i < arg0.length; ++i) {
			try {
				st = DB.getInstance().getConnection().createStatement();
				int count = st.executeUpdate("DELETE FROM Grad WHERE Naziv LIKE '" + arg0[i] + "'");
				if (count > 0) deleted++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return deleted;
	}

	@Override
	public boolean deleteCity(int arg0) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("DELETE FROM Grad WHERE IDGrad = " + arg0);
			if(count == 0) return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public List<Integer> getAllCities() {
		List<Integer> cities = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDGrad FROM Grad;");
			while (rs.next()) {
				cities.add(rs.getInt("IDGrad"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cities;
	}

	@Override
	public int insertCity(String name, String postalCode) {
		Statement st = null;
		int id = -1;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("INSERT INTO Grad VALUES ('" + name + "', '" + postalCode + "')");
			if(count > 0){
				ResultSet rs = st.executeQuery("SELECT IDGrad FROM Grad WHERE PostBroj LIKE '" + postalCode + "'");
			if (rs.next()) {
				id = rs.getInt("IDGrad");
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return id;
	}

}
