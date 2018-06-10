package student;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import operations.VehicleOperations;

public class dd140526_Vozilo implements VehicleOperations {

	@Override
	public boolean changeConsumption(String arg0, BigDecimal arg1) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("UPDATE Vozilo SET Potrosnja = " + Double.parseDouble(arg1.toString()) + 
					" WHERE RegBr = " + arg0);
			if(count == 0) return false;
			return true;
			}

		 catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean changeFuelType(String licensePlateNumber, int fuelType) {
			Statement st = null;
			try {
				st = DB.getInstance().getConnection().createStatement();
				int count = st.executeUpdate("UPDATE Vozilo SET TipGoriva = " + fuelType + 
						" WHERE RegBr = " + licensePlateNumber);
				if(count == 0) return false;
				return true;
				}

			 catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
	}

	@Override
	public int deleteVehicles(String... arg0) {
		Statement st = null;
		int deleted = 0;
		for (int i = 0; i < arg0.length; ++i) {
			try {
				st = DB.getInstance().getConnection().createStatement();
				int count = st.executeUpdate("DELETE FROM Vozilo WHERE RegBr = '" + arg0[i] + "'");
				if (count > 0)
					deleted++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return deleted;
	}

	@Override
	public List<String> getAllVehichles() {
		List<String> users = new LinkedList<String>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT RegBr FROM Vozilo;");
			while (rs.next()) {
				users.add(rs.getString("RegBr"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumtion) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("INSERT INTO Vozilo (RegBr, TipGoriva, Potrosnja) VALUES (" + licencePlateNumber
					+ ", " + fuelType + ", " + Double.parseDouble(fuelConsumtion.toString()) + ");");
			if(count == 0) return false;
			return true;
			}

		 catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

}
