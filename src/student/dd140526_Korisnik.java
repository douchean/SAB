package student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.UserOperations;

public class dd140526_Korisnik implements UserOperations {

	@Override
	public int declareAdmin(String arg0) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDKorisnik FROM Korisnik WHERE Username LIKE '" + arg0 + "'");
			if (rs.next()) {
				int idkor = rs.getInt("IDKorisnik");
				ResultSet rs2 = st.executeQuery("SELECT IDKorisnik FROM Administrator WHERE IDKorisnik = " + idkor);
				if (rs2.next()) {
					return 1; // already an admin
				} else {
					int count = st.executeUpdate("INSERT INTO Administrator(IDKorisnik) VALUES(" + idkor + " )");
					if (count == 0)
						return 2; // nece
					else
						return 0; // ok

				}
			} else
				return 2;
		} catch (SQLException e) {
			e.printStackTrace();
			return 2;// sry no
		}
	}

	@Override
	public int deleteUsers(String... arg0) {
		Statement st = null;
		int deleted = 0;
		for (int i = 0; i < arg0.length; ++i) {
			try {
				st = DB.getInstance().getConnection().createStatement();
				int count = st.executeUpdate("DELETE FROM Korisnik WHERE Username = '" + arg0[i] + "'");
				if (count > 0)
					deleted++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return deleted;
	}

	@Override
	public List<String> getAllUsers() {
		List<String> users = new ArrayList<String>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT Username FROM Korisnik;");
			while (rs.next()) {
				users.add(rs.getString("Username"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public Integer getSentPackages(String... arg0) {
		Integer value = null;
		Statement st = null;
		for (int i = 0; i < arg0.length; ++i) {
			try {
				st = DB.getInstance().getConnection().createStatement();
				ResultSet rs = st.executeQuery("SELECT BrPoslatih FROM Korisnik WHERE Username = '" + arg0[i] + "'");
				if (rs.next()) {
					if (value == null)
						value = new Integer(0);
					Integer brPoslatih = rs.getInt("BrPoslatih");
					value += brPoslatih;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	@Override
	public boolean insertUser(String username, String ime, String prezime, String password) {
		if (!Character.isUpperCase(ime.charAt(0)))
			return false;
		if (!Character.isUpperCase(prezime.charAt(0)))
			return false;
		if (!password.matches(".*\\d+.*"))
			return false;
		if (!password.matches(".*[a-z].*"))
			return false;
		if (password.length() <= 8)
			return false;
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("INSERT INTO Korisnik (Username, Ime, Prezime, Password)" + " VALUES ('"
					+ username + "', '" + ime + "', '" + prezime + "', '" + password + "')");
			if (count > 0) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
