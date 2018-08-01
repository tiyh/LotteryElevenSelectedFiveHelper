package com.example.sportslottery.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sportslottery.model.Lottery;
import com.example.sportslottery.service.LotteryService;

@Controller
public class LotteryController {
	  private final Logger logger = LoggerFactory.getLogger(LotteryController.class);
	  @Autowired
	  private LotteryService lotteryService;
	  
	  @RequestMapping(value="/", method = RequestMethod.GET)
	  public  String homepage() {
	    return "home";
	  }
	  
	  @RequestMapping(value="/querybybumbers", method = RequestMethod.GET)
	  public  String queryLotteryNumbers(@RequestParam(value="numbers",required=true,defaultValue="0") String numbers,
			  Model model) {
		if(numbers==null||numbers.isEmpty()) return "notFound";
		List<Lottery> findedLottery = lotteryService.queryByNumbers(numbers);
		if(findedLottery==null||findedLottery.isEmpty()) return "notFound";
		else{
			List<ArrayList<Lottery>> resultlist= new ArrayList<ArrayList<Lottery>>();
			for(Lottery lottery : findedLottery) {
				ArrayList<Lottery> s = new ArrayList<Lottery>();
				for(int i=-2;i<=1;i++){
					Lottery nearCase = lotteryService.queryById(lottery.getId()+i);
					if(nearCase!=null) s.add(nearCase);
				}
				if(!s.isEmpty()) {
					logger.warn("resultlist.size():"+resultlist.size());
					resultlist.add(s);
				}
			}
			model.addAttribute("querybybumbers",resultlist);
			return "querybybumbers";
	    }
	  }
	  
	  @RequestMapping(value="/querytwoterm", method = RequestMethod.GET)
	  public  String querylastLotteryNumbers(@RequestParam(value="beforenum",required=true,defaultValue="0") String beforenum,
			  @RequestParam(value="currentnum",required=true,defaultValue="0") String currentnum,Model model) {
		if(beforenum==null||beforenum.isEmpty()||currentnum==null||currentnum.isEmpty()) return "notFound";
		logger.warn("currentnum:"+currentnum+" beforenum:"+beforenum);
		List<Lottery> findedLottery = lotteryService.queryLastByNumbers(currentnum);
		if(findedLottery==null||findedLottery.isEmpty()) return "notFound";
		else{
			List<ArrayList<Lottery>> resultlist= new ArrayList<ArrayList<Lottery>>();
			for(Lottery lottery : findedLottery) {
				String[] strs=lottery.getNumbers().split(",");
			    List<String> nums = Arrays.asList(strs);
			    String[] befstrs=beforenum.split(",");
			    int j=0;
			    for(int i=0;i<befstrs.length;i++){
			    	if(nums.contains(befstrs[i])) j++;
			    }
			    if(j>=4){
					ArrayList<Lottery> s = new ArrayList<Lottery>();
					for(int i=-2;i<=2;i++){
						Lottery nearCase = lotteryService.queryById(lottery.getId()+i);
						if(nearCase!=null) s.add(nearCase);
					}
					if(!s.isEmpty()) {
						logger.warn("tiyh-----------resultlist.size():"+resultlist.size());
						resultlist.add(s);
					}
			    	
			    }
			}
			model.addAttribute("querybybumbers",resultlist);
			return "querybybumbers";
	    }
	  }
	  
	  @RequestMapping(value="/querybyid", method = RequestMethod.GET)
	  public  String queryLastById(@RequestParam(value="id",required=true,defaultValue="0") String id,
			  Model model) {
		int term=0;
		try {
			term = Integer.parseInt(id);
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		if(term==0){
			return "notFound";
		}
		Lottery findedLottery = lotteryService.queryById(term);
		if(findedLottery==null){
			return "notFound";
		}
		else{
			List<Lottery> list= new ArrayList<Lottery>();
			for(int i=-2;i<=1;i++){
				Lottery temp = lotteryService.queryById(term+i);
				if(temp!=null){
					list.add(temp);
				}
			}
			model.addAttribute("querybyid",list);
			return "querybyid";
	    }
	  }
	  
	  @RequestMapping(value="/countnumbers", method = RequestMethod.GET)
	  public  String countLotteryNumbers(@RequestParam(value="currentnum",required=true,defaultValue="0") String currentnum,
			  Model model) {
		if(currentnum==null||currentnum.isEmpty()) return "notFound";
		logger.warn("currentnum:"+currentnum);
		List<Lottery> nextLottery = lotteryService.queryNextByNumbers(currentnum);
		int sum = lotteryService.countNextByNumbers(currentnum);
		if(nextLottery==null||nextLottery.isEmpty()||sum<=0) return "notFound";
		else{
			HashMap<String,Integer> resultMap= new HashMap<String,Integer>();
			for(Lottery lottery : nextLottery) {
				String[] everyStrs=lottery.getNumbers().split(",");
			    for(int i=0;i<everyStrs.length;i++) {
			    	for(int k=i+1;k<everyStrs.length;k++) {
			    			String key = everyStrs[i]+','+everyStrs[k];
			    			int value = resultMap.getOrDefault(key, 0);
			    			resultMap.put(key, value+1);
			    	}
			    }
			}
			model.addAttribute("sum",sum);
			model.addAttribute("resultmap",resultMap);
			return "countnumbers";
	    }
	  }
	  
	  @RequestMapping(value="/countthreenumbers", method = RequestMethod.GET)
	  public  String countThreeNumbers(@RequestParam(value="currentnum",required=true,defaultValue="0") String currentnum,
			  Model model) {
		if(currentnum==null||currentnum.isEmpty()) return "notFound";
		logger.warn("currentnum:"+currentnum);
		List<Lottery> nextLottery = lotteryService.queryNextByNumbers(currentnum);
		int sum = lotteryService.countNextByNumbers(currentnum);
		if(nextLottery==null||nextLottery.isEmpty()||sum<=0) return "notFound";
		else{
			HashMap<String,Integer> resultMap= new HashMap<String,Integer>();
			for(Lottery lottery : nextLottery) {
				String[] everyStrs=lottery.getNumbers().split(",");
			    for(int i=0;i<everyStrs.length;i++) {
			    	for(int k=i+1;k<everyStrs.length;k++) {
			    		for(int m=k+1;m<everyStrs.length;m++) {
			    			String key = everyStrs[i]+','+everyStrs[k]+','+everyStrs[m];
			    			int value = resultMap.getOrDefault(key, 0);
			    			resultMap.put(key, value+1);
			    		}
			    	}
			    }
			}
			model.addAttribute("sum",sum);
			model.addAttribute("resultmap",resultMap);
			return "countnumbers";
	    }
	  }
	  
}
