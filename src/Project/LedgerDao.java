package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;


public class LedgerDao {

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "oraman";
	private String password = "oracle";
	

	public LedgerDao() {

		try { // 해야할일 5번 트라이 케치 하기 유입물 1번
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private Connection getConnection() {
		// 해야할일 7번 DriverManager.getConnection(url, id, password); 매소드 안에 만들고
		// 유입물 2번
		// 트라이 케치 하기
		try {
			return DriverManager.getConnection(url, id, password);// 리턴에 넣기
		} catch (SQLException e) {
			e.printStackTrace();
			return null; // return 생성해야함
		}

	}

	// 삭제 메소드
	public int DeleteData(int no) {

		String sql = "delete from ledger where no = ?";
		int cnt = -999999; // 삭제의 추가된 내용
		PreparedStatement pstmt = null;

		Connection conn = getConnection();

		try {

			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql); // 트랜잭션 처리 (삭제의 추가된 내용)

			pstmt.setInt(1, no); // ? 치환하는 것

			cnt = pstmt.executeUpdate(); // execute 위에 치환하는 것 적어야함

			conn.commit(); // 순서 중요 가장 마지막 (삭제의 추가된 내용)

		} catch (SQLException e) {

			try {
				conn.rollback(); // 삭제의 추가된 내용
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 오류 상수에 마이너스를 붙여서 리턴
			// 호출하는 곳에서 실제 오류 상수를 이용하여 적절한 오류 메시지를 보여 주면 된다.
			cnt = -e.getErrorCode();
			e.printStackTrace();

		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return cnt;

	}

	// 수정 메소드
	public int UpdateData(ListTable list) {

		//System.out.println( list.toString() );
		String sql = "update ledger set memo=?, pay=?, today=to_date(?,'yyyy/mm/dd'), price=?, balance=? where no= ?";

		int cnt = -999999; // 삭제의 추가된 내용
		PreparedStatement pstmt = null;

		Connection conn = getConnection();

		try {

			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql); // 트랜잭션 처리 (삭제의 추가된 내용)

			pstmt.setInt(6, list.getNo()); // ? 치환하는 것
			pstmt.setString(1, list.getMemo());
			pstmt.setInt(2, list.getPay());
			pstmt.setString(3, list.getToday());
			pstmt.setInt(4, list.getPrice());
			pstmt.setInt(5, list.getBalance());

			cnt = pstmt.executeUpdate(); // execute 위에 치환하는 것 적어야함

			conn.commit(); // 순서 중요 가장 마지막 (삭제의 추가된 내용)

		} catch (SQLException e) {

			try {
				conn.rollback(); // 삭제의 추가된 내용
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 오류 상수에 마이너스를 붙여서 리턴
			// 호출하는 곳에서 실제 오류 상수를 이용하여 적절한 오류 메시지를 보여 주면 된다.
			cnt = -e.getErrorCode();
			e.printStackTrace();

		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return cnt;

	}

	// 저장 메소드

	public int InsertData(ListTable list) {

		String sql = "insert into ledger (no, today, pay, memo, price, balance)";
		sql += " values(myseq.nextval,to_date(?, 'yyyy/mm/dd'),?,?,?,?)";

		int cnt = -999999; // 삭제의 추가된 내용
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;

		ResultSet rs = null;
		Connection conn = getConnection();

		try {
			String sql2 = " select balance from ledger ";
			sql2 += " where no = ( select max(no) from ledger)";
			pstmt2 = conn.prepareStatement(sql2);
			rs = pstmt2.executeQuery();
			int balance = 0;
			if (rs.next()) {
				//System.out.println("balance");
				balance = rs.getInt("balance");
			}else{
				balance = 0;
			}

			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql); // 트랜잭션 처리 (삭제의 추가된 내용)

			// pstmt.setInt(1, list.getNo()); // ? 치환하는 것
			pstmt.setString(1, list.getToday());
			pstmt.setInt(2, list.getPay());
			pstmt.setString(3, list.getMemo());
			pstmt.setInt(4, list.getPrice());
			
			// 잔액은 이전 행의 잔액과 산술 연산
			if (list.getPay() == 1) {
				balance += list.getPrice();
			} else {
				balance -= list.getPrice();
			}
			pstmt.setInt(5, balance);

			cnt = pstmt.executeUpdate(); // execute 위에 치환하는 것 적어야함

			conn.commit(); // 순서 중요 가장 마지막 (삭제의 추가된 내용)

		} catch (SQLException e) {

			try {
				conn.rollback(); // 삭제의 추가된 내용
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// 오류 상수에 마이너스를 붙여서 리턴
			// 호출하는 곳에서 실제 오류 상수를 이용하여 적절한 오류 메시지를 보여 주면 된다.
			cnt = -e.getErrorCode();
			e.printStackTrace();

		} finally {

			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return cnt;

	}

	// 전체 불러오기
	public List<ListTable> GetLedgerList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from ledger "; // 세미콜론 붙이지 말것
		Connection conn = getConnection();

		List<ListTable> lists = new ArrayList<ListTable>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ListTable oneitem = new ListTable();
				oneitem.setNo(rs.getInt("no"));
				oneitem.setToday( String.valueOf( rs.getDate("today")));
				oneitem.setPay(  rs.getInt("pay") );
				oneitem.setMemo( rs.getString("memo") );
				oneitem.setPrice( rs.getInt("price") ); 
				oneitem.setBalance(rs.getInt("balance"));
 

				lists.add(oneitem);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return lists;
	}
	
	//선택한달 조회	
	public List<ListTable> GetListMonth( int year, int month  ){ //위에 코딩한거 복사하기

		PreparedStatement pstmt = null ;
		
		ResultSet rs = null ; 
		
		String sql = "select * from ledger where today between to_date(?,'yyyy/MM/dd') and to_date(?,'yyyy/MM/dd')" ; 	//해야할일 15 ""안의 내용 바꿔주기
		Connection conn = getConnection() ;		
		
		
		Utility util = new Utility(year, month) ;
		String xx = util.getFirstDay() ;
		String yy = util.getLastDay() ;
		
		List<ListTable> lists = new ArrayList<ListTable>(); // 해야할일 14 	
		
		
		try {
			
			pstmt = conn.prepareStatement(sql) ;
			
			pstmt.setString(1, xx); // 해야할일 18  ?를 바꾸기 위한 문법 {1의 의미는 ?의 위치}
			pstmt.setString(2, yy);
			//System.out.println( xx + "," + yy );
			
			rs = pstmt.executeQuery() ; 
			
			
			while( rs.next() ){ 
				ListTable oneitem = new ListTable(); 
				oneitem.setNo(rs.getInt("no"));
				oneitem.setToday( String.valueOf( rs.getDate("today")));
				oneitem.setPay(  rs.getInt("pay") );
				oneitem.setMemo( rs.getString("memo") );
				oneitem.setPrice( rs.getInt("price") ); 
				oneitem.setBalance(rs.getInt("balance"));
				lists.add(oneitem);
			}
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try {
				
				if( rs != null ){rs.close();}
				if( pstmt != null ){pstmt.close();}
				if( conn != null ){conn.close();}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}	
		return lists ;
	}
	
	
	
	
	//이번달 가격 조회	
		public List<ListTable> GetListNowMonth(int nowyear, int nowmonth ){ 
			
			
			PreparedStatement pstmt = null ;
			
			ResultSet rs = null ; 
			
			String sql = "select pay, sum(price) as sumprice from ledger where today between to_date(?,'yyyy/MM/dd') and to_date(?,'yyyy/MM/dd') group by pay " ; 	//해야할일 15 ""안의 내용 바꿔주기
			Connection conn = getConnection() ;		
			
			
			Utility util = new Utility(nowyear, nowmonth) ;
			String xx = util.getFirstDay() ;
			String yy = util.getLastDay() ;
			
			List<ListTable> lists = new ArrayList<ListTable>(); // 해야할일 14 	
			
			ListTable oneitem = null;
			
			try {
				
				pstmt = conn.prepareStatement(sql) ;
				
				pstmt.setString(1, xx); // 해야할일 18  ?를 바꾸기 위한 문법 {1의 의미는 ?의 위치}
				pstmt.setString(2, yy);
				
				rs = pstmt.executeQuery() ; 
				
				
				while( rs.next() ){ 
					//ListTable oneitem = new ListTable(); 
					
					oneitem = new ListTable();
					oneitem.setPay(  rs.getInt("pay") );
					oneitem.setPrice(rs.getInt("sumprice"));
					lists.add(oneitem);
					
				}
				
				
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
				try {
					
					if( rs != null ){rs.close();}
					if( pstmt != null ){pstmt.close();}
					if( conn != null ){conn.close();}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}	
			return lists ;
		}
		
		//이번달 수입 
				public List<ListTable> GetListNowMonthSum(int nowyear, int nowmonth ,int pay ){ 
					
					
					PreparedStatement pstmt = null ;
					
					ResultSet rs = null ; 
					
					String sql = "select pay, sum(price) as sumprice from ledger ";
					sql+= " where (today between to_date(?,'yyyy/MM/dd') and to_date(?,'yyyy/MM/dd')) and  pay=? ";
					sql+= " group by pay " ; 	//해야할일 15 ""안의 내용 바꿔주기
					Connection conn = getConnection() ;		
					
					
					Utility util = new Utility(nowyear, nowmonth) ;
					String xx = util.getFirstDay() ;
					String yy = util.getLastDay() ;
					
					List<ListTable> lists = new ArrayList<ListTable>(); // 해야할일 14 	
					
					ListTable oneitem = null;
					
					
					try {
						
						pstmt = conn.prepareStatement(sql) ;
						
						pstmt.setString(1, xx); // 해야할일 18  ?를 바꾸기 위한 문법 {1의 의미는 ?의 위치}
						pstmt.setString(2, yy);
						pstmt.setInt(3, pay);
						
						rs = pstmt.executeQuery() ; 
						
						
						while( rs.next() ){ 
							//ListTable oneitem = new ListTable(); 
							
							oneitem = new ListTable();
							oneitem.setPay(  rs.getInt("pay") );
							oneitem.setPrice(rs.getInt("sumprice"));
							lists.add(oneitem);
							
							
						}
						
						
					} catch (SQLException e) {			
						e.printStackTrace();
					}finally{
						try {
							
							if( rs != null ){rs.close();}
							if( pstmt != null ){pstmt.close();}
							if( conn != null ){conn.close();}
							
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}	
					return lists ;
				}
	
	
}
