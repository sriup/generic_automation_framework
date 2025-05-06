beta - 0.12.1
============
• Updated the logic to download 64 bit chromedriver.


beta - 0.12.0
============
• Updated the source, logic to download chromedriver and launch the chrome.


beta - 0.11.0
============
• Adding capability to get the Edge downloaded file name and added allure report consolidation.
• Adding back the option to capture the screenshots irrespective of execution on SBox or local.

beta - 0.10.6
============
• Adding method to get the sheet names in the Excel workbook.


beta - 0.10.5
============
• updated Jira utility methods to handle the exceptions and un-auth scenarios rather stopping the script execution.

beta - 0.10.3 & 0.10.4
======================
• Handled Log4J2 updates and LogAccess modifications.


beta - 0.10.2
============
• Added methods to highlight the element border, waitUntilDownloadCompleted to handle 
  negative expected conditions.
  
• Added getRows with fall back mechanism, so that if max number of rows sent to the
    method are not available it will get the maximum available rows based on the
    fallback param value.

beta - 0.10.1
============
• Added methods to create, commit changes to a branch and perform action on the pull request

• Added method in ApiMethods to send the request data as part of method call.

• Added EmailUtil 


beta - 0.10
============
• Corrected regular expression to display the screenshot name correctly


beta - 0.9
============
• Removed the log message when the header is Authorization

• Updated the isElementDisplayed and isElementPresence methods to
    return false is the element is null.
    


beta - 0.8
============
• Introduced Jira Connector for jira integration with the automation scripts.

• Added method to get value from the rest api response using the json path

• Added method to pass the maximum timeout to wait for the element as part of method operations like
	click on the element related method with the maximum timeout for the element.
 

beta - 0.7.1
============
• Added method to copy file using new folder file path and new file name

• Added method to parse and parse the timestamp

• Added method to get the duration between two stamps

beta - 0.7
==========
• Updated the dependencies in the pom.xml based on the latest stable versions.

• Added logic to have control on the SoftAssert screenshot taking choice

• Removed IE related steps in the CommonFunctions

beta - 0.6.2
============
•	minor release to handle the stale element exception while a highlight and un-highlight elements

beta - 0.6.1
============
•	minor release to handle one of the chromedriver defect

beta - 0.6
==========
•	Updated the capture chunks method to calculate the reminder of the
    area correctly
    
•	Corrected isElementEnabled method call.

beta - 0.5
==========
•	Updated the un-zip logic to handle the sub-folders in the zip file

•	Added support Soft Assert instance creation on demand
 
	Useful to create new SoftAssert instance per test, without initializing the browser per each test.
	Note: Should be careful when using this, tests should NOT run in parallel.

beta - 0.3
==========
•	Added support for Edge browser executions

•	Updated the element highlight logic

•	Masked the passwords in the log

•	Added doc and docx to the firefox mimetype

•	Added methods to get the Excel column headers based on the row index

•	Added browseFile method to handle file upload field

•	Added getElement with expected conditions

•	Added initial wait time param to the waitForInvisibilityOfElement method


beta - 0.1
===============
•	Removed javaclick (softclick) when ElementNotIntractable exception occurred, to make sure we don't miss any defects

•	Updated the logic to move the element to the center of the page. Please use Method Override to implement project specific implementation.

•	Enhanced the logs so that we can see the more detailed information in the log which helps in root cause analysis in case any test fails.

•	IE capabilities can be controlled from the project, the default value for native events will be off. If the project requires the scripts to be executed with native defects, please use the below 3 lines in the BaseClass.