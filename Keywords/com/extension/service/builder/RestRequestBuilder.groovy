package com.extension.service.builder

import com.extension.service.helper.BodyType
import com.extension.service.helper.Constant
import com.extension.service.helper.MethodType
import com.kms.katalon.core.exception.KatalonRuntimeException
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.FormDataBodyParameter
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent

import groovy.json.JsonOutput


class RestRequestBuilder {
	private String requestObjectName = "DefaultName";
	private String requestUUID = "";
	private String restUrl = "";
	private MethodType requestType = MethodType.GET;
	private BodyType bodyType = BodyType.TEXT_BODY;
	private List<TestObjectProperty> requestHeaders = new ArrayList<TestObjectProperty>(Arrays.asList(Constant.DEFAULT_USER_AGENT_HEADER));
	private List<TestObjectProperty> requestQueryParameters = new ArrayList<TestObjectProperty>();
	private Map<String,String> bodyData = new HashMap<String, String>();

	private static class InstanceHolder {
		private static final RestRequestBuilder INSTANCE = new RestRequestBuilder();
	}

	public static RestRequestBuilder getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private RestRequestBuilder(){}

	public RestRequestBuilder Builder(MethodType type){
		this.requestType = type;
		return this;
	}

	public RestRequestBuilder setRequestObjectName(String name){
		this.requestObjectName = name;
		return this;
	}

	public RestRequestBuilder setRequestUUID(String UUID){
		this.requestUUID = UUID;
		return this;
	}

	public RestRequestBuilder setRequestUrl(String url){
		this.restUrl = url;
		return this;
	}

	public RestRequestBuilder setRequestHeader(List<TestObjectProperty> headers){
		this.requestHeaders.addAll(headers);
		this.filterDuplicatedHeader();
		return this;
	}

	public RestRequestBuilder setRequestBodyType(BodyType type){
		this.bodyType = type;
		return this;
	}

	public RestRequestBuilder setRequestTextBodyData(Map<String,String> bodyData){
		this.bodyData = bodyData;
		return this;
	}

	public RestRequestBuilder setRequestQueryParameter(Map<String,String> requestQueryParameters){
		requestQueryParameters.each { key, value ->
			this.requestQueryParameters.add(new TestObjectProperty(key, ConditionType.EQUALS, value))
		}
		return this;
	}

	public RequestObject build(){
		RequestObject request = new RequestObject();
		this.buildRequestDescription(request);
		this.buildRequestHeader(request)
		this.buildRequestBody(request);
		return request;
	}

	private void buildRequestDescription(RequestObject request) {
		request.setName(this.requestObjectName);
		request.setObjectId(this.requestUUID.length() > 0 ? this.requestUUID : UUID.randomUUID().toString());
	}

	private void buildRequestHeader(RequestObject request){
		request.setRestUrl(this.restUrl);
		request.setRestRequestMethod(this.requestType.getMethodName())
		request.setHttpHeaderProperties(this.requestHeaders);
		request.setRestParameters(this.requestQueryParameters);
	}

	private void filterDuplicatedHeader() {
		this.requestHeaders.unique {TestObjectProperty propA, TestObjectProperty propB -> propA.getName() <=> propB.getName()}
	}

	private void buildRequestBody(RequestObject request) {
		switch(this.bodyType) {
			case BodyType.TEXT_BODY:
				request.setBodyContent(new HttpTextBodyContent(JsonOutput.toJson(this.bodyData)))
				break;
			case BodyType.FORM_DATA_BODY:
				request.setBodyContent(new HttpFormDataBodyContent(this.buildFormDataBodyContent(this.bodyData)))
				break;
			default:
				throw new KatalonRuntimeException(String.format("Request Body Type %s does not support"),this.bodyType.getMethodName());
		}
	}

	private List<FormDataBodyParameter> buildFormDataBodyContent(bodyData) {
		List<FormDataBodyParameter> parameters = []
		bodyData.each { key, value ->
			parameters.add(new FormDataBodyParameter(key,value["value"], value["type"]))
		}
		return parameters
	}
}
