import AfsPanel.AfsSimulation;
import AgentPanel.MultiAgentSimulation2;
import ArpsoPanal.ArpsoSimulation;
import SuperPack.Panel.Result;
import SuperPack.Panel.SimulationPanel;

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

    JComboBox debugCombo1;
    JComboBox debugCombo2;


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
        canvas = new MultiAgentSimulation2(30);
        canvas4 = new ArpsoSimulation(30);
        canvas4.copy(canvas);

        GridLayout gl = new GridLayout(1, 2);

        // 各ボタン
        JButton logb = new JButton("START");
        logb.addActionListener(this);
        logb.setActionCommand("START");

        JButton stepb = new JButton("STEP");
        stepb.addActionListener(this);
        stepb.setActionCommand("STEP");

        JButton stopb = new JButton("STOP");
        stopb.addActionListener(this);
        stopb.setActionCommand("STOP");

        JButton resetb = new JButton("RESET");
        resetb.addActionListener(this);
        resetb.setActionCommand("RESET");

        JButton evaluation = new JButton("Eval");
        evaluation.addActionListener(this);
        evaluation.setActionCommand("Bat/Evaluation");

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

        JButton readEnemyB = new JButton("inputEnemy");
        readEnemyB.addActionListener(this);
        readEnemyB.setActionCommand("inputEnemy");

        JButton readRobotB = new JButton("inputRobot");
        readRobotB.addActionListener(this);
        readRobotB.setActionCommand("inputRobot");

        String[] canvasRobotIds = new String[canvas.robot.length + 1];
        canvasRobotIds[0] = "NONE";
        for (int i = 0; i < canvasRobotIds.length - 1; i++) {
            canvasRobotIds[i + 1] = canvas.robot[i].id + "";
        }

        debugCombo1 = new JComboBox(canvasRobotIds);
        debugCombo1.addActionListener(this);
        debugCombo1.setActionCommand("debugCanvas");

        String[] canvas2RobotIds = new String[canvas4.robot.length + 1];
        canvas2RobotIds[0] = "NONE";
        for (int i = 0; i < canvas2RobotIds.length - 1; i++) {
            canvas2RobotIds[i + 1] = canvas4.robot[i].id + "";
        }
        debugCombo2 = new JComboBox(canvas2RobotIds);
        debugCombo2.addActionListener(this);
        debugCombo2.setActionCommand("debugCanvas2");

        // パネル
        gl.setHgap(5);
        mainp.setLayout(gl);

        sp.setLayout(new FlowLayout((FlowLayout.LEFT)));

        sp.add(logb);
        sp.add(stopb);
        sp.add(stepb);
        sp.add(resetb);
        sp.add(evaluation);
        sp.add(psoFileOutPutB);
        sp.add(cmaFileOutPutB);
        sp.add(psoFailOutPutB);
        sp.add(cmaFailOutPutB);
        sp.add(psoFileInPutB);
        sp.add(cmaFileInPutB);
        sp.add(readEnemyB);
        sp.add(readRobotB);
        sp.add(debugCombo1);
        sp.add(debugCombo2);

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
     * @param fw         ファイルライタ
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

    private String inputFile() {
        JFileChooser filechooser = new JFileChooser(new File("./"));

        String str = "";

        int selected = filechooser.showOpenDialog(this);
        StringBuilder builder = new StringBuilder();

        if (selected == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(file));
                str = br.readLine();
                while (str != null) {
                    builder.append(str + System.getProperty("line.separator"));
                    str = br.readLine();
                }
                br.close();
            } catch (IOException e1) {
                System.out.println("ファイルが読み込めません。" + e1.getMessage());
            }
        }
        return builder.toString();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("START")) {
            if (!runFlag) {
                runFlag = true;
            }
        }

        if (command.equals("STOP")) {
            if (runFlag) {
                runFlag = false;
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

        if (command.equals("Bat/Evaluation")) {
            FileWriter canvasFW, canvas4FW;
            int num = canvas.robotsNum;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

            File canvasFile = new File(EVAL_DIR + canvas.getClass().getName() + sdf.format(Calendar.getInstance().getTime()) + ".csv");
            File canvas4File = new File(EVAL_DIR + canvas4.getClass().getName() + sdf.format(Calendar.getInstance().getTime()) + ".csv");

            canvas.setRobotNum(num);
            canvas4.setRobotNum(num);
            Reset();
            System.out.println("EvalStart");
            for (int count = 0; count < 100; count++) {
                try {
                    System.out.println(count + "/100");
                    canvasFW = new FileWriter(canvasFile, true);
                    canvas4FW = new FileWriter(canvas4File, true);
                    ArrayList<Result> canvasResults = canvas.evaluation();
                    ArrayList<Result> canvas4Results = canvas4.evaluation();
                    Reset();

                    outputCsv(canvasFW, canvasResults);
                    outputCsv(canvas4FW, canvas4Results);

                    //FIXME SimulationPanel.robotsNum = num;
                    Reset();

                    canvas4FW.close();
                    canvasFW.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println("EvalEnd");
            System.out.println("PSO　出力:" + canvasFile.getAbsolutePath());
            System.out.println("CMA　出力" + canvas4File.getAbsolutePath());
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
            String str = inputFile();
            System.out.println(str);
            canvas.readFile(str);
        }

        if (command.equals("cmaFileInPut")) {
            String str = inputFile();
            System.out.println(str);
            canvas4.readFile(str);
        }

        if (command.equals("inputEnemy")) {
            String str = inputFile();
            System.out.println(str);
            canvas.readEnemyFile(str);
            canvas4.copy(canvas);
            runFlag = false;
        }

        if (command.equals("inputRobot")) {
            String str = inputFile();
            System.out.println(str);
            canvas.readRobotFile(str);
            canvas4.readRobotFile(str);
            runFlag = false;
        }

        if (command.equals("debugCanvas")) {
            if (debugCombo1.getSelectedIndex() == -1 || ((String) debugCombo1.getSelectedItem()).equals("NONE")) {
                canvas.setDebugMode("-1");
            } else {
                canvas.setDebugMode((String) debugCombo1.getSelectedItem());
            }
        }

        if (command.equals("debugCanvas2")) {
            if (debugCombo2.getSelectedIndex() == -1 || ((String) debugCombo2.getSelectedItem()).equals("NONE")) {
                canvas4.setDebugMode("-1");
            } else {
                canvas4.setDebugMode((String) debugCombo2.getSelectedItem());
            }
        }
    }
}