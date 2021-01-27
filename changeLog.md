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
