package com.example.sportslottery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.sportslottery.DAO")
public class SportsLotteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsLotteryApplication.class, args);
	}
}
