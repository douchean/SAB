import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import operations.*;
import student.DB;
import student.dd140526_Generalka;
import student.dd140526_Grad;
import student.dd140526_Korisnik;
import student.dd140526_Kurir;
import student.dd140526_KurirZahtev;
import student.dd140526_Opstina;
import student.dd140526_Vozilo;
import tests.TestHandler;
import tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new dd140526_Grad(); // Change this to your implementation.
        DistrictOperations districtOperations = new dd140526_Opstina(); // Do it for all classes.
        CourierOperations courierOperations = new dd140526_Kurir(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new dd140526_KurirZahtev();
        GeneralOperations generalOperations = new dd140526_Generalka();
        UserOperations userOperations = new dd140526_Korisnik();
        VehicleOperations vehicleOperations = new dd140526_Vozilo();
        PackageOperations packageOperations = null;
/*
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
        
        Statement st = null;
    	try {
			 st  = DB.getInstance().getConnection().createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM Korisnik;");
			while(rs.next()){
				System.out.println(rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	finally{
    		try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}*/
        
    /*    
        List<String> vozila = courierRequestOperation.getAllCourierRequests();
        for(int i = 0; i< vozila.size(); i++){
        	System.out.println(vozila.get(i));
        }*/
        
        
        System.out.println(courierRequestOperation.deleteCourierRequest("sha"));
        
    }
    	
}
