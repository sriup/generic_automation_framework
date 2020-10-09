beta - 0.3
==========
•	Added support for Edge browser executions
•	Updated the element highlight logic
•	Masked the passwords in the log
•	Added doc and docx to the firefox mimetype
•	Added methods to get the excel column headers based on the row index
•	Added browseFile method to handle file upload field
•	Added getElement with expected conditions
•	Added initial wait time param to the waitForInvisibilityOfElement method


beta - 0.1
===============
•	Removed javaclick (softclick) when ElementNotInteractable exception occurred, to make sure we don't miss any defects (Nageshwar Rao Gundla - C Please share the documentation on the sample scenario that we discussed)
•	Updated the logic to move the element to the center of the page (this might not still support the applications wrapped in iframes like edfr). Please use Method Override to implement project specific implementation.
•	Enhanced the logs so that we can see the more detailed information in the log which helps in root cause analysis in case any test fails.
•	IE capabilities can be controlled from the project, the default value for native events will be off. If the project requires the scripts to be executed with native defects, please use the below 3 lines in the BaseClass.
