package com.todo.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title, desc;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 추가]\n"
				+ "제목 : ");
		
		title = sc.nextLine();
		if (list.isDuplicate(title)) {
			System.out.printf("제목이 중복되어 사용하실 수 없습니다 ! ");
			return;
		}
		
		System.out.print("내용 : ");
		desc = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(title, desc);
		list.addItem(t);
	}

	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.print("[항목 삭제]\n"
				+ "삭제할 항목의 제목 : ");
		String title = sc.nextLine();
		
		for (TodoItem item : l.getList()) {
			if (title.equals(item.getTitle())) {
				l.deleteItem(item);
				System.out.println("[" + title + "]이 삭제되었습니다.");
				break;
			}
		}
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 수정]\n"
				+ "수정할 항목의 제목 : ");
		String title = sc.nextLine().trim();
		if (!l.isDuplicate(title)) {
			System.out.println("존재하지 않습니다! ");
			return;
		}

		System.out.print("새 제목 : ");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicate(new_title)) {
			System.out.println("제목이 중복되어 사용하실 수 없습니다 ! ");
			return;
		}
		
		System.out.print("새 내용 : ");
		String new_description = sc.nextLine().trim();
		for (TodoItem item : l.getList()) {
			if (item.getTitle().equals(title)) {
				l.deleteItem(item);
				TodoItem t = new TodoItem(new_title, new_description);
				l.addItem(t);
				System.out.println("[" + title + "]이 [" + new_title+ "]로 수정되었습니다.");
			}
		}

	}

	public static void listAll(TodoList l) {
		System.out.println("<전체 목록> ");
		for (TodoItem item : l.getList()) {
			System.out.println(item.toString());
		}
	}

	public static void saveList(TodoList l, String string) {
		// TODO Auto-generated method stub
		try {
			Writer w = new FileWriter(string);
			
			for (TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadList(TodoList l, String string) {
		// TODO Auto-generated method stub
		File f = new File(string);
		if(f.isFile()) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(string));
			int count = 0;
			String item;
			while((item = br.readLine()) != null) {
				count++;
				StringTokenizer st = new StringTokenizer(item, "##");
				
				String title = st.nextToken();
				String desc = st.nextToken();
				String date = st.nextToken();
				
				TodoItem i = new TodoItem(title, desc, date);
				l.addItem(i);
			}
			br.close();
			if(count==0)
				System.out.println("todolist.txt 파일이 없습니다.");
			else
				System.out.println(count + "개의 항목을 읽었습니다. ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		}
		else
			System.out.println("todolist.txt 파일이 없습니다.");
	}
}
