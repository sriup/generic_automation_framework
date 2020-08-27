beta - 0.2
==========
08/13/2020
==========
- Masked the passwords in the log
- Added doc and docx to the firefox mimetype

08/20/2020
===========
- Added methods to get the excel column headers based on the row index
- Added browseFile method to handle file upload field
- Added getElement with expected conditions
- Fixed fullpage screenshot (removed duplicate area in the full page screenshot)


beta - 0.1
===============
- Removed javaclick (softclick) when ElementNotInteractable exception occurred, to make sure we don't miss any defects

- Updated the logic to move the element to the center of the page (this might not still support the applications wrapped in iframes like edfr). Please use Method Override to implement project specific implementation.

- Enhanced the logs so that we can see the more detailed information in the log which helps in root cause analysis in case any test fails.

- IE capabilities can be controlled from the project, the default value for native events will be off. If the project requires the scripts to be executed with native defects, please use the below 3 lines in the BaseClass.
