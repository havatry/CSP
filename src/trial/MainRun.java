package trial;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fileInput.ClearFiles;
import randomTopology.Constant;
import randomTopology.GenerateRandomNetwork_MainProgram;
import randomTopology.Topology;
import randomTopology.XMLHelper;

/**
 * 
 * OverView:
 *		该类是对本程序的一个总结。通过三个按钮、一个进度条和一个显示信息的状态栏，完成了本程序的所有功能。
 *		APP可以由本程序算法自行开发。此图形界面代码和本程序相关度不大，不再进行注释。
 */
public class MainRun extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton jbtProduceTopology = new JButton("生成拓扑");
	private JButton jbtDisplayGraphics = new JButton("展示图形");
	private JButton jbtProduceExcel = new JButton("生成Excel");
	private JProgressBar infoBar = new JProgressBar();
	private JLabel status = new JLabel("Ready");

	public MainRun() {
		// TODO Auto-generated constructor stub
		init();
		JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelTop.add(jbtProduceTopology);
		panelTop.add(jbtDisplayGraphics);
		panelTop.add(jbtProduceExcel);
		jbtProduceTopology.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MyTopologyDialog dialog = new MyTopologyDialog();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});

		jbtDisplayGraphics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String line = JOptionPane.showInputDialog(null, "输入组号-组内号", "1-1");
				int groupTh = Integer.parseInt(line.split("-")[0]);
				int copyTh = Integer.parseInt(line.split("-")[1]);
				int copy = Integer.parseInt(jbtProduceTopology.getClientProperty("copy").toString());
				int step = Integer.parseInt(jbtProduceTopology.getClientProperty("step").toString());
				if (groupTh < 1 || groupTh > Integer.parseInt(jbtProduceTopology.getClientProperty("group").toString())
						|| copyTh < 1 || copyTh > copy) {
					JOptionPane.showMessageDialog(getContentPane(), "输入值无效");
					return;
				}
				int showMaxNode = Integer.parseInt(XMLHelper.getValue("//allInfo/test/showMaxNode"));
				if ((groupTh * step) > showMaxNode) {
					JOptionPane.showMessageDialog(getContentPane(), "节点数超过100,不予显示");
					return;
				}
				new Display((groupTh - 1) * copy + copyTh);
			}
		});

		jbtProduceExcel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constant.WriteFile_TimeFor = 1;// 重置
				MyExcelDialog dialog = new MyExcelDialog();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});

		JPanel panelBlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelBlow.add(infoBar);
		panelBlow.add(status);
		add(panelTop, BorderLayout.NORTH);
		add(panelBlow, BorderLayout.CENTER);
	}

	private void init() {
		jbtProduceTopology.putClientProperty("group", Constant.group + "");
		jbtProduceTopology.putClientProperty("copy", Constant.copy + "");
		jbtProduceTopology.putClientProperty("step", Constant.step);
		jbtProduceTopology.putClientProperty("hasDone", false);
		jbtProduceExcel.putClientProperty("gama", "0.05-0.05-0.1");
		jbtProduceExcel.putClientProperty("delay", "1-2-5");
		jbtProduceExcel.putClientProperty("md", "0.2");
		jbtProduceExcel.putClientProperty("mask", "01111");
		infoBar.setStringPainted(true);
		infoBar.setValue(0);
		infoBar.setMaximum(100);
		infoBar.setVisible(false);
	}

	private int[] getDelays(String delayConstraint) {
		int delayStart = Integer.parseInt(delayConstraint.split("-")[0]);
		int delayStep = Integer.parseInt(delayConstraint.split("-")[1]);
		int delayEnd = Integer.parseInt(delayConstraint.split("-")[2]);
		int count = (delayEnd - delayStart) / delayStep + 1;
		int[] delay = new int[count];
		for (int i = 0; i < count; i++)
			delay[i] = delayStart + i * delayStep;
		return delay;
	}

	private double[] getGamas(String gama) {
		double gamaStart = Double.parseDouble(gama.split("-")[0]);
		double gamaStep = Double.parseDouble(gama.split("-")[1]);
		double gamaEnd = Double.parseDouble(gama.split("-")[2]);
		int count = (int) ((gamaEnd - gamaStart) / gamaStep) + 1;
		double[] gamaRet = new double[count];
		for (int i = 0; i < count; i++)
			gamaRet[i] = gamaStart + i * gamaStep;
		return gamaRet;
	}

	private boolean[] getStates(String state) {
		boolean[] stateRet = new boolean[state.length()];
		for (int i = 0; i < state.length(); i++)
			if (state.charAt(i) == '1')
				stateRet[i] = true;
		return stateRet;
	}

	private String[] getMethods(String state) {
		boolean[] statebol = getStates(state);
		List<String> methodList = new ArrayList<String>(
				Arrays.asList("YEN", "LARAC", "LARACMD", "BiLAD", "ExactBiLAD"));
		for (int i = 0; i < statebol.length; i++) {
			if (!statebol[i]) {
				switch (i) {
				case 0:
					methodList.remove("YEN");
					break;
				case 1:
					methodList.remove("LARAC");
					break;
				case 2:
					methodList.remove("LARACMD");
					break;
				case 3:
					methodList.remove("BiLAD");
					break;
				case 4:
					methodList.remove("ExactBiLAD");
					break;
				default:
					break;
				}
			}
		}
		String[] methods = new String[methodList.size()];
		for (int i = 0; i < methods.length; i++)
			methods[i] = methodList.get(i);
		return methods;
	}

	protected class Task2 extends SwingWorker<String, String> {
		private String[] methods;
		private int[] delay;
		private double[] gama;

		public Task2() {
			// TODO Auto-generated constructor stub
		}

		public Task2(String[] methods, int[] delay, double[] gama) {
			// TODO Auto-generated constructor stub
			this.methods = methods;
			this.delay = delay;
			this.gama = gama;
		}

		@Override
		protected String doInBackground() throws Exception {
			// TODO Auto-generated method stub
			MutilMethodsComparision mc = new MutilMethodsComparision(methods, delay, gama, this);
			mc.DesignExcel();
			return "SUCCESS";
		}

		@Override
		protected void done() {
			// TODO Auto-generated method stub
			super.done();
			infoBar.setValue(0);
			infoBar.setVisible(false);
			status.setVisible(true);
			status.setText("Excel文件已经生成");
			System.gc();// KEY
			//自动关机
			if(jbtProduceExcel.getClientProperty("mask").toString().charAt(0)=='1' 
					&& Boolean.parseBoolean(XMLHelper.getValue("//allInfo/test/shutdown"))){
				MutilMethodsComparision.shutdown();
			}
		}
	}

	private class Task extends SwingWorker<String, String> {
		private int count;

		public Task(int count) {
			// TODO Auto-generated constructor stub
			this.count = count;
		}

		@Override
		protected String doInBackground() throws Exception {
			// TODO Auto-generated method stub
			status.setVisible(false);
			infoBar.setVisible(true);
			Constant.WriteFile_TimeFor = 1;// KEY
			produce();
			return "SUCCESS";
		}

		@Override
		protected void done() {
			// TODO Auto-generated method stub
			super.done();
			GenerateRandomNetwork_MainProgram.writeCharacteristicToFile();
			status.setText("网络拓扑生成成功");
			status.setVisible(true);
			infoBar.setValue(0);
			infoBar.setVisible(false);
		}

		public void produce() {
			for (int i = 0; i < Constant.group; i++) {
				XMLHelper.setValue("//allInfo/node/number", Constant.step * (i + 1) + "");// 里面有更新变量
				for (int j = 0; j < Constant.copy; j++) {
					new Topology().ProduceTopology();
					Constant.WriteFile_TimeFor++;// 更新
					setProgress(100 * Constant.WriteFile_TimeFor / count);//当这个到100的时候，可能提前结束了
				}
			}
			GenerateRandomNetwork_MainProgram.writeCharacteristicToFile();
		}
	}

	private class MyExcelDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		public MyExcelDialog() {
			// TODO Auto-generated constructor stub
			this(null, true);
		}

		public MyExcelDialog(Frame parent, boolean modal) {
			super(parent, modal);
			boolean[] state = new boolean[5];
			String statestr = jbtProduceExcel.getClientProperty("mask").toString();
			for (int i = 0; i < statestr.length(); i++)
				if (statestr.charAt(i) == '1')
					state[i] = true;
			JCheckBox chYen = new JCheckBox("Yen算法", state[0]);
			JCheckBox chLarac = new JCheckBox("LARAC算法", state[1]);
			JCheckBox chLaracWithMd = new JCheckBox("LARACMD算法", state[2]);
			JCheckBox chBiLAD = new JCheckBox("BiLAD算法", state[3]);
			JCheckBox chExactBiLAD = new JCheckBox("ExactBiLAD算法", state[4]);
			JCheckBox chNull = new JCheckBox();
			JTextField tfGama = new JTextField(2);
			JTextField tfDelayContraint = new JTextField(2);
			JTextField tfMd = new JTextField(2);
			JPanel panelTop = new JPanel(new GridLayout(6, 2, 5, 5));
			panelTop.add(chYen);
			panelTop.add(chLarac);
			panelTop.add(chLaracWithMd);
			panelTop.add(chBiLAD);
			panelTop.add(chExactBiLAD);
			panelTop.add(chNull);
			panelTop.add(new JLabel("Game取值"));
			panelTop.add(tfGama);
			panelTop.add(new JLabel("Delay取值"));
			panelTop.add(tfDelayContraint);
			panelTop.add(new JLabel("Md取值"));
			panelTop.add(tfMd);
			chNull.setVisible(false);

			tfGama.setText(jbtProduceExcel.getClientProperty("gama").toString());
			tfDelayContraint.setText(jbtProduceExcel.getClientProperty("delay").toString());
			tfMd.setText(jbtProduceExcel.getClientProperty("md").toString());

			JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			JButton jbtOK = new JButton("确定");
			JButton jbtCancel = new JButton("取消");
			footer.add(jbtOK);
			footer.add(jbtCancel);

			chLaracWithMd.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					if (!chLaracWithMd.isSelected()) {
						tfMd.setText("");
						tfMd.setEditable(false);
					} else {
						tfMd.setText("0.2");
						tfMd.setEditable(true);
					}
				}
			});

			jbtOK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (jbtProduceTopology.getClientProperty("hasDone") instanceof Boolean)
						if (!(Boolean) jbtProduceTopology.getClientProperty("hasDone")) {
							JOptionPane.showMessageDialog(getContentPane(), "请先生成拓扑", "警告",
									JOptionPane.WARNING_MESSAGE);
							setVisible(false);
							dispose();
							return;
						}
					jbtProduceExcel.putClientProperty("gama", tfGama.getText());
					jbtProduceExcel.putClientProperty("delay", tfDelayContraint.getText());
					jbtProduceExcel.putClientProperty("md", tfMd.getText());
					String statestr = "00000";
					char[] statechars = statestr.toCharArray();
					if (chYen.isSelected())
						statechars[0] = '1';
					if (chLarac.isSelected())
						statechars[1] = '1';
					if (chLaracWithMd.isSelected())
						statechars[2] = '1';
					if (chBiLAD.isSelected())
						statechars[3] = '1';
					if (chExactBiLAD.isSelected())
						statechars[4] = '1';
					jbtProduceExcel.putClientProperty("mask", new String(statechars));
					setVisible(false);
					status.setVisible(false);
					infoBar.setVisible(true);
					
					// ------------------//
					int[] delay = getDelays(jbtProduceExcel.getClientProperty("delay").toString());
					double[] gama = getGamas(jbtProduceExcel.getClientProperty("gama").toString());
					String[] methods = getMethods(jbtProduceExcel.getClientProperty("mask").toString());
					Task2 task2 = new Task2(methods, delay, gama);
					task2.addPropertyChangeListener(new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							// TODO Auto-generated method stub
							if ("progress".equals(evt.getPropertyName())) {
								infoBar.setValue((Integer) evt.getNewValue());
							}
						}
					});
					task2.execute();
					// ---------------//
					dispose();
				}
			});

			jbtCancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
					dispose();
					return;
				}
			});

			add(panelTop, BorderLayout.CENTER);
			add(footer, BorderLayout.SOUTH);
			pack();
		}
	}

	private class MyTopologyDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		public MyTopologyDialog() {
			// TODO Auto-generated constructor stub
			this(null, true);
		}

		public MyTopologyDialog(Frame parent, boolean modal) {
			super(parent, modal);
			JTextField tfGroup = new JTextField(2);
			JTextField tfCopy = new JTextField(2);
			JTextField tfStep = new JTextField(2);
			JButton jbtOk = new JButton("确定");
			JButton jbtCancel = new JButton("取消");

			JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
			panel.add(new JLabel("所有组数"));
			panel.add(tfGroup);
			panel.add(new JLabel("每组个数"));
			panel.add(tfCopy);
			panel.add(new JLabel("每组差数"));
			panel.add(tfStep);

			JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			footer.add(jbtOk);
			footer.add(jbtCancel);

			tfGroup.setText(jbtProduceTopology.getClientProperty("group").toString());
			tfCopy.setText(jbtProduceTopology.getClientProperty("copy").toString());
			tfStep.setText(jbtProduceTopology.getClientProperty("step").toString());

			jbtOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					File[] files = new File("resource/file").listFiles();
					boolean judge = files.length == 0 ? false : true;
					if (judge) {
						int a = JOptionPane.showConfirmDialog(getContentPane(), "生成新拓扑前，是否清空已有文件", "检测到已有文件",
								JOptionPane.YES_NO_OPTION);
						if (a == JOptionPane.YES_OPTION) {
							ClearFiles.main(null);
							setVisible(false);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					jbtProduceTopology.putClientProperty("group", tfGroup.getText());
					jbtProduceTopology.putClientProperty("copy", tfCopy.getText());
					jbtProduceTopology.putClientProperty("step", tfStep.getText());
					jbtProduceTopology.putClientProperty("hasDone", true);
					status.setVisible(false);
					infoBar.setVisible(true);
					// --------------------//
					int group = Integer.parseInt(jbtProduceTopology.getClientProperty("group").toString());
					int copy = Integer.parseInt(jbtProduceTopology.getClientProperty("copy").toString());
					int step = Integer.parseInt(jbtProduceTopology.getClientProperty("step").toString());
					Constant.group = group;
					Constant.copy = copy;
					Constant.step = step;
					Task task = new Task(group * copy);
					task.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							// TODO Auto-generated method stub
							if ("progress".equals(evt.getPropertyName())) {
								infoBar.setValue((Integer) evt.getNewValue());
							}
						}
					});
					task.execute();
					// -------------------//
					dispose();
				}
			});

			jbtCancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					setVisible(false);
					dispose();
					return;
				}
			});
			add(panel, BorderLayout.CENTER);
			add(footer, BorderLayout.SOUTH);
			setTitle("拓扑属性");
			pack();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new MainRun();
		frame.setTitle("CSPMethods");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
