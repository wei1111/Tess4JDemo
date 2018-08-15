package ImageDemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.xmlbeans.impl.jam.xml.TunnelledException;

//实现接口ActionListener
public class ImageDemo2 implements ActionListener {

	JFrame jf;
	JPanel jpanel;
	JButton jb1, jb2, jb3, open, run;
	JTextArea jta = null;
	JScrollPane jscrollPane;
	// 需要扫描并识别的图片后图片文件夹路径
	String imagePath = null;
	List<String> list = null;
	// 生成的Excel的路径
	String resultExcelName = null;
	int threadSize;
	// 截取图片的x轴y轴开始坐标
	// 截取图片的x轴y轴长度
	int x, y, width, height;
	// Excel的列名
	String[] titles;
	public static String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	public ImageDemo2(String str[]) {

		resultExcelName = str[0];
		threadSize = Integer.parseInt(str[1]);
		x = Integer.parseInt(str[2]);
		y = Integer.parseInt(str[3]);
		width = Integer.parseInt(str[4]);
		height = Integer.parseInt(str[5]);
		titles = new String[] { str[6], str[7], str[8] };

		jf = new JFrame("工商图片识别");
		Image icon = Toolkit.getDefaultToolkit().getImage("ship.png");
		jf.setIconImage(icon);

		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new BorderLayout());

		jta = new JTextArea(10, 15);
		jta.setTabSize(8);
		jta.setFont(new Font("标楷体", Font.BOLD, 24));
		jta.setLineWrap(true);// 激活自动换行功能
		jta.setWrapStyleWord(true);// 激活断行不断字功能
		jta.setBackground(Color.pink);// 背景色粉

		jscrollPane = new JScrollPane(jta);// 滚轴
		jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(1, 3));

		jb1 = new JButton("复制");
		jb1.addActionListener(this);
		jb2 = new JButton("粘贴");
		jb2.addActionListener(this);
		jb3 = new JButton("剪切");
		jb3.addActionListener(this);
		open = new JButton("选择文件");
		open.addActionListener(this);
		run = new JButton("开始");
		run.addActionListener(this);

		jpanel.add(jb1);
		jpanel.add(jb2);
		jpanel.add(jb3);
		jpanel.add(open);
		jpanel.add(run);

		contentPane.add(jscrollPane, BorderLayout.CENTER);
		contentPane.add(jpanel, BorderLayout.SOUTH);

		jf.setSize(900, 700);
		jf.setLocation(400, 200);
		jf.setVisible(true);

		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	// 覆盖接口ActionListener的方法actionPerformed
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			jta.copy();
		} else if (e.getSource() == jb2) {
			jta.paste();
		} else if (e.getSource() == jb3) {
			jta.cut();
		} else if (e.getSource() == open) {
			actionPerformed_Open(e);
		} else if (e.getSource() == run) {
			jta.append(LINE_SEPARATOR + "----正在处理请等待----" + LINE_SEPARATOR);

			jta.append("识别速度取决于您的电脑配置" + LINE_SEPARATOR);
			jta.append("生成的Excel文件位置：" + resultExcelName + LINE_SEPARATOR);
			jta.append("开启的线程数" + threadSize + LINE_SEPARATOR);
			jta.append("识别图片启示坐标（" + x + "," + y + ")" + LINE_SEPARATOR);
			jta.append("识别图片长度和宽度（" + width + "," + height + ")" + LINE_SEPARATOR);
			jta.append("生成的Excel的列名：----" + titles[0] + "----" + titles[1] + "----" + titles[2] + LINE_SEPARATOR);
			jta.append("默认参数：" + "C:\\天猫工商信息执照\\天猫工商信息执照识别结果.xlsx, " + "9, " + "0, " + "8, " + "533, " + "67, " + "图片名称, "
					+ "公司名称, " + "公司注册号 " + LINE_SEPARATOR);
			jta.append("您可以根据电脑配置选择合理的线程数目，根据图片选择识别位置，在批处理文件中的更改默认配置" + LINE_SEPARATOR);
			jta.append("如果C盘因为权限问题不可写，请选择合适的Excel文件生成位置" + LINE_SEPARATOR);
			jta.paintImmediately(jta.getBounds());

			actionPerformed_Run(e);
		}
	}

	public void actionPerformed_Open(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file != null) {
			if (file.isDirectory()) {
				imagePath = file.getAbsolutePath();
				if (imagePath != null) {
					// System.out.println("所选文件夹:" + imagePath);
					jta.append(imagePath + LINE_SEPARATOR);
				}

			} else if (file.isFile()) {
				imagePath = file.getAbsolutePath();
				if (imagePath != null) {
					// System.out.println("文件:" + file.getAbsolutePath());
					jta.append(file.getAbsolutePath() + LINE_SEPARATOR);
					jta.append("生成的Excel文件位置：" + resultExcelName + LINE_SEPARATOR);
				}
			}
		}
		// System.out.println(jfc.getSelectedFile().getName());
	}

	public void actionPerformed_Run(ActionEvent e) {
		try {
			if (imagePath != null) {
				// String imagePath, String resultExcelName, int n, int x, int
				// y, int width,
				// int height, String[] titles

				list = new ImageDemo().runImageProcess(imagePath, resultExcelName, threadSize, x, y, width, height,
						titles);
				Iterator<String> iterator = list.iterator();
				while (iterator.hasNext()) {
					jta.append(iterator.next());
				}
			} else {
				// 什么都不做
			}
		} catch (Exception e1) {
			// System.out.println(e1.toString());
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args == null || args.length < 9) {
			String[] str1 = { "C:\\天猫工商信息执照\\天猫工商信息执照识别结果.xlsx", "9", "0", "8", "533", "67", "图片名称", "公司名称", "公司注册号" };
			args = str1;
		}
		new ImageDemo2(args);
	}
}
