package student;

import java.sql.SQLException;
import java.sql.Statement;

import operations.GeneralOperations;

public class dd140526_Generalka implements GeneralOperations {

	@Override
public void eraseAll() {
		
		Statement st;
		try {
			st = DB.getInstance().getConnection().createStatement();
			st.executeUpdate("DELETE FROM Ponuda");
			st.executeUpdate("DELETE FROM Zahtev");
			st.executeUpdate("DELETE FROM Voznja");
			//st.executeUpdate("DELETE FROM Paket");
			st.executeUpdate("DELETE FROM ZahtevPaket");
			st.executeUpdate("DELETE FROM Opstina");
			st.executeUpdate("DELETE FROM Grad");
			st.executeUpdate("DELETE FROM Kurir");
			st.executeUpdate("DELETE FROM Admin");
			st.executeUpdate("DELETE FROM Vozilo");
			st.executeUpdate("DELETE FROM Korisnik");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
