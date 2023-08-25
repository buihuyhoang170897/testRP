import com.extension.reportportal.service.LaunchService
import com.extension.reportportal.service.LoggingService
import com.extension.reportportal.service.TestItemService
import com.extension.service.helper.Constant

import internal.GlobalVariable

LaunchService launch = new LaunchService("test start Launch ",GlobalVariable.RP_NAME,"i want create lunch")
def res= launch.startLaunch()
def launchId = res.id
LoggingService logging = new LoggingService(launch)

TestItemService test = new TestItemService(launch)
res = test.startTestSuite("new test suite", launchId)
suiteId = res.id

res = test.startTestCase("test case two",suiteId,launchId)
testId = res.id

res = test.startNestedTestStep("Check pass stepNestedId", testId, launchId)
stepNestedId = res.id

logging.logInfo(stepNestedId, "Check pass stepNestedId")

test.finishTestStep(stepNestedId,Constant.REPORTPORTAL_PASS_STATUS)

//res = test.startTestStep("Check pass stepId",testId,launchId)
//stepId = res.id
//logging.logInfo(stepId, "Check pass stepId")
//test.finishTestStep(stepId,Constant.REPORTPORTAL_PASS_STATUS)

test.finishTestCase(testId, Constant.REPORTPORTAL_PASS_STATUS)
test.finishTestSuite(suiteId, Constant.REPORTPORTAL_PASS_STATUS)

launch.finishLaunch(launchId)