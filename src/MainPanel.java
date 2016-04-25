import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import AgentPanel.Agent;
import AgentPanel.MultiAgentSimulation2;
import PsoPanel.PsoSimulation;
import SuperPack.Result;
import SuperPack.SimulationPanel;

/**
 * ターゲットの捕獲を繰り返し行うSWINGアプリケーション
 * シミュレーターのIOをプラスしたもの
 * utf-8
 * @author Watari
 * 
 */
public class MainPanel extends JFrame implements ActionListener, Runnable {
	Thread thread = null;
	static JLabel label;
	private boolean runFlag = false;
	private SimulationPanel canvas;
	private SimulationPanel canvas4;

	public static void main(String args[]) {
		MainPanel game = new MainPanel();
	}

	public MainPanel() {
		setTitle("PSOagent Simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		this.setSize(650, 650);
		
		/* FIXME テキストエリア
		ta = new JTextArea(18, 40);
		ta.setLineWrap(true);
		ta.setEditable(false);
		*/
		label = new JLabel();

		JPanel mainp = (JPanel) getContentPane();
		JPanel sp = new JPanel();
		JPanel ep = new JPanel();
		JPanel enp = new JPanel();
		JPanel esp = new JPanel();

		// シミュレータの初期化
		canvas = new PsoSimulation();
		canvas4 = new MultiAgentSimulation2(1);
		canvas4.copy(canvas);

		GridLayout gl = new GridLayout(1, 2);

		// 各ボタン
		JButton logb = new JButton("START");
		logb.addActionListener(this);
		logb.setActionCommand("START");

		JButton stepb = new JButton("STEP");
		stepb.addActionListener(this);
		stepb.setActionCommand("STEP");

		JButton resetb = new JButton("RESET");
		resetb.addActionListener(this);
		resetb.setActionCommand("RESET");

		JButton outputb = new JButton("OutPut");
		outputb.addActionListener(this);
		outputb.setActionCommand("OutPut");

		JButton psoFileOutPutB = new JButton("psoFileOutPut");
		psoFileOutPutB.addActionListener(this);
		psoFileOutPutB.setActionCommand("psoFileOutPut");
		
		JButton cmaFileOutPutB = new JButton("cmaFileOutPut");
		cmaFileOutPutB.addActionListener(this);
		cmaFileOutPutB.setActionCommand("cmaFileOutPut");
		
		JButton psoFileInPutB = new JButton("psoFileInPut");
		psoFileInPutB.addActionListener(this);
		psoFileInPutB.setActionCommand("psoFileInPut");
		
		JButton cmaFileInPutB = new JButton("cmaFileInPut");
		cmaFileInPutB.addActionListener(this);
		cmaFileInPutB.setActionCommand("cmaFileInPut");
		
		JButton psoFailOutPutB = new JButton("psoFailOutPut");
		psoFailOutPutB.addActionListener(this);
		psoFailOutPutB.setActionCommand("psoFailOutPut");
		
		JButton cmaFailOutPutB = new JButton("cmaFailOutPut");
		cmaFailOutPutB.addActionListener(this);
		cmaFailOutPutB.setActionCommand("cmaFailOutPut");

		// パネル
		gl.setHgap(5);
		mainp.setLayout(gl);
		
		
		sp.setLayout(new FlowLayout((FlowLayout.LEFT)));

		sp.add(logb);
		sp.add(stepb);
		sp.add(resetb);
		sp.add(outputb);
		sp.add(psoFileOutPutB);
		sp.add(cmaFileOutPutB);
		sp.add(psoFailOutPutB);
		sp.add(cmaFailOutPutB);
		sp.add(psoFileInPutB);
		sp.add(cmaFileInPutB);

		enp.setLayout(new BorderLayout());
		enp.add(canvas, BorderLayout.CENTER);

		esp.setLayout(new BorderLayout());
		esp.add(canvas4, BorderLayout.CENTER);

		GridLayout egl = new GridLayout(2, 1);
		ep.setLayout(egl);
		ep.add(enp);
		ep.add(esp);

		mainp.add(ep);
		mainp.add(sp);
		this.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (true) {
			if (runFlag) {
				if (canvas.step()) {
					System.out.println("PsoHuntTarget"+canvas.huntedTarget+"\n");
				}
				if (canvas4.step()) {
					System.out.println("CmaHuntTarget"+canvas4.huntedTarget+"\n");
				}

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			repaint();
		}
	}

	/**
	 * 詳細な結果を外部に出力する
	 * @param fw
	 * @param results
	 */
	private void outputCsv(FileWriter fw, Result[] results) {
		//結果を出力
		try {
			int huntCount = 0;
			int target=0;
			fw.write(",step,communication,distance,density\r\n");
			for (Result result : results) {
				target++;
				if (result != null) {
					huntCount++;
					fw.write("target"+target+","+result + "\r\n");
				} else {
					fw.write("NoHunt\r\n");
				}
			} 
			fw.write("HuntedTarget,"+huntCount+"\r\n");
		}catch (IOException e) {
			System.out.println("ファイルを閉じてどうぞ"+e);
		}
		
	}

	private void Reset() {
		canvas.reset();
		canvas4.reset();

		canvas4.copy(canvas);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("START")) {
			if (!runFlag) {
				runFlag = true;
			}
		}

		if (command.equals("STEP")) {
			canvas.step();
			canvas4.step();
			repaint();
		}

		if (command.equals("RESET")) {
			runFlag = false;
			Reset();
		}

		if (command.equals("OutPut")) {
			FileWriter psoFW, cmaFW;
			int num = SimulationPanel.num;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

			File psoFile = new File("PSOresult" + sdf.format(Calendar.getInstance().getTime()) + ".csv");
			File cmaFile = new File("CMAresult" + sdf.format(Calendar.getInstance().getTime()) + ".csv");

			canvas.setRobotNum(num);
			canvas4.setRobotNum(num);
			Reset();

			for (int count = 0; count < 100; count++) {
				try {
					psoFW = new FileWriter(psoFile, true);
					cmaFW = new FileWriter(cmaFile, true);

					Result[] psoResults = null;
					Result[] cmaResults = null;

					for (int i = 0; i < 1; i++) {
						psoResults = canvas.setLog();
						cmaResults = canvas4.setLog();
						Reset();
					}
					outputCsv(psoFW, psoResults);
					outputCsv(cmaFW, cmaResults);

					SimulationPanel.num = num;
					Reset();
					cmaFW.close();
					psoFW.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			System.out.println("ターゲットの捕獲終了");
			System.out.println("PSO　出力:"+psoFile.getAbsolutePath());
			System.out.println("CMA　出力"+cmaFile.getAbsolutePath());
		}
		
		if (command.equals("psoFileOutPut")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			File outFile = new File("pso"+sdf.format(Calendar.getInstance().getTime())+".txt");
			try {
				FileWriter outFileWriter = new FileWriter(outFile);
				outFileWriter.write(canvas.toString());
				System.out.println(canvas);
				outFileWriter.close();
			} catch (IOException e1) {
				System.out.println(MessageFormat.format("{0}が書き込めません。{1}", outFile, e1.getMessage()));
			}
		}
		
		if (command.equals("cmaFileOutPut")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			File outFile = new File("cma"+sdf.format(Calendar.getInstance().getTime())+".txt");
			try {
				FileWriter outFileWriter = new FileWriter(outFile);
				outFileWriter.write(canvas4.toString());
				System.out.println(canvas4);
				outFileWriter.close();
			} catch (IOException e1) {
				System.out.println(MessageFormat.format("{0}が書き込めません。{1}", outFile, e1.getMessage()));
			}
		}

		if (command.equals("psoFailOutPut")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			File outFile = new File("FailPso"
					+ sdf.format(Calendar.getInstance().getTime()) + ".txt");
			try {
				FileWriter outFileWriter = new FileWriter(outFile);
				String failStr = canvas.getFailData();
				outFileWriter.write(failStr);
				System.out.println(failStr);
				outFileWriter.close();
			} catch (IOException e1) {
				System.out.println(outFile + "が書き込めません。" + e1.getMessage());
			}
		}

		if (command.equals("cmaFailOutPut")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			File outFile = new File("FailCma"
					+ sdf.format(Calendar.getInstance().getTime()) + ".txt");
			try {
				FileWriter outFileWriter = new FileWriter(outFile);
				String failStr = canvas4.getFailData();
				outFileWriter.write(failStr);
				System.out.println(failStr);;
				outFileWriter.close();
			} catch (IOException e1) {
				System.out.println(outFile + "が書き込めません。" + e1.getMessage());
			}
		}

		if (command.equals("psoFileInPut")) {
			JFileChooser filechooser = new JFileChooser(new File("./"));

			int selected = filechooser.showOpenDialog(this);
			String str = "";

			if (selected == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(file));
					str = br.readLine();
					br.close();
				} catch (IOException e1) {
					System.out.println("ファイルが読み込めません" + e1.getMessage());
				}
			}

			System.out.println(str);
			canvas.readFile(str);
		}

		if (command.equals("cmaFileInPut")) {
			JFileChooser filechooser = new JFileChooser(new File("./"));

			String str = "";

			int selected = filechooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(file));
					str = br.readLine();
					br.close();
				} catch (IOException e1) {
					System.out.println("ファイルが読み込めません。" + e1.getMessage());
				}
			}
			System.out.println(str);
			canvas4.readFile(str);
		}
	}
}