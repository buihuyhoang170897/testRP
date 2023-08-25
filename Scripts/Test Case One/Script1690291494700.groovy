import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import test.APIService

APIService api = new APIService()
ResponseObject res = api.callCatAPI("100")

KeywordUtil.logInfo("fact: "+WS.getElementText(res, "fact"))
