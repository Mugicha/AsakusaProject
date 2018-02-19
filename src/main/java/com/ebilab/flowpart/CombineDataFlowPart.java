package com.ebilab.flowpart;

import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.FlowPart;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory;
import com.ebilab.operator.CombineDataOperatorFactory;
import com.ebilab.operator.CombineDataOperatorFactory.CheckDowJones;
import com.ebilab.operator.CombineDataOperatorFactory.CheckNikkei300;
import com.ebilab.operator.CombineDataOperatorFactory.CheckTwitterStream;
import com.ebilab.operator.CombineDataOperatorFactory.SetMissingValueToDowJones;
import com.ebilab.operator.CombineDataOperatorFactory.SetMissingValueToNikkei;
import com.example.modelgen.dmdl.model.DailyDowJones;
import com.example.modelgen.dmdl.model.DailyNikkei300;
import com.example.modelgen.dmdl.model.DailyRateUsdJpy;
import com.example.modelgen.dmdl.model.SummaryData;

@FlowPart
public class CombineDataFlowPart extends FlowDescription{
	
	final In<DailyRateUsdJpy> dailyRateUsdJpy;
	final In<DailyDowJones> dailyDowJones;
	final In<DailyNikkei300> dailyNikkei300;
	final Out<SummaryData> summaryData;
	
	public CombineDataFlowPart(
			In<DailyRateUsdJpy> dailyRateUsdJpy,
			In<DailyDowJones> dailyDowJones,
			In<DailyNikkei300> dailyNikkei300,
			Out<SummaryData> summaryData) {
		this.dailyRateUsdJpy = dailyRateUsdJpy;
		this.dailyDowJones = dailyDowJones;
		this.dailyNikkei300 = dailyNikkei300;
		this.summaryData = summaryData;
	}
	
	@Override
	protected void describe() {
		CombineDataOperatorFactory operators = new CombineDataOperatorFactory();
		
		CheckDowJones checkDowJones = operators.checkDowJones(dailyDowJones, dailyRateUsdJpy);
		
		SetMissingValueToDowJones setMissingValueToDowJones = operators.setMissingValueToDowJones(checkDowJones.missed);
		
		CheckNikkei300 checkNikkei300 = operators.checkNikkei300(dailyNikkei300, checkDowJones.joined);
		
		SetMissingValueToNikkei setMissingValueToNikkei = operators.setMissingValueToNikkei(checkNikkei300.missed);
		
		CheckTwitterStream checkTwitterStream = operators.checkTwitterStream(twitterStream, checkNikkei300.joined);
	}
}
