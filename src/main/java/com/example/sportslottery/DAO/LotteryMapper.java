package com.example.sportslottery.DAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.sportslottery.model.Lottery;

@Mapper
public interface LotteryMapper {
	Lottery queryById(int id);
	List<Lottery> queryByNumbers(String numbers);
	List<Lottery> queryLastByNumbers(String numbers);
	int countNextByNumbers(String numbers);
	List<Lottery> queryNextByNumbers(String numbers);
}
