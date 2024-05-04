package com.dev.restaurant;

import com.dev.restaurant.services.IStaffService;
import com.dev.restaurant.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class RestaurantApplication implements CommandLineRunner {
	@Autowired
	private final IStaffService staffService;

	@Override
	public void run(String... args) throws Exception {
		staffService.staffInitialization();
	}

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}


}

