import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formattable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Customer extends Throwable {

	DBConnector db = new DBConnector();
	Properties props;
	Connection con;
	Home home = new Home();


	public void enrollInLP(int custid) {

		Scanner op = new Scanner(System.in);
		System.out.println("1. Enroll in Loyalty Program");
		System.out.println("2. Go Back");
		System.out.print("Your Option : ");

		Home home = new Home();
		int userop = op.nextInt();

		try {
			switch (userop) {
			case 1:
				enrollInLPProcess(custid);
				break;
			case 2:
				home.customerLanding(custid);
				break;
			default:
				System.out.println("You have entered an invalid option");
				enrollInLP(custid);
			}

			op.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getLoyaltyProgramChosen() throws Exception {
		
		Scanner op = new Scanner(System.in);
		Map<Integer, Integer> lpkeys = new HashMap();
		
    	props = readPropertiesFile();
		home = new Home();
		Connection con = db.getConnection();
		String selectLPs =  props.getProperty("getAllLoyaltyPrograms");	
		
		PreparedStatement statement = con.prepareStatement(selectLPs);

		
		ResultSet rs = statement.executeQuery();
		
		int ind = 1;
		while(rs.next()) {
			lpkeys.put(ind, rs.getInt("lpid"));
    	System.out.println(ind + ". " +rs.getString("lpname"));
		ind++;
		}
		System.out.print("Choose the Loyalty Program : ");
		int userop = op.nextInt();
		op.close();
		return lpkeys.get(userop);
				
	}

	public void enrollInLPProcess(int custid) throws Exception {
	  try {	
		db = new DBConnector();
		Connection con = db.getConnection();
		int lpid = getLoyaltyProgramChosen();
		String insertWallet = props.getProperty("insertIntoWallet");
		PreparedStatement statement2 = con.prepareStatement(insertWallet);
		statement2.setInt(1, custid);
		statement2.setInt(2, lpid);
		statement2.executeQuery();
		
		System.out.println("Customer is enrolled in the Loyalty Program");
		//home.customerLanding(custid);
		
	  }
	  catch(Exception e) {
		  System.out.println("Customer is already enrolled in the Loyalty Program");
		  e.printStackTrace();
	  }
	}

	/*public void rewardActivites(int custid, int lpid) throws Exception {
		Scanner op = new Scanner(System.in);
		ActivityType act = new ActivityType();
		Map<Integer, Integer> actkeys = new HashMap();
		
    	props = readPropertiesFile();
		home = new Home();
		Connection con = db.getConnection();
		String selectActTypes =  props.getProperty("selectActivityIDs");	
		
		PreparedStatement statement = con.prepareStatement(selectActTypes);
		
		ResultSet rs = statement.executeQuery();
		
		
		int ind = 1;
		while(rs.next()) {
			actkeys.put(ind, rs.getInt("actid"));
    	System.out.println(ind + ". " +rs.getString("actname"));
		ind++;
		}
		System.out.print("Choose one of the above Reward Activities: ");
		int userop = op.nextInt();
		
		if(userop == 1) {
			act.purchase();	
		}
		else if(userop == 2) {
			act.leaveAReview();
		}
		else if(userop == 3) {
			act.referAFriend();
		}
		
  
    	op.close();
		
	}*/


	public int getlpid(int custid) throws Exception {

		DBConnector db = new DBConnector();
        Connection con = db.getConnection();
        Scanner op1 = new Scanner(System.in);
        
        Connection conn = db.getConnection();
        PreparedStatement statement;
		statement = conn.prepareStatement("SELECT lpid, lpname from LoyaltyProgram where lpid IN (select lpid from WALLET where cid = ? )");
		statement.setInt(1, custid);
		
		ResultSet rs = statement.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int columnsNumber = rsmd.getColumnCount();
		
		while (rs.next()) {
//			for (int i = 1; i <= columnsNumber; i++) {
//				if (i > 1) System.out.print(",  ");
				int columnValue = rs.getInt("lpid");
				String columnName = rs.getString("lpname");
				System.out.print( columnValue  + "--" + columnName );
//			}
			System.out.println(" ");
		}

		System.out.println("Choose your Loyalty Program : ");
		
		return(op1.nextInt());

	}

	public void getrewardActivitiesScreen(int custid) throws Exception {
		
		int lpid = getlpid(custid);

		ActivityType act = new ActivityType();
		//Map<Integer, String> actkeys = new HashMap();
		
    	props = readPropertiesFile();
		home = new Home();
		Connection con = db.getConnection();
		String selectActTypes =  props.getProperty("selectActivityIDs");	
		
		PreparedStatement statement = con.prepareStatement("Select t.actid, at.actName from rerules t, activitytype at where t.lpid = ? and t.actid = at.actid");
		statement.setInt(1, lpid);
		ResultSet rs = statement.executeQuery();
		
		
		int ind = 1;
		while(rs.next()) {
			//actkeys.put(ind, rs.getString("actName"));
    		System.out.println(rs.getInt("actid") + "--" +rs.getString("actname"));
			
		}
		
		System.out.print("Choose the name from the above Reward Activities: ");
		Scanner op = new Scanner(System.in);
		String userip = op.next();
		
		earnPoints(custid, lpid, userip);
		
	}
	
//	public int getRewardType(String custid) throws Exception {
//
//		Scanner op = new Scanner(System.in);
//		props = readPropertiesFile();
//		home = new Home();
//		con = db.getConnection();
//		String rewardName = null;
//
//		System.out.println("1. Purchase");
//		System.out.println("2. Leave a review");
//		System.out.println("3. Refer a friend");
//		System.out.println("4. Go back");
//		System.out.print("Your Option : ");
//
//		Home home = new Home();
//		Customer cust = new Customer();
//		int userop = op.nextInt();
//
//		try {
//			switch (userop) {
//			case 1:
//				rewardName = "Purchase";
//				//home.purchase(custid);
//				break;
//			case 2:
//				rewardName = "Leave a review";
//				//home.leaveReview();
//				break;
//			case 3:
//				rewardName = "Refer a friend";
//				//home.referFriend();
//				break;
//			case 4:
//				System.out.println("Go back");
//				//home.customerLanding(custid);
//				break;
//			default:
//				System.out.println("You have entered an invalid option");
//				getLPjoinedToRewardActivities(custid);
//			}
//
//			op.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String getRewardTypeIdByName = props.getProperty("getRewardTypeIdByName").toString();
//		PreparedStatement statement = con.prepareStatement(getRewardTypeIdByName);
//		statement.setString(1, rewardName);
//		ResultSet rs = statement.executeQuery();
//		rs.next();
//		return rs.getInt("rewid");
//
//	}

	public void earnPoints(int custid, int lpid, String acttype) throws Exception {

		DBConnector db = new DBConnector();
        Connection con = db.getConnection();
        Scanner op1 = new Scanner(System.in);
        
        takeActivityInput(acttype, custid, lpid);
        PreparedStatement statement = con.prepareStatement("Select t.repoints from rerules t, activitytype at where t.lpid = ? and at.actname = ?");
		statement.setInt(1, lpid);
		statement.setString(2, acttype);
		
		ResultSet rs = statement.executeQuery();
		rs.next();
		int points = rs.getInt("repoints");
		
		PreparedStatement statement1;
		statement1 = con.prepareStatement("SELECT points, tier, num_cust_gc from WALLET where cid = ? and lpid = ?");
		statement1.setInt(1, custid);
		statement1.setInt(2, lpid);
		
		ResultSet rs2 = statement1.executeQuery();
		rs2.next();

		int custpoints = rs2.getInt("points");
		int cust_tier = rs2.getInt("tier");
		int num_cust_gc = rs2.getInt("num_cust_gc");
        
        if (acttype.equalsIgnoreCase(acttype)) {
        	while(true) {
        		System.out.print("How many gift cards do you want to use: ");
                int gc_amount = op1.nextInt();
                
                if (gc_amount <= num_cust_gc) {
                	num_cust_gc -= gc_amount;
                	statement1 = con.prepareStatement("INSERT INTO PURCHASE(custid, lpid, giftcardused, purchase_date) values(?, ?, ?, (SELECT SYSDATE FROM DUAL))");
            		statement1.setInt(1, custid);
            		statement1.setInt(2, lpid);
            		statement1.setInt(3, gc_amount);
            		
            		rs2 = statement1.executeQuery();
            		rs2.next();
                	break;
                }
                else {
                	System.out.println("You do not have enough gift cards");
                }
        	}
        }
		
		statement1 = con.prepareStatement("SELECT lptype from LOYALTYPROGRAM where lpid = ?");
		statement1.setInt(1, lpid);
		ResultSet rs3 = statement1.executeQuery();
		rs3.next();
		
		int multiplier = 1;
		int totalpts = 0;
		int new_cust_tier = cust_tier;
		String lp_type = rs3.getString("lptype");
		
		if (lp_type == "tiered") {
			statement1 = con.prepareStatement("SELECT multiplier from BRAND_TIER where lpid = ? and tier_num = ?");
			statement1.setInt(1, lpid);
			statement1.setInt(2, cust_tier);
			ResultSet rs4 = statement1.executeQuery();
			rs4.next();
			
			multiplier = rs4.getInt("multiplier");
			totalpts = custpoints + (points * multiplier);
			
			statement1 = con.prepareStatement("SELECT tier,points from BRAND_TIER where lpid = ? and tier_num >= ?");
			statement1.setInt(1, lpid);
			statement1.setInt(2, cust_tier);
			ResultSet rs5 = statement1.executeQuery();
			
			while(rs5.next()) {
				
				int tier = rs5.getInt("tier");
				int rew_pts = rs5.getInt("points");
				
				if (totalpts >= rew_pts)
					new_cust_tier = tier;
			}
			
		}

		totalpts = custpoints + (points * multiplier);

		statement1 = con.prepareStatement("UPDATE WALLET set points = ?, tier = ?, num_cust_gc = ? where cid = ? and lpid = ?");
		statement1.setInt(1, totalpts);
		statement1.setInt(2, new_cust_tier);
		statement1.setInt(3, num_cust_gc);
		statement1.setInt(4, custid);
		statement1.setInt(5, lpid);

		statement1.executeUpdate();
		
		

		System.out.println("You have earned " + points + " points");
		home.customerLanding(custid);

	}
	
	public void takeActivityInput(String acttype, int custid, int lpid) throws Exception {
		DBConnector db = new DBConnector();
        Connection con = db.getConnection();
		Scanner op = new Scanner(System.in);
		String result = "";
		if (acttype.equalsIgnoreCase("write review")) {
			System.out.print("Enter your review: ");
			result = op.nextLine();
	        
	        PreparedStatement statement = con.prepareStatement("INSERT INTO LEAVE_REVIEW(CID, LPID, REVIEW_CONTENT) values(?, ?, ?)");
			statement.setInt(1, custid);
			statement.setInt(2, lpid);
			statement.setString(3, result);
			
			ResultSet rs = statement.executeQuery();
			rs.next();
		}
		else if (acttype.equalsIgnoreCase("refer friend")) {
			System.out.print("Enter the name of the friend you want to refer: ");
			result = op.nextLine();
			
			PreparedStatement statement = con.prepareStatement("INSERT INTO REFER_FRIEND(CID, LPID, REVIEW_CONTENT) values(?, ?, ?)");
			statement.setInt(1, custid);
			statement.setInt(2, lpid);
			statement.setString(3, result);
			
			ResultSet rs = statement.executeQuery();
			rs.next();
		}
}

	public void redeemPoints(int custid, int lpid, String rewtype) throws Exception {

		DBConnector db = new DBConnector();
        Connection con = db.getConnection();
        Scanner op1 = new Scanner(System.in);
        System.out.println(rewtype);
        PreparedStatement statement = con.prepareStatement("Select t.rrpoints, t.rew_count from rrrules t where t.lpid = ? and t.rewname = ?");
		statement.setInt(1, lpid);
		statement.setString(2, rewtype);
		
		ResultSet rs = statement.executeQuery();
		rs.next();
		int rewardPoints = rs.getInt("rrpoints");
		int brandRewCount = rs.getInt("rew_count");
		
		PreparedStatement statement1;
		statement1 = con.prepareStatement("SELECT points, num_cust_gc, num_cust_fp from WALLET where cid = ? and lpid = ?");
		statement1.setInt(1, custid);
		statement1.setInt(2, lpid);
		
		ResultSet rs2 = statement1.executeQuery();
		rs2.next();

		int custpoints = rs2.getInt("points");
		
		int customerGCCount = rs2.getInt("num_cust_gc");
		int customerFPCount = rs2.getInt("num_cust_fp");
		int totalpoints = 0;
		int spentPoints = 0;
		
		if(rewtype.equalsIgnoreCase("GiftCard")) {
			int giftCardCount = 0;
			Scanner op = new Scanner(System.in);
			System.out.println("Enter the number of gift cards you want");
			System.out.println("This Loyalty Program has " + brandRewCount + " gift cards left at " + rewardPoints + " each");
			while(true) {
				System.out.print("How many do you want: ");
				giftCardCount = op.nextInt();
				if (giftCardCount <= brandRewCount && custpoints >= rewardPoints * giftCardCount) {
					spentPoints = rewardPoints * giftCardCount;
					totalpoints = custpoints - spentPoints;
					customerGCCount += giftCardCount;
					brandRewCount -= giftCardCount;
					break;
				}
				else {
					System.out.println("Enetered amount is greater than number available or you do not have enough points");
				}
			}
		}
		else {
			int freeProductCount = 0;
			Scanner op = new Scanner(System.in);
			System.out.println("Enter the number of free products you want");
			System.out.println("This Loyalty Program has " + brandRewCount + " free products left at " + rewardPoints + " each");
			while(true) {
				System.out.print("How many do you want: ");
				freeProductCount = op.nextInt();
				if (freeProductCount <= brandRewCount && custpoints >= rewardPoints * freeProductCount) {
					spentPoints = rewardPoints * freeProductCount;
					totalpoints = custpoints - spentPoints;
					customerFPCount += freeProductCount;
					brandRewCount -= freeProductCount;
					break;
				}
				else {
					System.out.println("Enetered amount is greater than number available or you do not have enough points");
				}
			}
		}

		

		String queryStatement = "UPDATE WALLET set points = ?, num_cust_gc = ?, num_cust_fp = ? where cid = ? and lpid = ?";
		statement1 = con.prepareStatement(queryStatement);
		statement1.setInt(1, totalpoints);
		statement1.setInt(2, customerGCCount);
		statement1.setInt(3, customerFPCount);
		statement1.setInt(4, custid);
		statement1.setInt(5, lpid);

		statement1.executeUpdate();
		
		queryStatement = "UPDATE RRRules set rew_count = ? where lpid = ? and rewname = ?";
		statement1 = con.prepareStatement(queryStatement);
		statement1.setInt(1, brandRewCount);
		statement1.setInt(2, lpid);
		statement1.setString(3, rewtype);

		statement1.executeUpdate();

		System.out.println("You have spent " + spentPoints + " points");
		home.customerLanding(custid);

	}
	
	public void getLPjoinedToRewardActivities(String custid) {

	}

	public void viewWallet(String custid) {
		try {
			props = readPropertiesFile();
			home = new Home();
			con = db.getConnection();
			String viewWalletQuery = props.getProperty("viewWalletQuery").toString();
			PreparedStatement statement = con.prepareStatement(viewWalletQuery);
			statement.setString(1, custid);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("username"));
			}

			Scanner op = new Scanner(System.in);
			System.out.println("1. Go Back");
			System.out.println("2. Log Out");
			System.out.print("Your Option : ");

			int userop = op.nextInt();
			switch (userop) {
			case 1:
				//home.customerLanding(custid);
				break;
			case 2:
				home.login();
				break;
			default:
				System.out.println("You have entered an invalid option");
				viewWallet(custid);
			}

			op.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Properties readPropertiesFile() throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream("src/queries.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	public void performRewardOperation(String custid, int rewid) {
	
		
	}
	
	public void redeemPointsUI(int custid) throws Exception {
		int lpid = getlpid(custid);

		RewardType rew = new RewardType();
		//Map<Integer, String> actkeys = new HashMap();
		
    	props = readPropertiesFile();
		home = new Home();
		Connection con = db.getConnection();
		String selectRewTypes =  props.getProperty("selectRewardIDs");	
		
		PreparedStatement statement = con.prepareStatement("Select t.rewid, rt.rewName from rrrules t, rewardtype rt where t.lpid = ? and t.rewid = rt.rewid");
		statement.setInt(1, lpid);
		ResultSet rs = statement.executeQuery();
		
		
		int ind = 1;
		while(rs.next()) {
			//actkeys.put(ind, rs.getString("actName"));
    		System.out.println(rs.getInt("rewid") + "--" +rs.getString("rewname"));
			
		}
		
		System.out.print("Choose the name from the above Rewards: ");
		Scanner op = new Scanner(System.in);
		String userip = op.next();
		
		redeemPoints(custid, lpid, userip);
	}
	
	public void viewWallet(int custid) throws Exception {
		try {
			props = readPropertiesFile();
			home = new Home();
			con = db.getConnection();
			String viewWallet = props.getProperty("viewWallet");
			PreparedStatement statement = con.prepareStatement(viewWallet);
			statement.setInt(1, custid);
			ResultSet rs = statement.executeQuery();
			if(rs != null) {
				while (rs.next()) {
					
					System.out.println("Points you have in your wallet for Loyalty Program " +rs.getString("lpname")+" are "
							+ rs.getString("points") + " " + rs.getString("num_cust_gc") + " gift cards and " + rs.getString("num_cust_fp") +
							" free products");
				}
			}
			else {
				System.out.println("You are not associated with a Loyalty Program");
			}
			home.customerLanding(custid);
		} catch (Exception e) {
			e.printStackTrace();
			home.customerLanding(custid);
		}

	}

}
