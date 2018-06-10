package student;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.CourierRequestOperation;

public class dd140526_KurirZahtev implements CourierRequestOperation {

	@Override
	public boolean changeVehicleInCourierRequest(String userName, String licencePlateNumber) {
		Statement st = null, st2 = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			st2 = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 = st.executeQuery("SELECT IDKorisnik from Korisnik WHERE Username = '" + userName + "';");
			if (rs1.next()) {
				int idkor = rs1.getInt("IDKorisnik");
				ResultSet rs3 = st2.executeQuery("SELECT IDVozila FROM Zahtev WHERE IDKorisnik = " + idkor);
				if(rs3.next()){
				int staro = rs3.getInt("IDVozila");
				
				ResultSet rs2 = st
						.executeQuery("SELECT IDVozila from Vozilo WHERE RegBr = '" + licencePlateNumber + "';");
				if (rs2.next()) {
					int idVoz = rs2.getInt("IDVozila");
					int count = st
							.executeUpdate("UPDATE Zahtev SET IDVozila =" + idVoz + " WHERE IDKorisnik = " + idkor + " AND IDVozila = '"
									+ staro + "';");
					if (count == 0)
						return false;
					return true;
				}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteCourierRequest(String arg0) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDKorisnik FROM Korisnik WHERE Username = '" + arg0 + "';");
			if (rs.next()) {
				int idkor = rs.getInt("IDKorisnik");
				int count = st.executeUpdate("DELETE FROM Zahtev WHERE IDKorisnik =" + idkor);
				if (count == 0)
					return false;
				return true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<String> getAllCourierRequests() {
		List<String> requests = new ArrayList<String>();
		Statement st = null, st2 = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			st2 = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDKorisnik FROM Zahtev;");
			while (rs.next()) {
				int idkor = rs.getInt("IDKorisnik");
				ResultSet rs2 = st2.executeQuery("SELECT Username FROM Korisnik WHERE IDKorisnik =" + idkor);
				if(rs2.next())
				requests.add(rs2.getString("Username"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return requests;
	}

	@Override
	public boolean grantRequest(String username) {
		Statement st = null, st2 = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			st2 = DB.getInstance().getConnection().createStatement();
			int idKor = -1, voz = -1;
			ResultSet postoji = st.executeQuery("SELECT IDKorisnik FROM Korisnik WHERE Username = '" + username + "';");
			if (postoji.next()) {
				idKor = postoji.getInt("IDKorisnik");
				ResultSet rs = st.executeQuery("SELECT * FROM Kurir WHERE IDKorisnik = " + idKor + ";");
				if (rs.next()) {
					return false; // vec je kurir
				}

				rs = st.executeQuery("SELECT * FROM Zahtev WHERE IDKorisnik = " + idKor + ";");
				while (rs.next()) {
					voz = rs.getInt("IDVozila");
					ResultSet rs1 = st2.executeQuery("SELECT * FROM Kurir WHERE IDVozila = " + voz + ";");
					if (!rs1.next()) {
						CallableStatement callSt = DB.getInstance().getConnection()
								.prepareCall("{ call ObradiZahtevKurir (?,?,?)}");
						callSt.setInt("idKor", idKor);
						callSt.setInt("idVoz", voz);
						callSt.setInt("status", 1);
						int br = callSt.executeUpdate();
						if (br != 0) {
							return true;
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean insertCourierRequest(String userName, String licencePlateNumber) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 = st.executeQuery("SELECT IDKorisnik from Korisnik WHERE Username = '" + userName + "';");
			if (rs1.next()) {
				int idkor = rs1.getInt("IDKorisnik");
				ResultSet rs2 = st
						.executeQuery("SELECT IDVozila from Vozilo WHERE RegBr = '" + licencePlateNumber + "';");
				if (rs2.next()) {
					int idVoz = rs2.getInt("IDVozila");
					int count = st.executeUpdate(
							"INSERT INTO Zahtev (IDKorisnik, IDVozila, Potvrda) VALUES (" + idkor + ", " + idVoz +", " + 0 +  ");");
					if (count == 0)
						return false;
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
