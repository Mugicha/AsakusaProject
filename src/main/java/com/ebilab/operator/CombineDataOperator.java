package com.ebilab.operator;

import com.asakusafw.vocabulary.operator.Convert;
import com.asakusafw.vocabulary.operator.MasterJoin;
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
	
	
	private final AddRateDj addRateDJ = new AddRateDj();
	@Convert
	public AddRateDj setMissingValueToDowJones(DailyRateUsdJpy dailyRate) {
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
	
	
	private final AddRateDjNikkei rateDJNikkei = new AddRateDjNikkei();
	@Convert
	public AddRateDjNikkei setMissingValueToNikkei(AddRateDj rateDJ) {
		rateDJNikkei.setDate(rateDJ.getDate());
		rateDJNikkei.setFEndRate(rateDJ.getDEndRate());
		rateDJNikkei.setFStartRate(rateDJ.getDStartRate());
		rateDJNikkei.setFMaxRate(rateDJ.getDMaxRate());
		rateDJNikkei.setFMinRate(rateDJ.getDMinRate());
		rateDJNikkei.setFDayBeforeRatio(rateDJ.getDDayBeforeRatio());
		rateDJNikkei.setDEndRate(rateDJ.getDEndRate());
		rateDJNikkei.setDStartRate(rateDJ.getDStartRate());
		rateDJNikkei.setDMaxRate(rateDJ.getDMaxRate());
		rateDJNikkei.setDMinRate(rateDJ.getDMinRate());
		rateDJNikkei.setDDekidaka(rateDJ.getDDekidaka());
		rateDJNikkei.setDDayBeforeRatio(rateDJ.getDDayBeforeRatio());
		rateDJNikkei.setNEndRate(0);
		rateDJNikkei.setNStartRate(0);
		rateDJNikkei.setNMaxRate(0);
		rateDJNikkei.setNMinRate(0);
		rateDJNikkei.setNDekidaka(0);
		rateDJNikkei.setNDayBeforeRatio(0);
		return rateDJNikkei;
	}
	
	/*
	@MasterCheck
	public abstract boolean checkTwitterStream(
			@Key(group = "date") AddRateDjNikkei rateDJNikkei,
			@Key(group = "date") SummaryTwitterStream twitterStream);
	*/
	@MasterJoin
	public abstract SummaryData checkTwitterStream(SummaryTwitterStream twitterStream, AddRateDjNikkei rateDJNikkei);
	
	
	private final SummaryData summaryData = new SummaryData();
	@Convert
	public SummaryData setMissingValueToTwitter(AddRateDjNikkei addRateDjNikkei) {
		summaryData.setDate(addRateDjNikkei.getDate());
		summaryData.setFEndRate(addRateDjNikkei.getFEndRate());
		summaryData.setFStartRate(addRateDjNikkei.getFStartRate());
		summaryData.setFMaxRate(addRateDjNikkei.getDMaxRate());
		summaryData.setFMinRate(addRateDjNikkei.getDMinRate());
		summaryData.setFDayBeforeRatio(addRateDjNikkei.getFDayBeforeRatio());
		summaryData.setDEndRate(addRateDjNikkei.getDEndRate());
		summaryData.setDStartRate(addRateDjNikkei.getDStartRate());
		summaryData.setDMaxRate(addRateDjNikkei.getDMaxRate());
		summaryData.setDMinRate(addRateDjNikkei.getDMinRate());
		summaryData.setDDekidaka(addRateDjNikkei.getDDekidaka());
		summaryData.setDDayBeforeRatio(addRateDjNikkei.getDDayBeforeRatio());
		summaryData.setNEndRate(addRateDjNikkei.getNEndRate());
		summaryData.setNStartRate(addRateDjNikkei.getNStartRate());
		summaryData.setNMaxRate(addRateDjNikkei.getNMaxRate());
		summaryData.setNMinRate(addRateDjNikkei.getNMinRate());
		summaryData.setNDekidaka(addRateDjNikkei.getNDekidaka());
		summaryData.setNDayBeforeRatio(addRateDjNikkei.getDDayBeforeRatio());
		
		summaryData.setHotWord1AsString("NA");
		summaryData.setHotWord2AsString("NA");
		summaryData.setHotWord3AsString("NA");
		summaryData.setHotWord4AsString("NA");
		summaryData.setHotWord5AsString("NA");
		summaryData.setHotWord6AsString("NA");
		summaryData.setHotWord7AsString("NA");
		summaryData.setHotWord8AsString("NA");
		summaryData.setHotWord9AsString("NA");
		summaryData.setHotWord10AsString("NA");
		summaryData.setHotWord11AsString("NA");
		summaryData.setHotWord12AsString("NA");
		summaryData.setHotWord13AsString("NA");
		summaryData.setHotWord14AsString("NA");
		summaryData.setHotWord15AsString("NA");
		summaryData.setHotWord16AsString("NA");
		summaryData.setHotWord17AsString("NA");
		summaryData.setHotWord18AsString("NA");
		summaryData.setHotWord19AsString("NA");
		summaryData.setHotWord20AsString("NA");
		summaryData.setHotWord21AsString("NA");
		summaryData.setHotWord22AsString("NA");
		summaryData.setHotWord23AsString("NA");
		summaryData.setHotWord24AsString("NA");
		summaryData.setHotWord25AsString("NA");
		summaryData.setHotWord26AsString("NA");
		summaryData.setHotWord27AsString("NA");
		summaryData.setHotWord28AsString("NA");
		summaryData.setHotWord29AsString("NA");
		summaryData.setHotWord30AsString("NA");
		summaryData.setHotWord31AsString("NA");
		summaryData.setHotWord32AsString("NA");
		summaryData.setHotWord33AsString("NA");
		summaryData.setHotWord34AsString("NA");
		summaryData.setHotWord35AsString("NA");
		summaryData.setHotWord36AsString("NA");
		summaryData.setHotWord37AsString("NA");
		summaryData.setHotWord38AsString("NA");
		summaryData.setHotWord39AsString("NA");
		summaryData.setHotWord40AsString("NA");
		summaryData.setHotWord41AsString("NA");
		summaryData.setHotWord42AsString("NA");
		summaryData.setHotWord43AsString("NA");
		summaryData.setHotWord44AsString("NA");
		summaryData.setHotWord45AsString("NA");
		summaryData.setHotWord46AsString("NA");
		summaryData.setHotWord47AsString("NA");
		summaryData.setHotWord48AsString("NA");
		summaryData.setHotWord49AsString("NA");
		summaryData.setHotWord50AsString("NA");
		
		return summaryData;
	}
}
