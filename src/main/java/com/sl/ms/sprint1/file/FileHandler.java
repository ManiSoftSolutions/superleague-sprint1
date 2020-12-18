package com.sl.ms.sprint1.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.s1.ms.sprint1.stock.InMemory;
import com.s1.ms.sprint1.stock.Inventory;
import com.s1.ms.sprint1.stock.Report;
/**
 * This class stores the stocks received on daily basis into inmemory db and initializes the summary reporting
 * @author MANI
 *
 */
public class FileHandler implements MessageHandler{
	
	public String destdir="";
	List<Inventory> singletonList=new ArrayList<>();
	public FileHandler(String destdir) {
		this.destdir=destdir;
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		InMemory memory=new InMemory();
		List<Inventory> list=new ArrayList<>();
		File file=(File)message.getPayload();
		String filename=destdir+"/inventory_"+System.currentTimeMillis()+".csv";
		try {
			BufferedReader readFile = new BufferedReader(new FileReader(file.getPath()));
			list=readFile.lines().skip(1).map(maptoclass).collect(Collectors.toList());
			//Optional<Inventory> optional=readFile.lines().skip(1).map(maptoclass).reduce((x,y)->x.getId()>0?x:y);
			//singletonList.add(optional.get());
			singletonList.addAll(list);
			memory.setInventoryList(singletonList);
			new Report().init(memory);
			list.clear();
			readFile.close();
			FileUtils.moveFile(file, new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Function<String,Inventory> maptoclass =(line)-> {
		Inventory cls=new Inventory();
		String[] items= line.split(",");
		
		cls.setDate(LocalDate.now());
		if(items[0]!=null)
			cls.setId(Integer.parseInt(items[0]));
		if(items[1]!=null)
			cls.setName(items[1]);
		if(items[2]!=null)
			cls.setPrice(Double.parseDouble(items[2]));
		if(items[3]!=null)
			cls.setQuantity(Integer.parseInt(items[3]));
		return cls;
	};

}
