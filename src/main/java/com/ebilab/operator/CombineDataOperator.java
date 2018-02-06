package com.ebilab.operator;

import com.asakusafw.vocabulary.operator.Convert;
import com.asakusafw.vocabulary.operator.MasterJoin;
import com.asakusafw.vocabulary.operator.Update;
import com.example.modelgen.dmdl.model.AddRateDj;
import com.example.modelgen.dmdl.model.AddRateDjNikkei;
import com.example.modelgen.dmdl.model.DailyDowJones;
import com.example.modelgen.dmdl.model.DailyNikkei300;
import com.example.modelgen.dmdl.model.DailyRateUsdJpy;
import com.example.modelgen.dmdl.model.SummaryData;
import com.example.modelgen.dmdl.model.SummaryTwitterStream;

public abstract class CombineDataOperator {
	/*
	@MasterCheck
	public abstract boolean checkDowJones(
			@Key(group = "date") DailyDowJones dowJones,
			@Key(group = "date") DailyRateUsdJpy rateUSDJPY);
	*/
	@MasterJoin
	public abstract AddRateDj checkDowJones(DailyDowJones dowJones, DailyRateUsdJpy rateUSDJPY);
	
	
	@Convert
	public AddRateDj setMissingValueToDowJones(DailyRateUsdJpy dailyRate) {
		AddRateDj addRateDJ = new AddRateDj();
		
		addRateDJ.setDate(dailyRate.getDate());
		addRateDJ.setFEndRate(dailyRate.getEndRate());
		addRateDJ.setFStartRate(dailyRate.getStartRate());
		addRateDJ.setFMaxRate(dailyRate.getMaxRate());
		addRateDJ.setFMinRate(dailyRate.getMinRate());
		addRateDJ.setFDayBeforeRatio(dailyRate.getDayBeforeRatio());
		addRateDJ.setDEndRate(0);
		addRateDJ.setDStartRate(0);
		addRateDJ.setDMaxRate(0);
		addRateDJ.setDMinRate(0);
		addRateDJ.setDDekidaka(0);
		addRateDJ.setDDayBeforeRatio(0);
		
		return addRateDJ;
	}
	
	/*
	@MasterCheck
	public abstract boolean checkNikkei300(
			@Key(group = "date") AddRateDj rateDJ,
			@Key(group = "date") DailyNikkei300 nikkei300);
	*/
	@MasterJoin
	public abstract AddRateDjNikkei checkNikkei300(DailyNikkei300 nikkei300, AddRateDj rateDJ);
	
	@Update
	public void setMissingValueToNikkei(AddRateDjNikkei rateDJNikkei) {
		rateDJNikkei.setNEndRate(0);
		rateDJNikkei.setNStartRate(0);
		rateDJNikkei.setNMaxRate(0);
		rateDJNikkei.setNMinRate(0);
		rateDJNikkei.setNDekidaka(0);
		rateDJNikkei.setNDayBeforeRatio(0);
	}
	
	/*
	@MasterCheck
	public abstract boolean checkTwitterStream(
			@Key(group = "date") AddRateDjNikkei rateDJNikkei,
			@Key(group = "date") SummaryTwitterStream twitterStream);
	*/
	@MasterJoin
	public abstract SummaryData checkTwitterStream(SummaryTwitterStream twitterStream, AddRateDjNikkei rateDJNikkei);
	
	@Update
	public void setMissingValueToTwitter(SummaryTwitterStream stream) {
		stream.setHotWord1AsString("NA");
		stream.setHotWord2AsString("NA");
		stream.setHotWord3AsString("NA");
		stream.setHotWord4AsString("NA");
		stream.setHotWord5AsString("NA");
		stream.setHotWord6AsString("NA");
		stream.setHotWord7AsString("NA");
		stream.setHotWord8AsString("NA");
		stream.setHotWord9AsString("NA");
		stream.setHotWord10AsString("NA");
		stream.setHotWord11AsString("NA");
		stream.setHotWord12AsString("NA");
		stream.setHotWord13AsString("NA");
		stream.setHotWord14AsString("NA");
		stream.setHotWord15AsString("NA");
		stream.setHotWord16AsString("NA");
		stream.setHotWord17AsString("NA");
		stream.setHotWord18AsString("NA");
		stream.setHotWord19AsString("NA");
		stream.setHotWord20AsString("NA");
		stream.setHotWord21AsString("NA");
		stream.setHotWord22AsString("NA");
		stream.setHotWord23AsString("NA");
		stream.setHotWord24AsString("NA");
		stream.setHotWord25AsString("NA");
		stream.setHotWord26AsString("NA");
		stream.setHotWord27AsString("NA");
		stream.setHotWord28AsString("NA");
		stream.setHotWord29AsString("NA");
		stream.setHotWord30AsString("NA");
		stream.setHotWord31AsString("NA");
		stream.setHotWord32AsString("NA");
		stream.setHotWord33AsString("NA");
		stream.setHotWord34AsString("NA");
		stream.setHotWord35AsString("NA");
		stream.setHotWord36AsString("NA");
		stream.setHotWord37AsString("NA");
		stream.setHotWord38AsString("NA");
		stream.setHotWord39AsString("NA");
		stream.setHotWord40AsString("NA");
		stream.setHotWord41AsString("NA");
		stream.setHotWord42AsString("NA");
		stream.setHotWord43AsString("NA");
		stream.setHotWord44AsString("NA");
		stream.setHotWord45AsString("NA");
		stream.setHotWord46AsString("NA");
		stream.setHotWord47AsString("NA");
		stream.setHotWord48AsString("NA");
		stream.setHotWord49AsString("NA");
		stream.setHotWord50AsString("NA");
	}
}
