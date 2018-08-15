package tess4j.test;

/**
 * ���̶߳���д�ļ�
 * 
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class test {
	public static void main(String args[]) {
		long millis1 = System.currentTimeMillis();
		System.out.println(millis1);
		Read3 read = new Read3(millis1);
		ExecutorService service = Executors.newFixedThreadPool(5);
		for (int i = 1; i <= 3; i++) {
			service.execute(new Thread(read, "�߳�" + i));
		}
	}
}

class Read3 implements Runnable {
	Object o = new Object();
	List<File> filePathsList = new ArrayList<File>();
	int index = 0;
	private long millis;

	public Read3(long millis1) {
		this.millis = millis1;
		File f = new File("d:" + File.separator + "gc2");
		getFileList(f);
	}

	private void getFileList(File f) {
		File[] filePaths = f.listFiles();
		for (File s : filePaths) {
			if (s.isDirectory()) {
				getFileList(s);
			} else {
				if (-1 != s.getName().lastIndexOf(".txt")) {
					filePathsList.add(s);
				}
			}
		}
	}

	public void run() {
		File file = null;
		File f2 = null;
		while (index < filePathsList.size()) {
			// �˴�����֤�˶��̲߳��ύ���ȡ�ļ�

			// --1.1�����ڵı������̰߳�ȫ��
			// ���ͣ����ڷ����ڵı�����˽�еģ�������ʵ�ͬʱ���˷��ʲ��ˣ��������̰߳�ȫ�ġ�
			// --1.2ʵ�������Ƿ��̰߳�ȫ��
			// ���ͣ�����ʵ�����������ɶ���̷߳��ʣ�������������������У�����Ҳ������ռ��Դ����������ʹ���ݲ�ͬ���ˣ������Ƿ��̰߳�ȫ�ġ�

			synchronized (o) {
				if (index > filePathsList.size()) {
					return;
				}
				file = filePathsList.get(index);
				index++;
				// System.out.println("�ڲ�index: " + index);
			}
			// System.out.println("�ļ�: " + file.getName());
			FileReader fr = null;
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();

			FileWriter fw = null;
			BufferedWriter bw = null;
			f2 = new File("d:" + File.separator + "gc3" + File.separator
					+ file.getName());

			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				fw = new FileWriter(f2);
				bw = new BufferedWriter(fw);

				String data = "";
				while ((data = br.readLine()) != null) {
					// sb.append(data + "\r");
					bw.write(data + "\r");
				}

				bw.write("---------------" + Thread.currentThread().getName()
						+ "---------------");
				System.out.println(Thread.currentThread().getName() + " : "
						+ file.getName());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
					br.close();

					/*
					 * long millis2 = System.currentTimeMillis();
					 * System.out.println(millis2); System.out.println(millis2 -
					 * millis); //��Լ1-2ms
					 */
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}