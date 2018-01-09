package com.funnel.smarker;

import java.util.ArrayList;
import java.util.List;

import com.funnel.smarker.builder.IDataBuilder;
import com.funnel.smarker.builder.TestDataBuilder;

public class TestSuperMarker {

	public static void main(String[] args) throws Exception {
		String file = "test.ftl";
		SuperMarker hf = new SuperMarker(SuperMarker.DEFAULT_FTL_DIR, file);
		List<IDataBuilder> builders = new ArrayList<IDataBuilder>();
		builders.add(new TestDataBuilder());
		hf.setBuilders(builders);
		hf.make();
	}
}
