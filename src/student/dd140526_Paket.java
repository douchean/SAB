package student;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import operations.PackageOperations;

public class dd140526_Paket implements PackageOperations {

	private static int[] pocetnaCena = { 10, 25, 75 };
	private static int[] tezinskiFaktor = { 0, 1, 2 };
	private static int[] cenaPoKg = { 0, 100, 300 };
	private static int[] cenaGoriva = { 15, 36, 32 };

	private double cenaJedneIsporuke(int i, double tezina, int x1, int y1, int x2, int y2) {
		double cena = 0, rastojanje = 0;
		rastojanje = Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) - Math.pow(Math.abs(y1 - y2), 2));
		cena = (pocetnaCena[i] + (tezinskiFaktor[i] * tezina) * cenaPoKg[i]) * rastojanje;
		return cena;
	}

	@Override
	public boolean acceptAnOffer(int offerId) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st
					.executeQuery("SELECT IDZahtevPaket, IDKorisnik FROM Ponuda WHERE IDZahtevPaket = " + offerId);
			int idzahtev = rs.getInt("IDZahtevPaket");
			int idkor = rs.getInt("IDKorisnik");
			int count1 = st.executeUpdate("UPDATE ZahtevPaket SET StatusIsporuke = 1, IDKurir = " + idkor
					+ ", VremePrihvatanja = CURRENT_TIMESTAMP WHERE IDZahtevPaket = " + idzahtev);
			if (count1 > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeType(int packageId, int newType) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st
					.executeUpdate("UPDATE ZahtevPaket SET Tip = " + newType + " WHERE IDZahtevPaket = " + packageId);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeWeight(int packageId, BigDecimal newWeight) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("UPDATE ZahtevPaket SET Tezina = " + Double.parseDouble(newWeight.toString())
					+ " WHERE IDZahtevPaket = " + packageId);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deletePackage(int packageId) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			int count = st.executeUpdate("DELETE FROM Paket WHERE IDPaket = " + packageId);
			if (count == 0)
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public int driveNextPackage(String courierUserName) {
		Statement st = null;
		Statement st2 = null;
		int idkor, idpaket, polazak, cilj, tipPaketa, tipVozila; //inicijalizuj
		double tezina;
		try {
			st = DB.getInstance().getConnection().createStatement();
			st2 = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st
					.executeQuery("SELECT IDKorisnik FROM Korisnik WHERE Username = '" + courierUserName + "';");
			if (rs.next()) {
				idkor = rs.getInt("IDKorisnik");
				ResultSet rs2 = st.executeQuery("SELECT Status FROM Kurir WHERE IDKorisnik = " + idkor);
				if (rs2.next()) {
					int status = rs2.getInt("Status");
					if (status == 0) {
						st2.executeUpdate("UPDATE Kurir SET Status = 1 WHERE IDKorisnik =" + idkor);
						st2.executeUpdate("UPDATE ZahtevPaket SET StatusIsporuke = 2 WHERE StatusIsporuke  = 1 AND "
								+ "IDKurir = " + idkor);
					}
					ResultSet rs3 = st.executeQuery("SELECT * FROM ZahtevPaket WHERE StatusIsporuke = 2 AND IDKurir = "
							+ idkor + " ORDER BY VremePrihvatanja ASC");
					if (rs3.next()) {
						idpaket = rs.getInt("IDZahtevPaket");
						polazak = rs.getInt("IDOpstinaPolazak");
						cilj = rs.getInt("IDOpstinaCilj");
						tipPaketa = rs.getInt("Tip");
						tezina = rs.getDouble("Tezina");
						st2.executeUpdate("UPDATE ZahtevPaket SET StatusIsporuke = 3 WHERE IDZahtevPaket = " + idpaket);

						ResultSet rs4 = st2.executeQuery("SELECT X, Y FROM Opstina WHERE IDOpstina =" + polazak);
						if (rs4.next()) {
							int x1 = rs4.getInt("X");
							int y1 = rs4.getInt("Y");
							int x2, y2;

							ResultSet rs5 = st2.executeQuery("SELECT X, Y FROM Opstina WHERE IDOpstina =" + cilj);
							if (rs5.next()) {
								x2 = rs5.getInt("X");
								y2 = rs5.getInt("Y");
							}
							double distanca = rastojanje(x1, y1, x2, y2);
							double dobitak = cenaJedneIsporuke(tip, tezina, x1, y1, x2, y2);
						}
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Date getAcceptanceTime(int packageId) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st
					.executeQuery("SELECT VremePrihvatanja FROM ZahtevPaket WHERE IDZahtevPaket = " + packageId);
			if (rs.next()) {
				return rs.getDate("VremePrihvatanja");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getAllOffers() {
		List<Integer> list = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDPonuda FROM Ponuda;");
			while (rs.next()) {
				list.add(rs.getInt("IDPonuda"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
		List<Pair<Integer, BigDecimal>> list = new ArrayList<Pair<Integer, BigDecimal>>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDPonuda, Procenat FROM Ponuda WHERE IDZahtevPaket = " + packageId);
			while (rs.next()) {
				list.add(new dd140526_Pair<Integer, BigDecimal>(rs.getInt("IDPonuda"),
						new BigDecimal(rs.getDouble("Procenat"))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Integer> getAllPackages() {
		List<Integer> list = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDZahtevPaket FROM ZahtevPaket;");
			while (rs.next()) {
				list.add(rs.getInt("IDZahtevPaket"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Integer> getAllPackagesWithSpecificType(int type) {
		List<Integer> list = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDZahtevPaket FROM ZahtevPaket WHERE Tip =" + type);
			while (rs.next()) {
				list.add(rs.getInt("IDZahtevPaket"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer getDeliveryStatus(int packageId) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT Tip FROM ZahtevPaket WHERE IDZahtevPaket = " + packageId);
			if (rs.next()) {
				return rs.getInt("Tip");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getDrive(String courierUsername) {
		List<Integer> list = new ArrayList<Integer>();
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT IDZahtevPaket FROM ZahtevPaket p, Kurir q, Korisnik k"
					+ " WHERE p.IDKorisnik = q.IDKorisnik AND q.IDKorisnik = k.IDKorisnik AND" + " k.Username = '"
					+ courierUsername + "' AND p.StatusIsporuke = 2");
			while (rs.next()) {
				list.add(rs.getInt("IDZahtevPaket"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public BigDecimal getPriceOfDelivery(int packageId) {
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT Cena FROM ZahtevPaket WHERE IDZahtevPaket = " + packageId);
			if (rs.next()) {
				Double d = rs.getDouble("Cena");
				if (d != null)
					return new BigDecimal(d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
		int pkey = -1;
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 = st.executeQuery("SELECT IDKorisnik from Korisnik WHERE Username = '" + userName + "';");
			if (rs1.next()) {
				int idkor = rs1.getInt("IDKorisnik");

				int count = st.executeUpdate(
						"INSERT INTO ZahtevPaket (IDKorisnik, IDOpstinaPolazak, IDOpstinaCilj, Tip, Tezina"
								+ ") VALUES (" + idkor + ", " + districtFrom + ", " + districtTo + ", " + packageType
								+ ", " + Double.parseDouble(weight.toString()) + ");");
				if (count > 0) {
					ResultSet rs = st.executeQuery("SELECT IDZahtevPaket FROM ZahtevPaket WHERE IDKorisnik = " + idkor
							+ " AND IDOpstinaPolazak = " + districtFrom + " AND IDOpstinaCilj = " + districtTo);
					if (rs.next()) {
						pkey = rs.getInt("IDZahtevPaket");
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pkey;
	}

	@Override
	public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {
		int pkey = -1;
		Statement st = null;
		try {
			st = DB.getInstance().getConnection().createStatement();
			ResultSet rs1 = st
					.executeQuery("SELECT IDKorisnik from Korisnik WHERE Username = '" + couriersUserName + "';");
			if (rs1.next()) {
				int idkor = rs1.getInt("IDKorisnik");

				int count = st.executeUpdate("INSERT INTO Ponuda (IDKorisnik, IDZahtevPaket, Procenat" + ") VALUES ("
						+ idkor + ", " + packageId + ", " + Double.parseDouble(pricePercentage.toString()) + ");");
				if (count > 0) {
					ResultSet rs = st.executeQuery("SELECT IDPonuda FROM Ponuda WHERE IDKorisnik = " + idkor
							+ " AND IDZahtevPaket = " + packageId);
					if (rs.next()) {
						pkey = rs.getInt("IDPonuda");
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pkey;
	}

}
