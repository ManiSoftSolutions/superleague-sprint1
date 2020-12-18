package com.s1.ms.sprint1.stock;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Read the stock summary from the in memory database and report the stocks
 * @author MANI
 *
 */
public class Report {
	
	/**
	 * Initialize stock summary executions
 	 * @param memory
	 */
	public void init(InMemory memory) {
		stockSummaryPerDay(memory);
		sumForToday(memory);
		top5Items(memory);
		sumByMonth(memory);
		sumByItem(memory);
	}
	
	/**
	 * Display Stock summary received for the day
	 * @param memory
	 */
	public void stockSummaryPerDay(InMemory memory) {
		System.out.println("-----Stock Summary Per Day:-------");
		System.out.println("-----Date    Id    ItemName    Price    Quantity--------");
		
		if (!memory.getInventoryList().isEmpty()) {
			
		Map<LocalDate,	List<Inventory>> list= memory.getInventoryList().stream().collect(Collectors.groupingBy(Inventory::getDate));
		list.entrySet().forEach(i->{
		System.out.println("-----"+i.getKey()+"-------");
			i.getValue().forEach(a->{
				System.out.println(a.toString());
			});
		});
		}
	}
	
	/**
	 * Sort the inventory based on month and quantity
	 * display only top 5 items
	 * @param memory
	 */
	public void top5Items(InMemory memory) {
		System.out.println("-----Sales leader board. TOP 5 items in demand. (For last 1 month) -------");
		System.out.println("-----Date    Id    ItemName    Price    Quantity--------");
		List<Inventory> sorted=memory.getInventoryList().stream().sorted(Comparator.comparing(Inventory::getDate).thenComparing(Inventory::getQuantity)).collect(Collectors.toList());
		sorted.stream().limit(5).forEach(x->{
			System.out.println(x.toString());
		});
	}
	
	/**
	 * Calculate the told items sold for the day based on the quantity  
	 * @param memory
	 */
	public void sumForToday(InMemory memory) {
		int total=memory.getInventoryList().stream().filter(i->i.getDate().equals(LocalDate.now())).collect(Collectors.summingInt(i->i.getQuantity()));
		System.out.println("-----Summary of total items for the day:-------");
		System.out.println(LocalDate.now() + " : "+total);
	}
	
	/**
	 * Calculate and display the sum of the quantity by months
	 * @param memory
	 */
	public void sumByMonth(InMemory memory) {
		Map<Object, Integer> collect=memory.getInventoryList().stream().collect(Collectors.groupingBy(i->i.getDate(),Collectors.summingInt(i->i.getQuantity())));
		System.out.println("-----Summary of total items by month-------");
		collect.entrySet().forEach(i->System.out.print(i.getKey()+" : "+i.getValue()+System.lineSeparator()));
	}
	
	/**
	 * Calculate and display the sum of the quantity by items
	 * @param memory
	 */
	public void sumByItem(InMemory memory) {
		Map<Object, Integer> collect=memory.getInventoryList().stream().collect(Collectors.groupingBy(i->i.getName(),Collectors.summingInt(i->i.getQuantity())));
		System.out.println("-----Summary of quantity of sale for one particular item-------");
		collect.entrySet().forEach(i->System.out.print(i.getKey()+" : "+i.getValue()+System.lineSeparator()));
	}
}
