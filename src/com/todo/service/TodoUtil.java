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
import java.text.SimpleDateFormat;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title, desc, category, due_date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 추가]\n");
		System.out.print("카테고리 : ");
		category = sc.nextLine().trim();
		
		System.out.print("제목 : ");
		title = sc.nextLine().trim();
		if (list.isDuplicate(title)) {
			System.out.printf("제목이 중복되어 사용하실 수 없습니다 ! ");
			return;}
		
		System.out.print("내용 : ");
		desc = sc.nextLine().trim();
		
		System.out.print("마감날짜 ex)2021/09/26 : ");
		due_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(category, title, desc, due_date);
		list.addItem(t);
	}

	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.print("[항목 삭제]\n" + "삭제할 항목의 번호를 입력하세요 : ");
		int number = sc.nextInt();
		String d;
		
		for (TodoItem item : l.getList()) {
			if (number == l.indexOf(item)+1) {
				System.out.println(l.indexOf(item)+1 + ". " + item.toString());
				System.out.print("위 항목을 삭제하시겠습니까? (y/n) : ");
				d = sc.next();
				if(d.equals("y")) {
					l.deleteItem(item);
					System.out.println(number + "번이 삭제되었습니다.");
				}
				else {
					System.out.println("변경이 취소되었습니다.");
				}
				break;
			}
		}
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("[항목 수정]\n" + "수정할 항목의 번호를 입력하세요 : ");
		int number = sc.nextInt();
		sc.nextLine();

		if (number<1 || number > l.getList().size()) {
			System.out.println(number + "번이 존재하지 않습니다! ");
			return;
		}
		
		for (TodoItem item : l.getList()) {
			if (number == l.indexOf(item)+1) {
				System.out.println(l.indexOf(item)+1 + ". " + item.toString());
				
				System.out.print("수정할 항목의 영역들을 입력하세요 (카테고리/제목/내용/마감) : ");
				String update = sc.nextLine().trim();
				
				if(update.contains("카테고리")) {
					System.out.print("새 카테고리 : ");
					String new_category = sc.nextLine().trim();
					item.setCategory(new_category);
				}
				if(update.contains("제목")) {
					String new_title;
					while(true){
					System.out.print("새 제목 : ");
					new_title = sc.nextLine().trim();
					
					if(l.isDuplicate(new_title)) 
						System.out.println("제목이 중복되어 사용하실 수 없습니다!");
					else break;
					}
					item.setTitle(new_title);
				}
				if(update.contains("내용")) {
					System.out.print("새 내용 : ");
					String new_description = sc.nextLine().trim();
					item.setDesc(new_description);
				}
				if(update.contains("마감")) {
					System.out.print("새 마감날짜 : ");
					String new_due_date = sc.nextLine().trim();
					item.setDue_date(new_due_date);
				}
			SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
			item.setCurrent_date(f.format ( new Date()));
			break;
			}
		}
		
		System.out.println(number + "번이 변경되었습니다.");
	}
		
	public static void listAll(TodoList l) {
		System.out.println("<전체 목록, 총 " + l.getList().size() + "개>");
		for (TodoItem item : l.getList()) {
			System.out.println(l.indexOf(item)+1 + ". " + item.toString());
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
				
				String category = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due_date = st.nextToken();
				String date = st.nextToken();
				
				TodoItem i = new TodoItem(category, title, desc, due_date, date);
				l.addItem(i);
			}
			br.close();
			if(count==0)
				System.out.println("todolist.txt 파일이 없습니다.");
			else
				System.out.println(count + "개의 항목을 읽었습니다. ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (FileNotFoundException e) {
			//System.out.println("todolist.txt 파일이 존재하지 않습니다.");
		//}
		
		}
		else
			System.out.println("todolist.txt 파일이 존재하지 않습니다.");
	}

	public static void findKeyword(TodoList l, String keyword) {
		
		int number = 0;
		for (TodoItem item : l.getList()) {
			if(item.getTitle().contains(keyword) || item.getDesc().contains(keyword)) {
				System.out.println(l.indexOf(item)+1 + ". "+item.toString());
				number += 1;
			}	
		}
		System.out.println("총 "+number+"개의 항목을 찾았습니다.");
	}

	public static void findCategoryKeyword(TodoList l, String keyword) {
		
		int number = 0;
		for (TodoItem item : l.getList()) {
			if(item.getCategory().contains(keyword)) {
				System.out.println(l.indexOf(item)+1 + ". "+item.toString());
				number += 1;
			}	
		}
		System.out.println("총 "+number+"개의 항목을 찾았습니다.");
		
	}

	public static void listCategory(TodoList l) {
		
		int number = 0;
		HashSet<String> set = new HashSet<String>(l.getList().size());
		for(TodoItem item : l.getList()) {
			set.add(item.getCategory());
		}
		Iterator it = set.iterator();
		while(it.hasNext()) {
			if(number!=0) System.out.print(" / ");
			System.out.print(it.next());
			number += 1;
		}
		System.out.println("\n총 " + number + "개의 카테고리가 등록되어 있습니다.");
		
	}
}
