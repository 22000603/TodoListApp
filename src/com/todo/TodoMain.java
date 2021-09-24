package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() {
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		boolean isList = false;
		boolean quit = false;
		String keyword;
		TodoUtil.loadList(l, "todolist.txt");
		Menu.displaymenu();
		do {
			
			Menu.prompt();
			isList = false;
			String choice = sc.next();
			//sc.next();
			
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l);
				break;

			case "ls_name_asc":
				l.sortByName();
				System.out.println("정렬 방법: 제목");
				isList = true;
				break;

			case "ls_name_desc":
				l.sortByName();
				l.reverseList();
				isList = true;
				break;
				
			case "ls_date":
				l.sortByDate();
				isList = true;
				break;

			case "exit":
				quit = true;
				break;
			
			case "help":
				Menu.displaymenu();
				break;
				
			case "find":
				keyword = sc.next();
				TodoUtil.findKeyword(l, keyword);
				break;
				
			case "find_cate":
				keyword = sc.next();
				TodoUtil.findCategoryKeyword(l, keyword);
				break;
				
			case "ls_date_desc":
				l.sortByDateDesc();
				isList = true;
				break;
				
			case "ls_cate":
				TodoUtil.listCategory(l);
				break;

			default:
				System.out.println("존재하지 않는 기능입니다.\n목록을 보시려면 help를 입력해 주세요.");
				break;
			}
			
			//if(isList) l.listAll();
			if(isList) TodoUtil.listAll(l);
		} while (!quit);
		TodoUtil.saveList(l, "todolist.txt");
		System.out.print("모든 데이터가 저장되었습니다.");
	}
}
