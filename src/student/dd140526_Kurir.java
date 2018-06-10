package student;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.CourierOperations;

public class dd140526_Kurir implements CourierOperations {

	@Override
	public boolean deleteCourier(String arg0) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("DELETE FROM Kurir WHERE IDKorisnik = (SELECT IDKorisnik from Korisnik WHERE username"
					+ " LIKE '" + arg0 + "' )");
			if(count == 0) return false;
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public List<String> getAllCouriers() {
		List<String> couriers = new ArrayList<String>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT ko.username FROM Kurir ku, Korisnik ko "
					+ "WHERE ko.IDKorisnik = ku.IDKorisnik ORDER BY ku.Profit DESC");
			while (rs.next()) {
				couriers.add(rs.getString("username"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return couriers;
	}

	@Override
	public BigDecimal getAverageCourierProfit(int arg0) {
		BigDecimal prosek = null;
		int obradjenih = 0;
		int profit = 0;
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT Profit FROM Kurir WHERE BrPaketa >= " + arg0);
			while (rs.next()) {
				profit += rs.getInt("Profit");
				obradjenih++;
			}
			if (obradjenih == 0)
				return BigDecimal.valueOf(0);
			prosek = BigDecimal.valueOf(profit).divide(BigDecimal.valueOf(obradjenih));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return prosek;
	}

	@Override
	public List<String> getCouriersWithStatus(int arg0) {
		List<String> couriers = new ArrayList<String>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT ko.username FROM Korisnik ko, Kurir ku WHERE "
					+ "ko.IDKorisnik = ku.IDKorisnik AND ku.status = " + arg0);
			while (rs.next()) {
				couriers.add(rs.getString("username"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return couriers;
	}

	@Override
	public boolean insertCourier(String userame, String licencePlate) {
		Statement st = null;
		int key = 0, voz = 0;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 = st.executeQuery("SELECT IDKorisnik FROM Korisnik WHERE Username = '" + userame + "';");
			if (rs1.next()) {
				key = rs1.getInt("IDKorisnik");
			
			ResultSet rs2 = st.executeQuery("SELECT IDVozila FROM Vozilo WHERE RegBr LIKE '" + licencePlate + "';");
			if (rs2.next()) {
				voz = rs2.getInt("IDVozila");
			
			int count = st.executeUpdate("INSERT INTO Kurir(IDKorisnik, BrPaketa, Profit, IDVozila, Status) " + " VALUES (" + key
					+ ", 0, 0, " + voz + ", 0)");

			if(count == 0) return false;
			return true;
			}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}

}
