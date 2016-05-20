package com.centit.support.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.centit.support.algorithm.DatetimeOpt;

/**
 * 
 * 这个类是用来对纯文本的文件追加文本内容
 * 可以用来写操作日志
 * 
 * 他有两组函数，一组静态函数打开、写入、关闭 一次完成
 * 				一组是类成员函数，可以打开，重复写入，关闭，
 * 两组成员函数用于不同的场合
 * 
 * @author codefan
 * @version 
 */
public class TxtLogFile {
	
	public static void writeLog(String sLogFileName,String slog, boolean bNewLine,boolean bShowTime){
		try {
			BufferedWriter bw = new BufferedWriter(
			         new FileWriter(sLogFileName));
			if(bShowTime)
				bw.write(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
			if(bNewLine){
				bw.write(slog+"\r\n");
			}else{
				bw.write(slog);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLogEx(String sLogFileName,String slog, boolean bNewLine,boolean bShowTime){
		try {
			BufferedWriter bw = new BufferedWriter(
			         new FileWriter(sLogFileName));
			if(bShowTime){
				bw.write(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
				System.out.print(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
			}
			if(bNewLine){
				bw.write(slog+"\r\n");
				System.out.println(slog);
			}else{
				bw.write(slog);
				System.out.print(slog);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void writeLog(String sLogFileName,String slog){
		try {
			BufferedWriter bw = new BufferedWriter(
			         new FileWriter(sLogFileName));

			bw.write(slog);
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedWriter logWriter;
	
	public boolean openLogFile(String sLogFileName){
		closeLogFile();
		boolean bOpened = false;
		try {
			logWriter = new BufferedWriter(
			         new FileWriter(sLogFileName));
			bOpened = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bOpened;
	}
	
	public boolean closeLogFile(){
		boolean bClosed = false;
		try {
			if(logWriter!=null )
				logWriter.close();
			logWriter = null;
			bClosed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bClosed;		
	}
	
	public void writeLog(String slog, boolean bNewLine,boolean bShowTime){
		try {
			if(bShowTime)
				logWriter.write(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
			if(bNewLine){
				logWriter.write(slog+"\r\n");
			}else{
				logWriter.write(slog);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLogEx(String slog, boolean bNewLine,boolean bShowTime){
		try {
			if(bShowTime){
				logWriter.write(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
				System.out.print(DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), DatetimeOpt.getDateTimePattern()));
			}
			if(bNewLine){
				logWriter.write(slog+"\r\n");
				System.out.println(slog);
			}else{
				logWriter.write(slog);
				System.out.print(slog);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
