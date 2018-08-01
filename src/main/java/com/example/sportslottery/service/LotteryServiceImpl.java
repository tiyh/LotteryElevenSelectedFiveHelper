package com.example.sportslottery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sportslottery.DAO.LotteryMapper;
import com.example.sportslottery.model.Lottery;
@Service
public class LotteryServiceImpl implements LotteryService {
	@Autowired
	private LotteryMapper lotteryMapper;
	@Override
	public Lottery queryById(int id) {
		return lotteryMapper.queryById(id);
	}

	@Override
	public List<Lottery> queryByNumbers(String numbers) {
		return lotteryMapper.queryByNumbers(numbers);
	}

	@Override
	public List<Lottery> queryLastByNumbers(String numbers) {
		return lotteryMapper.queryLastByNumbers(numbers);
	}

	@Override
	public int countNextByNumbers(String numbers) {
		return lotteryMapper.countNextByNumbers(numbers);
	}
	@Override
	public List<Lottery> queryNextByNumbers(String numbers){
		return lotteryMapper.queryNextByNumbers(numbers);
	}
}
