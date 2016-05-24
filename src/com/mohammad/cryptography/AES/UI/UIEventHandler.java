package com.mohammad.cryptography.AES.UI;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.mohammad.cryptography.AES.AES_CORE;
import com.mohammad.cryptography.AES.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIEventHandler implements Initializable {

	private Stage stage;

	@FXML
	private Button openBtn;
	@FXML
	private Button browseBtn;
	@FXML
	private Button insertKeyBtn;
	@FXML
	private Button chooseKeyBtn;
	@FXML
	private Button EncryptBtn;
	@FXML
	private Button decryptBtn;
	@FXML
	private Button allKeysBtn;
	@FXML
	private Button resultBtn;
	@FXML
	private Button cypherTextDetailesBtn;
	@FXML
	private Button plainTextDetailesBtn;
	@FXML
	private Button r_encryptBtn;
	@FXML
	private Button r_decryptBtn;

	@FXML
	private Label filePathLabel;
	@FXML
	private Label r_timeLabel;
	@FXML
	private Label l_timeLabel;
	@FXML
	private Label statusBarLabel;

	@FXML
	private TextField filePathTF;
	@FXML
	private TextField l_insertKeyTF;
	@FXML
	private TextField r_keyTF;

	@FXML
	private TextArea plainTextArea;
	@FXML
	private TextArea cypherTextArea;

	private String keyPath = "key.txt", resultPath = "result.txt", inputPath = "input.txt", allKeysPath = "allKeys.txt";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	public void init(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void textAreaEncrypti() {
		System.out.println("textAreaEncrypti...");
		statusBarLabel.setText("textAreaEncrypti...");

		String text = plainTextArea.getText();
		String flag = text.substring(0, 3);
		text = text.substring(3);
		List<String> plain = new ArrayList<>();
		String binaryText = "";
		StringBuilder binary = new StringBuilder(), textSB = new StringBuilder(), ascii = new StringBuilder(),
				hex = new StringBuilder();
		Tools tool = new Tools();

		String Key = r_keyTF.getText();
		if (Key.length() != 128) {
			Key = tool.hex2binary(Key);
		}

		AES_CORE AES = new AES_CORE(Key);
		switch (flag.toUpperCase()) {
		case "HEX":
			text = text.replaceAll(" ", "").replaceAll("\n", "");
			tool.HexConversion(new StringBuilder(text), binary, textSB, ascii);
			binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
			break;
		case "TXT":
			while (text.length() % 128 != 0)
				text += " ";

			tool.TextConversion(textSB.append(text), binary, hex, ascii);
			binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
			break;
		case "BIN":
			binaryText = text.replaceAll(" ", "").replaceAll("\n", "");
			tool.BinaryConversion(new StringBuilder(binaryText), textSB, hex, ascii);
			break;
		}
		String out = "";
		double starttime = System.currentTimeMillis() ;
		while (binaryText.length() > 0) {
			String next = binaryText.substring(0, 128);
			binaryText = binaryText.substring(128);
			out = out + AES.encrypt(next);
		}
		double time = System.currentTimeMillis() - starttime;
		r_timeLabel.setText("TIME "+time+" .ms");
		
		textSB = new StringBuilder();
		hex = new StringBuilder();
		ascii = new StringBuilder();
		tool.BinaryConversion(new StringBuilder(out), textSB, hex, ascii);
		cypherTextArea.setText("hex \n" + hex + "\n\nbinary\n" + out + "\n\nTXT\n" + textSB.toString());
		AES.printKeysToFile(allKeysPath);
	}

	@FXML
	public void textAreaDecrypti() {
		System.out.println("textAreaDecrypti...");
		statusBarLabel.setText("textAreaDecrypti...");
		String text = plainTextArea.getText();
		String flag = text.substring(0, 3);
		text = text.substring(3);
		String binaryText = "";
		StringBuilder binary = new StringBuilder(), textSB = new StringBuilder(), ascii = new StringBuilder(),
				hex = new StringBuilder();
		Tools tool = new Tools();

		String Key = r_keyTF.getText();
		if (Key.length() != 128) {
			Key = tool.hex2binary(Key);
		}

		AES_CORE AES = new AES_CORE(Key);
		switch (flag.toUpperCase()) {
		case "HEX":
			text = text.replaceAll(" ", "").replaceAll("\n", "");
			tool.HexConversion(new StringBuilder(text), binary, textSB, ascii);
			binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
			break;
		case "TXT":
			while (text.length() % 128 != 0)
				text += " ";

			tool.TextConversion(textSB.append(text), binary, hex, ascii);
			binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
			break;
		case "BIN":
			binaryText = text.replaceAll(" ", "").replaceAll("\n", "");
			tool.BinaryConversion(new StringBuilder(binaryText), textSB, hex, ascii);
			break;
		}
		String out = "";
		double starttime = System.currentTimeMillis();
		while (binaryText.length() > 0) {
			String next = binaryText.substring(0, 128);
			binaryText = binaryText.substring(128);
			out = out + AES.decrypt(next);
		}
		double time = System.currentTimeMillis() - starttime;
		r_timeLabel.setText("TIME "+time+" .ms");

		textSB = new StringBuilder();
		hex = new StringBuilder();
		ascii = new StringBuilder();
		tool.BinaryConversion(new StringBuilder(out), textSB, hex, ascii);
		cypherTextArea.setText("hex \n" + hex + "\n\nbinary\n" + out + "\n\nTXT\n" + textSB.toString());
		AES.printKeysToFile(allKeysPath);
	}

	public void fileEncrypt() {
		try {

			File input = new File(filePathTF.getText()), keyFile = new File(keyPath);
			Scanner in_sc = new Scanner(input), keySc = new Scanner(keyFile);
			String flag = in_sc.next();
			String text = "", Key = keySc.next();
			while (in_sc.hasNextLine())
				text += in_sc.nextLine() + "\n";

			in_sc.close();
			keySc.close();

			String binaryText = "";
			StringBuilder binary = new StringBuilder(), textSB = new StringBuilder(), ascii = new StringBuilder(),
					hex = new StringBuilder();
			Tools tool = new Tools();

			if (Key.length() != 128) {
				Key = tool.hex2binary(Key);
			}

			AES_CORE AES = new AES_CORE(Key);
			switch (flag.toUpperCase()) {
			case "HEX":
				text = text.replaceAll(" ", "").replaceAll("\n", "");
				tool.HexConversion(new StringBuilder(text), binary, textSB, ascii);
				binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
				break;
			case "TXT":
				while (text.length() % 128 != 0)
					text += " ";

				tool.TextConversion(textSB.append(text), binary, hex, ascii);
				binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
				break;
			case "BIN":
				binaryText = text.replaceAll(" ", "").replaceAll("\n", "");
				tool.BinaryConversion(new StringBuilder(binaryText), textSB, hex, ascii);
				break;
			}
			String out = "";
			double starttime = System.currentTimeMillis();
			while (binaryText.length() > 0) {
				String next = binaryText.substring(0, 128);
				binaryText = binaryText.substring(128);
				out = out + AES.encrypt(next);
			}

			double time = System.currentTimeMillis() - starttime;
			l_timeLabel.setText("TIME "+time+" .ms");

			textSB = new StringBuilder();
			hex = new StringBuilder();
			ascii = new StringBuilder();
			tool.BinaryConversion(new StringBuilder(out), textSB, hex, ascii);
			FileWriter resultWrite = new FileWriter(new File(resultPath));

			resultWrite.write("hex \n" + hex + "\n\nbinary\n" + out + "\n\nTXT\n" + textSB.toString());

			resultWrite.close();
			
			AES.printKeysToFile(allKeysPath);
		} catch (Exception e) {
			statusBarLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	public void fileDecrypt() {
		try {

			File input = new File(filePathTF.getText()), keyFile = new File(keyPath);
			Scanner in_sc = new Scanner(input), keySc = new Scanner(keyFile);
			String flag = in_sc.next();
			String text = "", Key = keySc.next();
			while (in_sc.hasNextLine())
				text += in_sc.nextLine() + "\n";

			in_sc.close();
			keySc.close();

			String binaryText = "";
			StringBuilder binary = new StringBuilder(), textSB = new StringBuilder(), ascii = new StringBuilder(),
					hex = new StringBuilder();
			Tools tool = new Tools();

			if (Key.length() != 128) {
				Key = tool.hex2binary(Key);
			}

			AES_CORE AES = new AES_CORE(Key);
			switch (flag.toUpperCase()) {
			case "HEX":
				text = text.replaceAll(" ", "").replaceAll("\n", "");
				tool.HexConversion(new StringBuilder(text), binary, textSB, ascii);
				binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
				break;
			case "TXT":
				while (text.length() % 128 != 0)
					text += " ";

				tool.TextConversion(textSB.append(text), binary, hex, ascii);
				binaryText = binary.toString().replaceAll(" ", "").replaceAll("\n", "");
				break;
			case "BIN":
				binaryText = text.replaceAll(" ", "").replaceAll("\n", "");
				tool.BinaryConversion(new StringBuilder(binaryText), textSB, hex, ascii);
				break;
			}
			String out = "";
			
			double starttime = System.currentTimeMillis();
			while (binaryText.length() > 0) {
				String next = binaryText.substring(0, 128);
				binaryText = binaryText.substring(128);
				out = out + AES.decrypt(next);
			}

			double time = System.currentTimeMillis() - starttime;
			l_timeLabel.setText("TIME "+time+" .ms");

			textSB = new StringBuilder();
			hex = new StringBuilder();
			ascii = new StringBuilder();
			tool.BinaryConversion(new StringBuilder(out), textSB, hex, ascii);
			FileWriter resultWrite = new FileWriter(new File(resultPath));
			resultWrite.write("hex \n" + hex + "\n\nbinary\n" + out + "\n\nTXT\n" + textSB.toString());

			resultWrite.close();
			
			AES.printKeysToFile(allKeysPath);
		} catch (Exception e) {
			statusBarLabel.setText(e.getMessage());
			e.printStackTrace();
		}
	}

	public void OpenInputFile() {
		String path = filePathTF.getText();
		System.out.println("Openeng file " + path);
		statusBarLabel.setText("Openeng file " + path);
		try {
			Open(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InsertKey() {
		try {

			FileWriter keyFileWrit = new FileWriter(new File(keyPath));

			String key = l_insertKeyTF.getText();
			keyFileWrit.write(key);
			keyFileWrit.close();
			filePathLabel.setText(keyPath);
			statusBarLabel.setText("Key Inserted");

		} catch (IOException e) {
			statusBarLabel.setText("ERROR :: Key Insertion Failed!");

			e.printStackTrace();
		}

	}

	public void ChooseKey() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		statusBarLabel.setText("Browse file");
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mohammad\\EclipsJEEWorkSpace\\CS402_Lab6"));
		File file = fileChooser.showOpenDialog(this.stage);
		if (file != null) {
			this.keyPath = file.getPath();
			filePathLabel.setText(keyPath);
		}
	}

	public void OpenKeyFile() {
		try {
			Open(keyPath);
			statusBarLabel.setText("Key file Opened");

		} catch (Exception e) {
			statusBarLabel.setText("Can't Open File " + keyPath);

			e.printStackTrace();
		}
	}

	public void OpenResultFile() {
		try {
			Open(resultPath);
			statusBarLabel.setText("result file Opened");

		} catch (Exception e) {
			statusBarLabel.setText("Can't Open File " + resultPath);

			e.printStackTrace();
		}
	}

	public void OpenAllKeysFile() {
		try {
			Open(allKeysPath);
			statusBarLabel.setText("allKeys file Opened");

		} catch (Exception e) {
			statusBarLabel.setText("Can't Open File " + allKeysPath);

			e.printStackTrace();
		}
	}

	public void Open(String filePath) throws IllegalArgumentException {
		File f = new File(filePath);
		Desktop dt = Desktop.getDesktop();
		try {
			dt.open(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Browse() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		statusBarLabel.setText("Browse file");
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mohammad\\EclipsJEEWorkSpace\\CS402_Lab6"));
		File file = fileChooser.showOpenDialog(this.stage);
		if (file != null) {
			filePathTF.setText(file.getPath());
		}
	}
}
