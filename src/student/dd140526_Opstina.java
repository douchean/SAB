package student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.DistrictOperations;

public class dd140526_Opstina implements DistrictOperations {

	@Override
	public int deleteAllDistrictsFromCity(String arg0) {
		Statement st = null;
		int deleted = 0;
		int idkor = 0;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 =  st.executeQuery("SELECT IDGrad FROM Grad WHERE Naziv = '" + arg0 + "'");
			if (rs1.next()) {
				idkor = rs1.getInt("IDGrad");
			
			deleted = st.executeUpdate("DELETE FROM Opstina WHERE IDGrad = " + idkor + ";");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return deleted;
	}

	@Override
	public boolean deleteDistrict(int arg0) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("DELETE FROM Opstina WHERE IDOpstina = " +  arg0);
			if(count == 0) return false;
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	public int deleteDistricts(String... arg0) {
		Statement st = null;
		int deleted = 0;
		for (int i = 0; i < arg0.length; ++i) {
			try {
				st = DB.getInstance().getConnection().createStatement();
				int count = st.executeUpdate("DELETE FROM Opstina WHERE Naziv LIKE '" + arg0[i] + "'");
				if (count > 0) deleted++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return deleted;
	}

	@Override
	public List<Integer> getAllDistricts() {
		List<Integer> list = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDOpstina FROM Opstina;");
			while (rs.next()) {
				list.add(rs.getInt("IDOpstina"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Integer> getAllDistrictsFromCity(int arg0) {
		List<Integer> lista = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDOpstina FROM Opstina "
					+ " WHERE IDGrad = " + arg0);
			while (rs.next()) {
				lista.add(rs.getInt("IDOpstina"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(lista.isEmpty()) return null;
		return lista; 
	}

	@Override
	public int insertDistrict(String name, int cityId, int xCord, int yCord) {
		int id = -1;
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("INSERT INTO Opstina(Naziv, X, Y, IDGrad) " + " VALUES ('" + name + "', " + xCord
					+ ", " + yCord + ", " + cityId + ")");
			if (count > 0) {
				ResultSet rs = st.executeQuery("SELECT IDOpstina FROM Opstina WHERE Naziv LIKE '" + name + "' AND X = "
						+ xCord + " AND Y = " + yCord + " AND IDGrad = " + cityId);
				if (rs.next()) {
					id = rs.getInt("IDOpstina");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return id;

	}

}
