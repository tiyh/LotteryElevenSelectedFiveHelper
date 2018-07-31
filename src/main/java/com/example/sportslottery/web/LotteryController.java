package com.example.sportslottery.web;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sportslottery.DAO.LotteryMapper;
import com.example.sportslottery.model.Lottery;

@Controller
public class LotteryController {
	  private LotteryMapper lotteryMapper;
	  @RequestMapping(value="/", method = RequestMethod.GET)
	  public  String homepage() {
	    System.out.println("HOME");
	    return "home";
	  }
	  @RequestMapping(value="/lottery", method = RequestMethod.GET)
	  public  String queryLotteryNumbers(@RequestParam(value="numbers",required=true,defaultValue="0") String numbers,
			  Model model) {
		/*int term=0;
		try {
			term = Integer.parseInt(id);
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}*/
		if(numbers==null||numbers.isEmpty()) return "notFound";
		List<Lottery> findedLottery = lotteryMapper.queryByNumbers(numbers);
		if(findedLottery==null||findedLottery.isEmpty()) return "notFound";
		else{
			List<String> resultlist= new ArrayList<String>();
			for(Lottery lottery : findedLottery) {
				StringBuilder s = new StringBuilder();
				for(int i=-3;i<4;i++){
					Lottery nearCase = lotteryMapper.queryById(lottery.getId()+i);
					if(nearCase!=null) s.append(nearCase.toString());
				}
				if(s.length()!=0) resultlist.add(s.toString());
			}
			model.addAttribute(resultlist);
			return "profile";
	    }
	  }
	  @RequestMapping(value="/unionlottery", method = RequestMethod.GET)
	  public  String queryLotteryTwo(@RequestParam(value="id",required=true,defaultValue="0") String id,
			  Model model) {
		int term=0;
		try {
			term = Integer.parseInt(id);
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		if(term==0) return "notFound";
		Lottery findedLottery = lotteryMapper.queryById(term);
		if(findedLottery==null) return "notFound";
		else{
			List<Lottery> list= new ArrayList<Lottery>();
			for(int i=-3;i<4;i++){
				Lottery temp = lotteryMapper.queryById(term+i);
				if(temp!=null){
					String[] strs=temp.getNumbers().split(",");
				    List nums = Arrays.asList(strs);
				    nums.contains("");
					list.add(temp);
				}
			}
			model.addAttribute(list);
			return "profile";
	    }
	  }
	  
}
