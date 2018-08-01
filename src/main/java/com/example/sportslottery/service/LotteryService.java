package com.example.sportslottery.service;

import java.util.List;

import com.example.sportslottery.model.Lottery;

public interface LotteryService {
	Lottery queryById(int id);
	List<Lottery> queryByNumbers(String numbers);
	List<Lottery> queryLastByNumbers(String numbers);
}
