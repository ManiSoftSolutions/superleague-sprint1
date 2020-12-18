package com.sl.ms.sprint1;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@ComponentScan(basePackages = "com.sl.ms.sprint1.*")
public class SuperLeagueSprint1 implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SuperLeagueSprint1.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("...........Stock Summary/Inventory Management Application Started......");
		//exit(0);
	}

}
