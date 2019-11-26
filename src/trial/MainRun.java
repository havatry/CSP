package trial;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import fileInput.ClearFiles;
import fileInput.FileLine;
import randomTopology.Constant;
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
	private JButton jbtProduceTopology = new JButton("RandomGeneration");
	private JButton jbtProduceExcel = new JButton("生成Excel");
	private JProgressBar infoBar = new JProgressBar();
	private JLabel status = new JLabel("Step1");
	private JButton jbtCompute = new JButton("Compute");
	private boolean hasProduced = false;
	private JButton jbtPrev = new JButton("Prev");
	
	public MainRun() {
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void page2() {
		setTitle("Algorithms");
		status.setText("Step2");
		boolean[] state = new boolean[5];
		String statestr = jbtProduceExcel.getClientProperty("mask").toString();
		for (int i = 0; i < statestr.length(); i++)
			if (statestr.charAt(i) == '1')
				state[i] = true;
		JCheckBox chYen = new JCheckBox("Yen算法", state[0]);
		JCheckBox chLarac = new JCheckBox("LARAC算法", state[1]);
		JCheckBox chLaracWithMd = new JCheckBox("LARACMD算法", state[2]);
		JCheckBox chBiLAD = new JCheckBox("BiLAD Algorithm", state[3]);
		JCheckBox chExactBiLAD = new JCheckBox("ExactBiLAD Algorithm", state[4]);
		JTextField tfGama = new JTextField(2);
		JTextField tfDelayContraint = new JTextField(2);
		JTextField tfMd = new JTextField(2);
		JPanel panelTop = new JPanel(new GridBagLayout());
		panelTop.add(chBiLAD, new GBC(0, 0).setInsets(50, 73, 0, 75).setIpad(42, 10));
		panelTop.add(chExactBiLAD, new GBC(0, 1).setInsets(50, 100, 0, 75).setIpad(42, 10));
		tfGama.setText(jbtProduceExcel.getClientProperty("gama").toString());
		tfDelayContraint.setText(jbtProduceExcel.getClientProperty("delay").toString());
		tfMd.setText(jbtProduceExcel.getClientProperty("md").toString());
		
		JPanel panelBlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelBlow.add(infoBar);
		panelBlow.add(status);
		panelTop.add(panelBlow, new GBC(0, 2).setInsets(50, -135, 0, 0).setIpad(0, 29));
		
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
		footer.add(jbtPrev);
		footer.add(jbtCompute);
		panelTop.add(footer, new GBC(1, 2).setInsets(50, -150, 0, 0).setIpad(50, 29));
		
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

		jbtCompute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Finish")) {
					// 完成
					// 打开excel, 并且关闭
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
				if (Integer.parseInt((String) jbtProduceExcel.getClientProperty("mask")) == 0) {
					JOptionPane.showMessageDialog(null, "Please choose one algorithm at least");
					return;
				}
				status.setVisible(false);
				infoBar.setVisible(true);
				
				// ------------------//
				int random_dalay = Math.random() < 0.5 ? 1 : 2;
				int[] delay = new int[] { random_dalay }; // 固定
				double[] gama = new double[] { 0.05 };
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
			}
		});

		jbtPrev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (infoBar.isVisible()) {
					return;
				}
				if (e.getActionCommand().equals("Open")) {
					// 打开excel
					Runtime rt = Runtime.getRuntime();
					try {
						File file = new File(Constant.excelFile);
						String absolute = file.getAbsolutePath();
						rt.exec("cmd /c " + absolute.substring(absolute.lastIndexOf("\\") + 1),
								null, new File(absolute.substring(0, absolute.lastIndexOf("\\"))));
					} catch (Exception e1) {
						// TODO: handle exception
						e1.printStackTrace();
					}
					return;
				}
				getContentPane().remove(0);
				page1();
				repaint();
			}
		});
		
		add(panelTop);
		pack();
	}
	
	private void page1() {
		JButton jbtExit = new JButton("Eixt");
		JButton jbtNext = new JButton("Next");
		JButton jbtRandom = new JButton("RandomGeneration");
		JButton jbtImport = new JButton("YourExample");
		setTitle("TestInstances");
		status.setText("Step1");
		// 第一页
		JPanel panelTop = new JPanel(new GridBagLayout());
		panelTop.add(jbtRandom, new GBC(0, 0).setInsets(50, 100, 0, 75).setIpad(70, 10));
		panelTop.add(jbtImport, new GBC(0, 1).setInsets(50, 100, 0, 75).setIpad(100, 10));
		jbtRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MyTopologyDialog dialog = new MyTopologyDialog("RandomGeneration");
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});

		jbtImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MyTopologyDialog dialog = new MyTopologyDialog("YourInstance");
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});
		
		jbtExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});

		jbtNext.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (infoBar.isVisible()) {
					// 正在生成拓扑
					return;
				}
				if (!hasProduced) {
					JOptionPane.showMessageDialog(null, "Please produce topology first");
					return;
				}
				getContentPane().remove(0);
				page2();
				repaint();
			}
		});
		JPanel panelBlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelBlow.add(infoBar);
		panelBlow.add(status);
		panelTop.add(panelBlow, new GBC(0, 2).setInsets(50, -95, 0, 0).setIpad(50, 25));
		JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panelControl.add(jbtExit);
		panelControl.add(jbtNext);
		panelTop.add(panelControl, new GBC(1, 2).setInsets(50, -142, 0, 0).setIpad(50, 25));
		add(panelTop);
		pack();
	}

	private void init() {
		jbtProduceTopology.putClientProperty("group", Constant.group + "");
		jbtProduceTopology.putClientProperty("copy", Constant.copy + "");
		jbtProduceTopology.putClientProperty("step", Constant.step);
		jbtProduceTopology.putClientProperty("hasDone", false);
		jbtProduceExcel.putClientProperty("gama", "0.05-0.05-0.1");
		jbtProduceExcel.putClientProperty("delay", "1-2-5");
		jbtProduceExcel.putClientProperty("md", "0.2");
		jbtProduceExcel.putClientProperty("mask", "00011");
		infoBar.setStringPainted(true);
		infoBar.setValue(0);
		infoBar.setMaximum(100);
		infoBar.setVisible(false);
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
		private MutilMethodsComparision mc;

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
			mc = new MutilMethodsComparision(methods, delay, gama, this);
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
			jbtPrev.setText("Open");
			status.setText("save path: " + mc.getFilename());
			Constant.excelFile = mc.getFilename();
			jbtCompute.setText("Finish");
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
//			GenerateRandomNetwork_MainProgram.writeCharacteristicToFile();
			status.setText("save dir: " + Constant.basePath);
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
//			GenerateRandomNetwork_MainProgram.writeCharacteristicToFile();
		}
	}

	private class MyTopologyDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		
		public MyTopologyDialog(String title) {
			this(null, true, title);
		}

		public MyTopologyDialog(Frame parent, boolean modal, String title) {
			super(parent, modal);
			JTextField tfGroup = new JTextField(2);
			JTextField tfCopy = new JTextField(2);
			JTextField tfStep = new JTextField(2);
			JTextField tfData = new JTextField();
			JTextField tfSource = new JTextField(2);
			JTextField tfTarget = new JTextField(2);
			JTextField tfDelay = new JTextField(2);
			JButton jbtOk = new JButton("OK");
			JButton jbtCancel = new JButton("Cancel");
			
			JPanel panel;
			if (title.equals("RandomGeneration")) {
				panel = new JPanel(new GridLayout(2, 2, 5, 5));
				panel.add(new JLabel("NumberNodes"));
				panel.add(tfStep);
				panel.add(new JLabel("NumberInstances"));
				panel.add(tfCopy);
			} else {
				panel = new JPanel(new GridLayout(4, 2, 5, 5));
				panel.add(new JLabel("LinkData"));
				panel.add(tfData);
				panel.add(new JLabel("SourceNode"));
				panel.add(tfSource);
				panel.add(new JLabel("TargetNode"));
				panel.add(tfTarget);
				panel.add(new JLabel("DelayThresold"));
				panel.add(tfDelay);
			}
			tfData.setText("Click here to select file");
			tfData.setEditable(false);

			tfData.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileFilter() {
						
						@Override
						public String getDescription() {
							// TODO Auto-generated method stub
							return "*.txt";
						}
						
						@Override
						public boolean accept(File f) {
							// TODO Auto-generated method stub
							return f.getName().endsWith(".txt");
						}
					});
					int value = fc.showOpenDialog(null);
					if (value == JFileChooser.APPROVE_OPTION) {
						// 确认选中
						Constant.specIdFile = fc.getSelectedFile().getAbsolutePath();
						Constant.specFile = true; // 指定目录
						// key
						Constant.numNodes = FileLine.GetLineNumber(Constant.specIdFile);
						tfData.setText("OK");
					}
				}
			});
			JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
			footer.add(jbtOk);
			footer.add(jbtCancel);
			
			if (title.equals("RandomGeneration")) {
				tfGroup.setText(jbtProduceTopology.getClientProperty("group").toString());
				tfCopy.setText(jbtProduceTopology.getClientProperty("copy").toString());
				tfStep.setText(jbtProduceTopology.getClientProperty("step").toString());
			}

			jbtOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					// 检查输入是否都填充了
					if (title.equals("RandomGeneration")) {
						if (tfCopy.getText().equals("") || tfStep.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please fill all textFields");
							return;
						}
					} else {
						if (!tfData.getText().equals("OK") || tfSource.getText().equals("")
								|| tfTarget.getText().equals("") || tfDelay.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please fill all textFields");
							return;
						}
					}
					File[] files = new File("resource/file").listFiles();
					boolean judge = files.length == 0 ? false : true;
					if (title.equals("RandomGeneration") && judge) {
						int a = JOptionPane.showConfirmDialog(getContentPane(), "Clear existed files", "Information",
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
						} else {
							// 取消
							return;
						}
					}
					if (title.equals("RandomGeneration")) {
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
						Task task = new Task(Constant.group * Constant.copy);
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
					} else 
					// 考虑指定文件的情况
					{
						Constant.group = 1;
						Constant.copy = 1;
						Constant.start = Integer.parseInt(tfSource.getText());
						Constant.end = Integer.parseInt(tfTarget.getText());
						Constant.specDelay = Integer.parseInt(tfDelay.getText());
						status.setText("Locate file: " + 
								Constant.specIdFile.substring(Constant.specIdFile.lastIndexOf("\\") + 1));
					}
					hasProduced = true; // 已经产生拓扑了
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
			setTitle(title);
			pack();
		}
	}

	public static void main(String[] args) {
		MainRun frame = new MainRun();
		frame.page1();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
