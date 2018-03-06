package com.ebilab.flowpart;

import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.FlowPart;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory.Confluent;
import com.ebilab.operator.CombineDataOperatorFactory;
import com.ebilab.operator.CombineDataOperatorFactory.CheckDowJones;
import com.ebilab.operator.CombineDataOperatorFactory.CheckNikkei300;
import com.ebilab.operator.CombineDataOperatorFactory.CheckTwitterStream;
import com.ebilab.operator.CombineDataOperatorFactory.SetMissingValueToDowJones;
import com.ebilab.operator.CombineDataOperatorFactory.SetMissingValueToNikkei;
import com.ebilab.operator.CombineDataOperatorFactory.SetMissingValueToTwitter;
import com.example.modelgen.dmdl.model.AddRateDj;
import com.example.modelgen.dmdl.model.DailyDowJones;
import com.example.modelgen.dmdl.model.DailyNikkei300;
import com.example.modelgen.dmdl.model.DailyRateUsdJpy;
import com.example.modelgen.dmdl.model.SummaryData;
import com.example.modelgen.dmdl.model.SummaryTwitterStream;

@FlowPart
public class CombineDataFlowPart extends FlowDescription{
	
	final In<DailyRateUsdJpy> dailyRateUsdJpy;
	final In<DailyDowJones> dailyDowJones;
	final In<DailyNikkei300> dailyNikkei300;
	final In<SummaryTwitterStream> summaryTwitterStream;
	final Out<SummaryData> summaryData;
	
	public CombineDataFlowPart(
			In<DailyRateUsdJpy> dailyRateUsdJpy,
			In<DailyDowJones> dailyDowJones,
			In<DailyNikkei300> dailyNikkei300,
			In<SummaryTwitterStream> summaryTwitterStream,
			Out<SummaryData> summaryData) {
		this.dailyRateUsdJpy = dailyRateUsdJpy;
		this.dailyDowJones = dailyDowJones;
		this.dailyNikkei300 = dailyNikkei300;
		this.summaryTwitterStream = summaryTwitterStream;
		this.summaryData = summaryData;
	}
	
	@Override
	protected void describe() {
		CombineDataOperatorFactory operators = new CombineDataOperatorFactory();
		CoreOperatorFactory core = new CoreOperatorFactory();
		
		// 1. DOW JONES　の結合
		CheckDowJones checkDowJones = operators.checkDowJones(dailyDowJones, dailyRateUsdJpy);
		SetMissingValueToDowJones setMissingValueToDowJones = operators.setMissingValueToDowJones(checkDowJones.missed);
		
		Confluent<AddRateDj> confDowJones = core.confluent(checkDowJones.joined, setMissingValueToDowJones.out);
		
		// 2. Nikkei300 の結合
		CheckNikkei300 checkNikkei300 = operators.checkNikkei300(dailyNikkei300, checkDowJones.joined);
		checkNikkei300 = operators.checkNikkei300(dailyNikkei300, setMissingValueToDowJones.out);
		SetMissingValueToNikkei setMissingValueToNikkei = operators.setMissingValueToNikkei(checkNikkei300.missed);
		
		
		// 3. Twitter の結合
		CheckTwitterStream checkTwitterStream = operators.checkTwitterStream(summaryTwitterStream, checkNikkei300.joined);
		checkTwitterStream = operators.checkTwitterStream(summaryTwitterStream, setMissingValueToNikkei.out);
		SetMissingValueToTwitter setMissingValueToTwitter = operators.setMissingValueToTwitter(checkTwitterStream.missed);
		
		
		// 4. 出力
		summaryData.add(checkTwitterStream.joined);
		summaryData.add(setMissingValueToTwitter.out);
		
	}
}
