package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Project.DrawGraph1.DrawG1;
public class DrawGraph extends JFrame {

	private int nowyear;
	private int nowmonth;
	private int prevmonth;

	private int nowmonthpay;
	private int nowmonthexpense;
	private int lastmonthpay;
	private int lastmonthexpense;

	private int sumpercent[] = { 0, 0 };
	private int sumangle[] = { 0, 0 };
	double total;

	private int arcanglecount = 0;

	Color color[];
	String str[] = { "", "" };

	private int max_index = 0;
	private int max_start_angle;
	
	
public class DrawG extends JPanel {

	
	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < 2; i++) {
			
			g.setColor(color[i]);
			g.fillArc(50, 30, 150, 150, arcanglecount, sumangle[i]);
			g.setColor(Color.black);
			g.drawArc(50, 30, 150, 150, arcanglecount, sumangle[i]);

			g2.setColor(color[i]);
			g2.fillArc(50, 30, 150, 150, arcanglecount, sumangle[i]);
			g2.setColor(Color.black);
			g2.drawArc(50, 30, 150, 150, arcanglecount, sumangle[i]);

			if (sumangle[i] > sumangle[max_index]) {
				max_start_angle = arcanglecount;
				max_index = i;
			}

			arcanglecount = arcanglecount + sumangle[i];
			g2.setColor(Color.black);
			g2.drawString(str[i], (int) (Math.cos((arcanglecount - sumangle[i] / 2) * Math.PI / 180) * 40) + 110,
					(int) (-Math.sin((arcanglecount - sumangle[i] / 2) * Math.PI / 180) * 30) + 90);

		}
		arcanglecount = 0;

	}

	public DrawG() { // 생성자

		int[] sum = new int[2];
		sum[0] = nowmonthpay;
		sum[1] = lastmonthpay;

		color = new Color[2];
		color[0] = new Color(255, 0, 0);
		color[1] = new Color(0, 0, 255);

		total = sum[0] + sum[1];

		sumpercent[0] = (int) (sum[0] / total * 100);
		sumpercent[1] = (int) (sum[1] / total * 100);

		sumangle[0] = (int) (360 * sum[0] / total);
		sumangle[1] = (int) (360 * sum[1] / total);
		
		super.repaint(); 
		
	}
}
	public DrawGraph(String string) {
		

		Calendar oCalendar = Calendar.getInstance();
		nowyear = oCalendar.get(Calendar.YEAR);
		prevmonth = oCalendar.get(Calendar.MONTH); // 이전달
		nowmonth = oCalendar.get(Calendar.MONTH) + 1;// 금월

		LedgerDao dao = new LedgerDao();

		int pay = 1;
		List<ListTable> list3 = dao.GetListNowMonthSum(nowyear, nowmonth, pay);

		for (ListTable oneitem : list3) {
			String imsi1 = "";
			imsi1 += oneitem.getPay();
			imsi1 += oneitem.getPrice();
			// System.out.println(oneitem);

			nowmonthpay = oneitem.getPrice(); // 이번달 수입 총합

		}

		pay = 1;
		List<ListTable> list5 = dao.GetListNowMonthSum(nowyear, prevmonth, pay);

		for (ListTable oneitem : list5) {
			String imsi1 = "";
			imsi1 += oneitem.getPay();
			imsi1 += oneitem.getPrice();
			// System.out.println(oneitem);

			lastmonthpay = oneitem.getPrice(); // 저번달 수입 총합
			// System.out.println(lastmonthpay);

		}
	
		DrawG drawg = new DrawG();
	
	super.setVisible(true);
	super.setBounds(675, 350, 260, 260);
	super.setResizable(false);
	super.setTitle(string);
	
	super.setLayout(new BorderLayout());
	super.add(drawg);
	}	
	

	public static void main(String[] args) {
		new DrawGraph("수입 총합 그래프");
	}
}
	

