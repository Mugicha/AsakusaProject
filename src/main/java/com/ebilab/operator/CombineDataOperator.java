package com.ebilab.operator;

import java.text.SimpleDateFormat;
import java.util.List;

import com.asakusafw.runtime.core.Result;
import com.asakusafw.runtime.value.Date;
import com.asakusafw.runtime.value.DateUtil;
import com.asakusafw.vocabulary.model.Key;
import com.asakusafw.vocabulary.operator.Convert;
import com.asakusafw.vocabulary.operator.GroupSort;
import com.asakusafw.vocabulary.operator.MasterJoin;
import com.ebilab.modelgen.dmdl.model.AddRateDj;
import com.ebilab.modelgen.dmdl.model.AddRateDjNikkei;
import com.ebilab.modelgen.dmdl.model.DailyDowJones;
import com.ebilab.modelgen.dmdl.model.DailyNikkei300;
import com.ebilab.modelgen.dmdl.model.DailyRateUsdJpy;
import com.ebilab.modelgen.dmdl.model.SummaryData;
import com.ebilab.modelgen.dmdl.model.SummaryTwitterStream;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;

public abstract class CombineDataOperator {
    private final SummaryTwitterStream ddwm = new SummaryTwitterStream();
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
	/**
	 * Twitter縦横変換。
	 * @param in
	 * @param out
	 */
    @GroupSort
    public void reformatTop50(
    		@Key(group = "date", order = { "tf_idf DESC", "word ASC" }) List<DayWordCountModel> in,
            Result<SummaryTwitterStream> out) {
        int limit = 50;
        //	単語数が５０に満たない場合は小さい方に合わせる。
        if(limit < in.size())limit = in.size();
        ddwm.setDate(Date.valueOf(in.get(0).getDateOption(), Date.Format.SIMPLE));
        for(int i=0;i<limit;i++){
        	switch (i) {
			case 1:ddwm.setHotWord1(in.get(i).getWord());break;
			case 2:ddwm.setHotWord2(in.get(i).getWord());break;
			case 3:ddwm.setHotWord3(in.get(i).getWord());break;
			case 4:ddwm.setHotWord4(in.get(i).getWord());break;
			case 5:ddwm.setHotWord5(in.get(i).getWord());break;
			case 6:ddwm.setHotWord6(in.get(i).getWord());break;
			case 7:ddwm.setHotWord7(in.get(i).getWord());break;
			case 8:ddwm.setHotWord8(in.get(i).getWord());break;
			case 9:ddwm.setHotWord9(in.get(i).getWord());break;
			case 10:ddwm.setHotWord10(in.get(i).getWord());break;
			case 11:ddwm.setHotWord11(in.get(i).getWord());break;
			case 12:ddwm.setHotWord12(in.get(i).getWord());break;
			case 13:ddwm.setHotWord13(in.get(i).getWord());break;
			case 14:ddwm.setHotWord14(in.get(i).getWord());break;
			case 15:ddwm.setHotWord15(in.get(i).getWord());break;
			case 16:ddwm.setHotWord16(in.get(i).getWord());break;
			case 17:ddwm.setHotWord17(in.get(i).getWord());break;
			case 18:ddwm.setHotWord18(in.get(i).getWord());break;
			case 19:ddwm.setHotWord19(in.get(i).getWord());break;
			case 20:ddwm.setHotWord20(in.get(i).getWord());break;
			case 21:ddwm.setHotWord21(in.get(i).getWord());break;
			case 22:ddwm.setHotWord22(in.get(i).getWord());break;
			case 23:ddwm.setHotWord23(in.get(i).getWord());break;
			case 24:ddwm.setHotWord24(in.get(i).getWord());break;
			case 25:ddwm.setHotWord25(in.get(i).getWord());break;
			case 26:ddwm.setHotWord26(in.get(i).getWord());break;
			case 27:ddwm.setHotWord27(in.get(i).getWord());break;
			case 28:ddwm.setHotWord28(in.get(i).getWord());break;
			case 29:ddwm.setHotWord29(in.get(i).getWord());break;
			case 30:ddwm.setHotWord30(in.get(i).getWord());break;
			case 31:ddwm.setHotWord31(in.get(i).getWord());break;
			case 32:ddwm.setHotWord32(in.get(i).getWord());break;
			case 33:ddwm.setHotWord33(in.get(i).getWord());break;
			case 34:ddwm.setHotWord34(in.get(i).getWord());break;
			case 35:ddwm.setHotWord35(in.get(i).getWord());break;
			case 36:ddwm.setHotWord36(in.get(i).getWord());break;
			case 37:ddwm.setHotWord37(in.get(i).getWord());break;
			case 38:ddwm.setHotWord38(in.get(i).getWord());break;
			case 39:ddwm.setHotWord39(in.get(i).getWord());break;
			case 40:ddwm.setHotWord40(in.get(i).getWord());break;
			case 41:ddwm.setHotWord41(in.get(i).getWord());break;
			case 42:ddwm.setHotWord42(in.get(i).getWord());break;
			case 43:ddwm.setHotWord43(in.get(i).getWord());break;
			case 44:ddwm.setHotWord44(in.get(i).getWord());break;
			case 45:ddwm.setHotWord45(in.get(i).getWord());break;
			case 46:ddwm.setHotWord46(in.get(i).getWord());break;
			case 47:ddwm.setHotWord47(in.get(i).getWord());break;
			case 48:ddwm.setHotWord48(in.get(i).getWord());break;
			case 49:ddwm.setHotWord49(in.get(i).getWord());break;
			case 50:ddwm.setHotWord50(in.get(i).getWord());break;
			default:
				break;
			}
        }
        out.add(ddwm);
    }
}
