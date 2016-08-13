import FipsAgentPanel.FipsAgentSimulation;
import PsoPanel.PsoSimulation;
import SuperPack.Result;
import SuperPack.SimulationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * ターゲットの捕獲を繰り返し行うSWINGアプリケーション
 * シミュレーターのIOをプラスしたもの
 * utf-8
 *
 * @author Watari
 */
class MainPanel extends JFrame implements ActionListener, Runnable {
    private Thread thread = null;
    private boolean runFlag = false;
    private SimulationPanel canvas;
    private SimulationPanel canvas4;

    private final static String DIR = "bin";
    private final static String EVAL_DIR = DIR + "\\results\\";
    private final static String FAIL_DIR = DIR + "\\fails\\";
    private final static String OUTPUT_DIR = DIR + "\\outputs\\";


    public static void main(String args[]) {
        MainPanel game = new MainPanel();
    }

    private MainPanel() {
        setTitle("PSOagent Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.setSize(650, 650);
        /* FIXME テキストエリア
        ta = new JTextArea(18, 40);
		ta.setLineWrap(true);
		ta.setEditable(false);
		*/

        JPanel mainp = (JPanel) getContentPane();
        JPanel sp = new JPanel();
        JPanel ep = new JPanel();
        JPanel enp = new JPanel();
        JPanel esp = new JPanel();

        // シミュレータの初期化
        canvas = new PsoSimulation();
        canvas4 = new FipsAgentSimulation(20);
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

        JButton evaluation = new JButton("Eval");
        evaluation.addActionListener(this);
        evaluation.setActionCommand("Evaluation");

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
        sp.add(evaluation);
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
                if (!canvas.isEnd())
                    canvas.step();
                if (!canvas4.isEnd())
                    canvas4.step();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.printf(e.getMessage());
                }
            }
            repaint();
        }
    }

    /**
     * 詳細な結果を外部に出力する
     *
     * @param fw      ファイルライタ
     * @param resultList 結果の配列
     */
    private void outputCsv(FileWriter fw, ArrayList<Result> resultList) {
        //結果を出力
        try {
            int huntCount = 0;
            int target = 0;
            fw.write(",step,communication,distance,density,huntedTarget\r\n");
            for (Result result : resultList) {
                target++;
                if (result != null) {
                    huntCount++;
                    fw.write("target" + result + "\r\n");
                } else {
                    fw.write("NoHunt\r\n");
                }
            }
            fw.write("HuntedTarget," + huntCount + "\r\n");
        } catch (IOException e) {
            System.out.println("ファイルを閉じてどうぞ" + e);
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

        if (command.equals("Evaluation")) {
            FileWriter psoFW, cmaFW;
            int num = SimulationPanel.robotsNum;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

            File psoFile = new File(EVAL_DIR + "PSOresult" + sdf.format(Calendar.getInstance().getTime()) + ".csv");
            File cmaFile = new File(EVAL_DIR + "CMAresult" + sdf.format(Calendar.getInstance().getTime()) + ".csv");

            canvas.setRobotNum(num);
            canvas4.setRobotNum(num);
            Reset();
            System.out.println("EvalStart");
            for (int count = 0; count < 100; count++) {
                try {
                    System.out.println(count + "/100");
                    psoFW = new FileWriter(psoFile, true);
                    cmaFW = new FileWriter(cmaFile, true);
                    ArrayList<Result> psoResults = canvas.evaluation();
                    ArrayList<Result> cmaResults = canvas4.evaluation();
                    Reset();

                    outputCsv(psoFW, psoResults);
                    outputCsv(cmaFW, cmaResults);

                    SimulationPanel.robotsNum = num;
                    Reset();

                    cmaFW.close();
                    psoFW.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("EvalEnd");
            System.out.println("PSO　出力:" + psoFile.getAbsolutePath());
            System.out.println("CMA　出力" + cmaFile.getAbsolutePath());
        }

        if (command.equals("psoFileOutPut")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            File outFile = new File(OUTPUT_DIR + "pso" + sdf.format(Calendar.getInstance().getTime()) + ".txt");
            try {
                FileWriter outFileWriter = new FileWriter(outFile);
                outFileWriter.write(canvas.toString());
                System.out.println(canvas);
                outFileWriter.close();
            } catch (IOException e1) {
                System.out.println(MessageFormat.format("{0}が書き込めません。{1}", outFile, e1.getMessage()));
            }
        }

        if (command.equals("cmaFileOutPut")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            File outFile = new File(OUTPUT_DIR + "cma" + sdf.format(Calendar.getInstance().getTime()) + ".txt");
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
            File outFile = new File(FAIL_DIR + "FailPso"
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
            File outFile = new File(FAIL_DIR + "FailCma"
                    + sdf.format(Calendar.getInstance().getTime()) + ".txt");
            try {
                FileWriter outFileWriter = new FileWriter(outFile);
                String failStr = canvas4.getFailData();
                outFileWriter.write(failStr);
                System.out.println(failStr);
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