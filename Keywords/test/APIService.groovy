package test


import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

public class APIService {

	public static RequestObject createCatAPIRequestObject(String maxLength) {
		def builder = new RestRequestObjectBuilder()
		return  builder.withRestRequestMethod("GET")
				.withRestUrl("https://catfact.ninja/fact")
				.withRestParameters([
					new TestObjectProperty("max_length", ConditionType.EQUALS,"${maxLength}")
				])
				.withHttpHeaders([
					new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json")
				])
				.build()
	}

	public static ResponseObject sendCatRequest(RequestObject requestObject) {
		return WS.sendRequest(requestObject)
	}


	public static ResponseObject callCatAPI (String maxLength) {

		KeywordUtil.logInfo("maxLength: ${ maxLength}")
		RequestObject req =  createCatAPIRequestObject(maxLength)
		KeywordUtil.logInfo("request: "+ req.getRestUrl())
		ResponseObject res = sendCatRequest(req)
		KeywordUtil.logInfo("response: "+ res.getResponseText())
		return res
	}
}
