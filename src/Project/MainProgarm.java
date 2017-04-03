package Project;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class MainPro extends JFrame implements ActionListener, ItemListener {
	// 컴포넌트, 배치 관리자, 컨테이너 변수 선언

	CardLayout card;

	private int pay;
	private int month;
	private int year;
	private JPanel pMainL, pMainT;
	private JPanel pTopL, pTopL1, pTopL2 = null;
	private JPanel pCenterL, pLeftL, pRightL = null;
	private JPanel pRightL1, pRightL2 = null;
	private JPanel pBottomL = null;

	private JPanel pLeftT, pCenterT, pRightT , pCenterTG, pCenterTG1= null;

	private JButton btUpdateL, btDeleteL, btSaveL = null;
	private JButton btLedgerL, btTotalL, bExitL = null;
	private JButton btSearchL = null;
	
	private JButton btGraph1 , btGraph2 = null;
	
	private JButton bExitT1, bExitT2 = null;

	private JScrollPane jspane = null;
	private JTable table = null;
	private MyTableModel dtm = null;

	private JTextField jTfL1, jTfL2, jTfL3 = null;
	private JComboBox<String> jComyearL, jCommonthL, jComPayL = null;
	private JLabel jLabelL1, jLabelL2, jLabelL3, jLabelL4, jLabelL5 = null;
	private JLabel jLabelT1, jLabelT2, jLabelT3 = null;
	private JTextArea jTaT1, jTaT2 = null;

	private List<ListTable>  imsiData;
	private LedgerDao dao = new LedgerDao();

	private DrawTool drawtool = new DrawTool();
	private DrawTool1 drawtool1 = new DrawTool1();
	private DrawTool2 DrawTool2 = new DrawTool2();
	private DrawTool3 DrawTool3 = new DrawTool3();
	private DrawTool4 DrawTool4 = new DrawTool4();

	private int nowyear;
	private int nowmonth;
	private int prevmonth;
	
	
	

	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object who = e.getSource();

		if (who == btLedgerL) {
			card.first(pMainL); // CardLayout의 이전 내용을 보여준다.

		} else if (who == btTotalL) {
			card.last(pMainL); // CardLayout의 다음 내용을 보여준다.
		} else if (who == bExitL) {

			int y = JOptionPane.showConfirmDialog(this, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);

			if (y == 0) {
				System.exit(0);
			} else if (y == 1) {
				return;
			}

		} else if (who == btUpdateL) {
			if (validCheck() == true) {
				UpdateData();
			}
		} else if (who == btDeleteL) {

			DeleteData();

		} else if (e.getSource() == btSaveL) {
			if (validCheck() == true) {
				InsertData();
			}
		} else if (e.getSource() == btSearchL) {
			if (validCheck1() == true) {
				GetListMonth();
				// System.out.println(year);
			}
		}
		else if (who == btGraph1) {

			DrawGraph dg = new DrawGraph("수입 총합 그래프");

		}
		else if (who == btGraph2) {

			DrawGraph1 dg1 = new DrawGraph1("지출 총합 그래프");

		}
		

	}

	// 선택한 달 조회 메소드
	private void GetListMonth() {

		LedgerDao dao = new LedgerDao();

		List<ListTable> lists = dao.GetListMonth(year, month);
		
		dtm.DeleteAll();
		for (ListTable oneitem : lists) { 
			dtm.InsertListTable(oneitem); //코딩할것
		}
	}

	private void UpdateData() { // 수정 매소드

		int cnt = -99999;

		ListTable list = new ListTable();

		int rowsu = table.getSelectedRow() ;
		int no = Integer.parseInt( table.getValueAt(rowsu, 0).toString() )  ;
		int pay = jComPayL.getSelectedIndex() ;
		list.setNo(no);
		list.setPay( pay );
		list.setToday(jTfL1.getText());
		list.setMemo(jTfL2.getText());
		list.setPrice(Integer.valueOf(jTfL3.getText()));

		cnt = dao.UpdateData(list);

		dtm.UpdateListTable( list ) ;
		
		JOptionPane.showMessageDialog(this, "수정되었습니다", "수정", JOptionPane.INFORMATION_MESSAGE);

		btSaveL.setEnabled(true);
		jTfL1.setText("");
		jTfL2.setText("");
		jTfL3.setText("");
		btUpdateL.setEnabled(false);
		btDeleteL.setEnabled(false);

	}

	private void DeleteData() { // 삭제 매소드

		int myrow = table.getSelectedRow();
		if (myrow == -1) {
			return;
		}
		//System.out.println(table.getValueAt(myrow, 0));
		int no = Integer.valueOf(table.getValueAt(myrow, 0).toString());

		LedgerDao dao = new LedgerDao();
		int cnt = -99999;

		cnt = dao.DeleteData(no);

		if (cnt > 0) {

			dtm.DeleteListTable(no);
			table.repaint();
			JOptionPane.showMessageDialog(this, "삭제되었습니다", "삭제", JOptionPane.INFORMATION_MESSAGE);
			
		} 

		btSaveL.setEnabled(true);
		jTfL1.setText("");
		jTfL2.setText("");
		jTfL3.setText("");
		btUpdateL.setEnabled(false);
		btDeleteL.setEnabled(false);

		//dtm.removeRow(myrow);// 행을 삭제 한다  //코딩할것

	}

	private void InsertData() { // 저장 메소드

		int cnt = -99999;

		ListTable list = new ListTable();

		list.setPay(pay);
		list.setToday(jTfL1.getText());
		list.setMemo(jTfL2.getText());
		list.setPrice(Integer.valueOf(jTfL3.getText()));
		// list.setBalance();

		cnt = dao.InsertData(list);

		if (cnt == 1) {
			// System.out.println("성공");

//			Object[] newarr = new Object[6]; // 테이블에 추가
//			newarr[1] = jTfL1.getText();
//			newarr[2] = jTfL2.getText();
//			newarr[3] = jTfL3.getText();

			//dtm.addRow(newarr);// 행을 추가한다
			dtm.InsertListTable( list ); 
			JOptionPane.showMessageDialog(this, "저장되었습니다", "저장", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	class CardLayout1 extends JPanel {

		public CardLayout1() {

			btUpdateL = new JButton("수정");
			btDeleteL = new JButton("삭제");
			btSaveL = new JButton("저장");

			btSearchL = new JButton("조회");

			dtm = new MyTableModel();

			table = new JTable(dtm);
			jspane = new JScrollPane(table);

			// 현재 시간 구하기
			Calendar oCalendar = Calendar.getInstance();
			String day;
			day = oCalendar.get(Calendar.YEAR) + "년 " + (oCalendar.get(Calendar.MONTH) + 1) + "월 "
					+ oCalendar.get(Calendar.DAY_OF_MONTH) + "일";
			
			imsiData = dao.GetLedgerList();

			for (ListTable oneitem : imsiData) {
				// Vector<String> vec2 = new Vector<String>() ;
//				Object[] newarr = new Object[oneitem.size()];
//				for (int i = 0; i < oneitem.size(); i++) {
				//int pay = oneitem.getPay() ;
				//newarr[2] = pay ==  ? "수입" : "지출";
/*					if (pay == 2) {
						// i==2 pay 분기처리
						String imsi = oneitem.get(i); // 1 , 2
						newarr[2] = (imsi.equals("1")) ? "수입" : "지출";
					} else {
						newarr[i] = oneitem.get(i);
					}*/
				//}
				dtm.InsertListTable(oneitem); //코딩할것
			}

			pTopL = new JPanel();
			pTopL1 = new JPanel();
			pTopL2 = new JPanel();
			pCenterL = new JPanel();
			pLeftL = new JPanel();
			pRightL = new JPanel();
			pRightL1 = new JPanel();
			pRightL2 = new JPanel();

			jTfL1 = new JTextField();
			jTfL2 = new JTextField();
			jTfL3 = new JTextField();

			jComPayL = new JComboBox<>();
			jComyearL = new JComboBox<>();
			jCommonthL = new JComboBox<>();

			jLabelL1 = new JLabel("날짜");
			jLabelL2 = new JLabel("내역");
			jLabelL3 = new JLabel("가격");
			jLabelL4 = new JLabel("오늘");
			jLabelL5 = new JLabel(day);

			jComPayL.addItem("선택");
			jComPayL.addItem("수입");
			jComPayL.addItem("지출");

			jComyearL.addItem("년");

			for (int i = 2010; i < 2030; i++) {

				jComyearL.addItem(String.valueOf(i) + "년");
				
			}

			jCommonthL.setModel(new DefaultComboBoxModel(
					new String[] { "월", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" }));

			dtm = new MyTableModel();
			imsiData = dao.GetLedgerList();

			TableCellRenderer renderer = new TableCellRenderer();

			table.setDefaultRenderer(table.getColumnClass(0), renderer);//setDefaultRenderer가 뭔지 찾아볼것!

			/////////////////////////////////////////////////////////////
			super.setLayout(new FlowLayout());
			pTopL.setLayout(null);
			pLeftL.setLayout(null);
			pRightL.setLayout(null);
			pRightL1.setLayout(null);
			pRightL2.setLayout(null);
			pCenterL.setLayout(null);
			pTopL1.setLayout(null);
			pTopL2.setLayout(null);
			jTfL1.setLayout(null);
			jTfL2.setLayout(null);
			jTfL3.setLayout(null);
			jComPayL.setLayout(null);
			jComyearL.setLayout(null);
			jCommonthL.setLayout(null);
			jLabelL1.setLayout(null);
			jLabelL2.setLayout(null);
			jLabelL3.setLayout(null);
			jLabelL4.setLayout(null);
			jLabelL5.setLayout(null);
			btUpdateL.setLayout(null);
			btDeleteL.setLayout(null);
			btSaveL.setLayout(null);
			btSearchL.setLayout(null);
			drawtool.setLayout(null);
			drawtool1.setLayout(null);
			DrawTool2.setLayout(null);

			super.setLayout(null);

			// 테이블의 칼럼 넓이 변경 
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(-100);
			table.getColumnModel().getColumn(1).setPreferredWidth(210);
			table.getColumnModel().getColumn(2).setPreferredWidth(210);
			table.getColumnModel().getColumn(3).setPreferredWidth(225);
			table.getColumnModel().getColumn(4).setPreferredWidth(220);
			table.getColumnModel().getColumn(5).setPreferredWidth(220);

			pTopL.setBounds(0, 0, 1500, 100);
			pTopL1.setBounds(0, 0, 200, 100);
			pTopL2.setBounds(450, 0, 600, 100);
			pCenterL.setBounds(0, 100, 1500, 700);
			pLeftL.setBounds(0, 0, 1100, 700);
			pRightL.setBounds(1100, 0, 400, 700);
			pRightL1.setBounds(15, 70, 350, 100);
			pRightL2.setBounds(15, 250, 350, 100);

			jspane.setBounds(0, 80, pLeftL.getWidth(), pLeftL.getHeight()); // jspane 의 넓이 높이를 판낼 끝까지 키움

			jTfL1.setBounds(90, 70, 70, 30);
			jTfL2.setBounds(180, 70, 70, 30);
			jTfL3.setBounds(270, 70, 70, 30);

			jComPayL.setBounds(0, 70, 70, 30);
			jComyearL.setBounds(100, 35, 100, 30);
			jCommonthL.setBounds(250, 35, 100, 30);

			jLabelL1.setBounds(110, 30, 100, 40);
			jLabelL2.setBounds(200, 30, 100, 40);
			jLabelL3.setBounds(290, 30, 200, 40);
			jLabelL4.setBounds(10, 30, 100, 40);
			jLabelL5.setBounds(95, 30, 200, 40);

			btSaveL.setBounds(0, 0, 100, 40);
			btUpdateL.setBounds(120, 0, 100, 40);
			btDeleteL.setBounds(240, 0, 100, 40);
			btSearchL.setBounds(400, 35, 80, 30);

			drawtool.setBounds(1100, 180, 800, 500);
			drawtool1.setBounds(530, 20, 800, 800);
			DrawTool2.setBounds(0, 20, 800, 800);

			pLeftL.add(jspane);

			pCenterL.add(pRightL);
			pCenterL.add(pLeftL);

			pRightL.add(pRightL1);
			pRightL.add(pRightL2);
			pRightL.add(drawtool);

			pRightL1.add(jTfL1);
			pRightL1.add(jTfL2);
			pRightL1.add(jTfL3);
			pRightL1.add(jComPayL);
			pRightL1.add(jLabelL1);
			pRightL1.add(jLabelL2);
			pRightL1.add(jLabelL3);

			pRightL2.add(btSaveL);
			pRightL2.add(btUpdateL);
			pRightL2.add(btDeleteL);

			pTopL2.add(btSearchL);
			pTopL2.add(jComyearL);
			pTopL2.add(jCommonthL);

			pTopL1.add(jLabelL4);
			pTopL1.add(jLabelL5);

			pTopL.add(pTopL1);
			pTopL.add(pTopL2);

			super.add(drawtool);
			super.add(drawtool1);
			super.add(DrawTool2);

			super.add(pTopL, BorderLayout.NORTH);
			super.add(pCenterL, BorderLayout.CENTER);

			// pMainL.setBackground(Color.pink);
			// pTopL.setBackground(Color.YELLOW);
			// pCenterL.setBackground(Color.black);
			// pBottomL.setBackground(Color.gray);
			// pLeftL.setBackground(Color.blue);
			// pRightL.setBackground(Color.red);
			// pRightL1.setBackground(Color.red);
			// pRightL2.setBackground(Color.gray);

			btUpdateL.setEnabled(false);
			btDeleteL.setEnabled(false);

		}
	}

	class CardLayout2 extends JPanel {
		public CardLayout2() {
			jTaT1 = new JTextArea();
			jTaT2 = new JTextArea();			
			
			Calendar oCalendar = Calendar.getInstance();
			nowyear = oCalendar.get(Calendar.YEAR);
			prevmonth = oCalendar.get(Calendar.MONTH) ; //이전달
			nowmonth = oCalendar.get(Calendar.MONTH) + 1 ;//금월

			LedgerDao dao = new LedgerDao();
			
			List<ListTable> list1 = dao.GetListNowMonth(nowyear, prevmonth);
			String imsi = "" ;
			for(ListTable oneitem : list1){				
				imsi += oneitem.getPay() == 1 ? "저번달 수입 총합 : " : "저번달 지출 총합 : " ;
				imsi += oneitem.getPrice() +"원" +"\n"+"\n" ;
			}
			jTaT1.setText("\n"+"\n"+"\n"+"\n"+"\n"+"\n"+imsi);
			jTaT1.setFont(new Font("고딕", Font.BOLD, 20));
			jTaT1.setForeground(Color.blue);
			
			
			
			imsi = ""; 
			
			List<ListTable> list2 = dao.GetListNowMonth(nowyear, nowmonth);
			for(ListTable oneitem : list2){
				imsi += oneitem.getPay() == 1 ? "이번달 수입 총합 : ": "이번달 지출 총합 : " ;
				imsi += oneitem.getPrice() +"원" + "\n" +"\n" ;
				//System.out.println(oneitem);
			}
			jTaT2.setText("\n"+"\n"+"\n"+"\n"+"\n"+"\n"+imsi);
			jTaT2.setFont(new Font("고딕", Font.BOLD, 20));
			jTaT2.setForeground(Color.red);
			
			
			pMainT = new JPanel();

			pLeftT = new JPanel();
			pCenterT = new JPanel();
			pRightT = new JPanel();
			pCenterTG = new JPanel();
			pCenterTG1 = new JPanel();

			jLabelT1 = new JLabel("저번달 수입,지출 통계");
			jLabelT1.setForeground(Color.BLUE);
			jLabelT2 = new JLabel("이번달 수입,지출 통계");
			jLabelT2.setForeground(Color.red);

			jLabelT3 = new JLabel();
			
			btGraph1 = new JButton("수입 통계 그래프");
			btGraph2 = new JButton("지출 통계 그래프");
			


			super.setLayout(null);
			pMainT.setLayout(null);
			pLeftT.setLayout(null);
			pCenterT.setLayout(null);
			pRightT.setLayout(null);
			pCenterTG.setLayout(null);
			pCenterTG1.setLayout(null);
			btGraph2.setLayout(null);
			btGraph1.setLayout(null);
		

			DrawTool3.setLayout(null);
			DrawTool4.setLayout(null);

			pMainT.add(pLeftT);
			pMainT.add(pCenterT);
			pMainT.add(pRightT);

			pLeftT.add(jLabelT1);
			pLeftT.add(jTaT1);
			pRightT.add(DrawTool4);

			pRightT.add(jLabelT2);
			pRightT.add(jTaT2);
			pLeftT.add(DrawTool3);
			
			pCenterT.add(pCenterTG);
			pCenterT.add(pCenterTG1);
			
			pCenterTG.add(btGraph1);
			pCenterTG1.add(btGraph2);

			pMainT.setBounds(0, 0, 1500, 800);
			pLeftT.setBounds(0, 0, 500, 800);
			pCenterT.setBounds(500, 0, 500, 800);
			pRightT.setBounds(1000, 0, 500, 800);
			
			pCenterTG1.setBounds(0, 0, 500, 400);
			pCenterTG.setBounds(0, 400, 500, 400);

			jLabelT1.setBounds(140, 50, 300, 100);
			jTaT1.setBounds(50, 220, 400, 450);
			jLabelT1.setFont(new Font("고딕", Font.BOLD, 20));

			jLabelT2.setBounds(145, 50, 300, 100);
			jTaT2.setBounds(40, 220, 400, 450);
			jLabelT2.setFont(new Font("고딕", Font.BOLD, 20));

			DrawTool3.setBounds(0, 0, 600, 800);
			DrawTool4.setBounds(0, 0, 15000, 15000);
			
			btGraph1.setBounds(100, 0, 300, 100);
			btGraph2.setBounds(100, 300, 300, 100);

			super.add(DrawTool3);
			super.add(DrawTool4);
			super.add(pMainT, BorderLayout.CENTER);
			
		

			jTaT1.setEditable(false);
			jTaT2.setEditable(false);

			//pMainT.setBackground(Color.black);
			// pLeftT.setBackground(Color.BLUE);
			// pCenterT.setBackground(Color.pink);
			// pRightT.setBackground(Color.red);
			//pCenterTG1.setBackground(Color.pink);
			//pCenterTG.setBackground(Color.red);

			//super.setBackground(Color.YELLOW);
		

		}
	}

	private void compose() {
		Container con = super.getContentPane();
		// 컴포넌트, 배치 관리자, 컨테이너 변수에 대한 객체 생성

		card = new CardLayout();
		setLayout(card);

		pMainL = new JPanel();
		pMainT = new JPanel();

		btLedgerL = new JButton("가계부");
		btTotalL = new JButton("통계");
		bExitL = new JButton("종료");

		Container contain = super.getContentPane();
		// 오류 발생
		// table.setDefaultRenderer(table.getColumnClass(0), renderer);

		pBottomL = new JPanel();

		card = new CardLayout();

		// 배치 관리자 설정 : 컨테이너.setLayout( 배치관리자객체 );

		super.setLayout(new BorderLayout());

		pMainL.setLayout(card);
		//
		pBottomL.setLayout(new GridLayout(1, 3));

		//

		pBottomL.add(btLedgerL);
		pBottomL.add(btTotalL);
		pBottomL.add(bExitL);

		pMainL.add(new CardLayout1());
		pMainL.add(new CardLayout2());

		contain.add(pMainL, BorderLayout.CENTER);
		contain.add(pBottomL, BorderLayout.SOUTH);

	}

	private void setevent() {
		// 이벤트 처리, 이벤트 객체 등록
		btLedgerL.addActionListener(this);

		btTotalL.addActionListener(this);

		bExitL.addActionListener(this);

		btSaveL.addActionListener(this);

		btUpdateL.addActionListener(this);

		btDeleteL.addActionListener(this);

		btSearchL.addActionListener(this);
		
		btGraph1.addActionListener(this);
		
		btGraph2.addActionListener(this);

		jComPayL.addItemListener(this);

		jComyearL.addItemListener(this);

		jCommonthL.addItemListener(this);

		table.addMouseListener(new MyMouseEvent());

		// JFrame에 대한 종료 옵션
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public MainPro(String title) {

		super(title);
		super.setBounds(0, 0, 1493, 800);
		// super.setResizable( false );

		this.compose();
		this.setevent();

		super.setResizable(false);
		super.setVisible(true); // 보이기 옵션

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() - super.getWidth()) / 2;
		int ypos = (int) (screen.getHeight() - super.getHeight()) / 2;
		super.setLocation(xpos, ypos); // 프레임의 좌측 상단의 좌표를 지정한다.

	}

	@Override
	public void itemStateChanged(ItemEvent e) {

		JComboBox<String> combo = (JComboBox) e.getSource();
		String strItem = combo.getSelectedItem().toString();
		int idx = combo.getSelectedIndex();

		if (combo == jComPayL) {
			pay = idx;
		} else if (combo == jComyearL) {
			year = Integer.parseInt(strItem.substring(0, 4));
		} else if (combo == jCommonthL) {
			month = idx;
		}

	}

	class MyMouseEvent extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {

			int myrow = table.getSelectedRow();
			if (myrow == -1) {
				return;
			}

			jTfL1.setText(table.getValueAt(myrow, 1).toString());
			jTfL2.setText(table.getValueAt(myrow, 3).toString());
			jTfL3.setText(table.getValueAt(myrow, 4).toString());

			// jComPayL.set

			btSaveL.setEnabled(false); // 버튼 못 누르게 하

			btUpdateL.setEnabled(true);
			btDeleteL.setEnabled(true);

			if ((table.getValueAt(myrow, 2).toString()).equals("수입")) {

				jComPayL.setSelectedIndex(1);

			} else if ((table.getValueAt(myrow, 2).toString()).equals("지출")) {

				jComPayL.setSelectedIndex(2);

			}

		}

	}

	// 유효성 검사!!!!!
	public boolean validCheck() {
		// 입력 항목들에 대하여 유효성 검사를 수행한다.

		if (jTfL1.getText().length() == 0) { // jtf1에 택스트 길이가 없을때
			// 경고 박스
			JOptionPane.showMessageDialog(this, "날짜가 누락되었습니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (jTfL2.getText().length() == 0) { // jtf1에 택스트 길이가 없을때
			// 경고 박스
			JOptionPane.showMessageDialog(this, "내역이 누락되었습니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if (jTfL3.getText().length() == 0) { // jtf1에 택스트 길이가 없을때
			// 경고 박스
			JOptionPane.showMessageDialog(this, "가격이 누락되었습니다.", "오류", JOptionPane.INFORMATION_MESSAGE);

			return false;
		}
		if (pay == 0) {
			JOptionPane.showMessageDialog(this, "수입 지출을 선택하세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// 페턴 넣어주기

		String pattern = "\\d{4}\\/\\d{2}\\/\\d{2}";
		String birth = jTfL1.getText();
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(birth);

		if (m.matches() == false) {
			JOptionPane.showMessageDialog(this, "올바른 날짜 형식이 아닙니다.(예시 : 2016/12/25)", "오류",
					JOptionPane.INFORMATION_MESSAGE);
			return false;

		}

		pattern = "\\d{1,}";
		birth = jTfL3.getText();
		p = Pattern.compile(pattern);
		m = p.matcher(birth);

		if (m.matches() == false) {
			JOptionPane.showMessageDialog(this, "가격에 숫자를 넣어주세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;

		// j프레임 마지막 단

	}

	public boolean validCheck1() {

		if (year == 0) {
			JOptionPane.showMessageDialog(this, "년을 선택하세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		if (month == 0) {
			JOptionPane.showMessageDialog(this, "달을 선택하세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;

	}

	// 테이블 커스텀 할 수 있게 해주는 것
	class MyTableModel extends AbstractTableModel {
		String[] col_name = { "번호", "날짜", "수입,지출", "내역", "가격", "잔액" };

		//lists : 요소들을 담고 있는 컬렉션
		List<ListTable> lists = new ArrayList<ListTable>();

		@Override
		public int getColumnCount() {// 컬럼의 갯수
			return col_name.length;
		}

		public void DeleteAll() {//현재 컬렉션에  들어 있는 모드 요소를 제거
			 lists.clear();
		}

		@Override
		public int getRowCount() {// 행 갯수
			return lists.size() ;
		}

		public void InsertListTable( ListTable item ){		
			lists.add(item) ;
			fireTableRowsInserted( getRowCount() - 1, getRowCount() - 1);
		}
		
		public void UpdateListTable( ListTable item ){
			//System.out.println("수정해야지");
			for (int i = 0; i < lists.size(); i++) {		
				if ( lists.get( i ).getNo() == item.getNo() ) {
					lists.remove(i) ;
					lists.add(i, item);				
					break; 
				}
			}
			fireTableRowsUpdated( getRowCount() - 1, getRowCount() - 1);
			table.repaint();
		}
		
		public void DeleteListTable( int no ){
			for (int i = 0; i < lists.size(); i++) {		
				if ( lists.get( i ).getNo() == no ) {
					lists.remove( i ) ;				
					break; 
				}
			}
			fireTableRowsDeleted( getRowCount() - 1, getRowCount() - 1);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {// 해당 셀의 값
			ListTable oneitem = lists.get(rowIndex) ;
			switch (columnIndex) {
			case 0:
				return oneitem.getNo() ;
			case 1: 
				return oneitem.getToday() ;
			case 2:
				return oneitem.getPay() == 1 ? "수입" : "지출" ;
			case 3:
				return oneitem.getMemo();
			case 4:
				return oneitem.getPrice() ;
			case 5:
				return oneitem.getBalance() ;
			}
			return "";
		}

		@Override
		public String getColumnName(int column) {// 해당 커럼의 이름
			return col_name[column];
		}

	}	
	
	// j프레임 마지막 단

}

public class MainProgarm {
	public static void main(String[] args) {
		new MainPro("가계부");
	}
}

// 이너 클래스 -------------------------------------

class DrawTool extends JPanel {

	public DrawTool() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(0, 0, 375, 250);

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, 375, 130);

	}
}

class DrawTool1 extends JPanel {

	public DrawTool1() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(0, 0, 418, 60);

	}
}

class DrawTool2 extends JPanel {

	public DrawTool2() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(0, 0, 200, 60);

	}
}

class DrawTool3 extends JPanel {

	public DrawTool3() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(100, 50, 290, 100);

		g.setColor(Color.black);
		g.drawRect(45, 215, 408, 460);

	}
}

class DrawTool4 extends JPanel {

	public DrawTool4() {

	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(1100, 50, 290, 100);

		g.setColor(Color.black);
		g.drawRect(1035, 215, 408, 460);

	}
}

class TableCellRenderer extends DefaultTableCellRenderer {

	@Override // 선택되지 않는 행에 대한 색상 지정
	public void setBackground(Color c) {
		super.setBackground(Color.black);
	}

	@Override // 전경색
	public void setForeground(Color c) {
		super.setForeground(Color.white);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (column == 0 || column == 1 || column == 2 || column == 3) {
			// 가운데 정렬
			setHorizontalAlignment(SwingConstants.CENTER);
		} else if (column == 4 || column == 5) {
			setHorizontalAlignment(SwingConstants.RIGHT);
			if (column == 4 || column == 5) {
				String pattern = "###,###원";
				DecimalFormat df = new DecimalFormat(pattern);
				int val = Integer.valueOf(value.toString());
				setText(df.format(val));
			}
		}
		return this;
	}

}

//class drawg extends Applet{
//	
//	double datas[] = { 
//	
//}
